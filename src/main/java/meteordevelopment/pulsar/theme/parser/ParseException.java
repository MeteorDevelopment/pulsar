package meteordevelopment.pulsar.theme.parser;

public class ParseException extends RuntimeException {
    public final Token token;

    public ParseException(Token token, String message) {
        super(message);
        this.token = token;
    }
}
