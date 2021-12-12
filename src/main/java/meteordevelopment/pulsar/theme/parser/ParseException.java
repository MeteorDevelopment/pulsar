package meteordevelopment.pulsar.theme.parser;

public class ParseException extends RuntimeException {
    public final String file;
    public final Token token;

    public ParseException(String file, Token token, String message) {
        super(String.format("%s [line %d at '%s']: %s", file, token.line(), token.lexeme(), message));

        this.file = file;
        this.token = token;
    }
}
