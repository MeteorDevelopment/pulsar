package meteordevelopment.pulsar.theme.parser;

import java.io.IOException;
import java.io.Reader;

public class Scanner {
    private final Reader reader;
    private final StringBuilder sb = new StringBuilder();

    private char current, next;
    private int line = 1;

    public Scanner(Reader reader) {
        this.reader = reader;

        advance(false);
        advance(false);
    }

    public Token next() {
        sb.setLength(0);

        skipWhitespace();
        if (isAtEnd()) return token(TokenType.Eof);

        char c = advance();

        if (isDigit(c)) return number();
        if (isAlpha(c)) return identifier();

        return switch (c) {
            case '{' -> token(TokenType.LeftBrace);
            case '}' -> token(TokenType.RightBrace);
            case '.' -> token(TokenType.Dot);
            case ',' -> token(TokenType.Comma);
            case ':' -> token(TokenType.Colon);
            case '@' -> token(TokenType.At);

            case '#' -> hex();
            case '"' -> string();

            default -> error("Unexpected character.");
        };
    }

    private Token number() {
        while (isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while (isDigit(peek())) advance();
        }

        return token(TokenType.Number);
    }

    private Token identifier() {
        while (isAlpha(peek()) || isDigit(peek())) advance();
        return token(TokenType.Identifier);
    }

    private Token hex() {
        while (isLetter(peek()) || isDigit(peek())) advance();

        return token(TokenType.Hex);
    }

    private Token string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) return error("Unterminated string.");

        advance();

        return token(TokenType.String);
    }

    private void skipWhitespace() {
        while (true) {
            if (isMeaninglessWhitespace(peek())) {
                advance(false);
                continue;
            }

            switch (peek()) {
                case '\n' -> {
                    incrementLine();
                    advance(false);
                }
                case '/' -> {
                    if (peekNext() == '/') {
                        while (peek() != '\n' && !isAtEnd()) advance(false);
                    }
                    else if (peekNext() == '*') {
                        advance(false);
                        advance(false);

                        while ((peek() != '*' || peekNext() != '/') && !isAtEnd()) {
                            advance(false);
                        }

                        advance(false);
                        advance(false);
                    }
                    else return;
                }
                default -> {
                    return;
                }
            }
        }
    }

    // Utils

    private char advance() {
        return advance(true);
    }

    private char advance(boolean append) {
        if (append) sb.append(current);

        char prev = current;
        current = next;

        try {
            int i = reader.read();
            next = i == -1 ? '\0' : (char) i;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prev;
    }

    private char peek() {
        return current;
    }

    private char peekNext() {
        return next;
    }

    private boolean isAtEnd() {
        return peek() == '\0';
    }

    private boolean isMeaninglessWhitespace(char c) {
        return c == ' ' || c == '\r' || c == '\t';
    }

    private void incrementLine() {
        line++;
    }

    // Character functions

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isAlpha(char c) {
        return isLetter(c) || c == '_' || c == '-';
    }

    // Token creation

    private Token token(TokenType type) {
        return new Token(type, sb.toString(), line);
    }

    private Token error(String msg) {
        return new Token(TokenType.Error, msg, line);
    }
}
