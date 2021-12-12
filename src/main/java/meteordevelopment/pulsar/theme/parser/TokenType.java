package meteordevelopment.pulsar.theme.parser;

public enum TokenType {
    // Single-character tokens
    LeftBrace, RightBrace,
    Dot, Comma, Colon, Equal, At, Bang,

    // Literals
    Number, Hex, String, Identifier,

    // Other
    Error, Eof
}
