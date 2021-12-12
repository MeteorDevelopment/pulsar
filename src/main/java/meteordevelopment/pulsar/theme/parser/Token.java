package meteordevelopment.pulsar.theme.parser;

public record Token(TokenType type, String lexeme, int line) {
    public String idk() {
        return lexeme.substring(1, lexeme.length() - 1);
    }
}
