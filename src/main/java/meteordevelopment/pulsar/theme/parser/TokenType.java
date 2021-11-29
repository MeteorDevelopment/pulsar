package meteordevelopment.pulsar.theme.parser;

public enum TokenType {
    // Single-character tokens
    LeftBrace, RightBrace,
    Dot, Comma, Colon, At,

    // Literals
    Number, Hex, String, Identifier,

    // Other
    Error, Eof
}
