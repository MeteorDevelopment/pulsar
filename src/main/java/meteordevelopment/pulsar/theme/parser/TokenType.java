package meteordevelopment.pulsar.theme.parser;

public enum TokenType {
    // Single-character tokens
    LeftBrace, RightBrace,
    Dot, Colon,

    // Literals
    Number, Hex, String, Identifier,

    // Other
    Error, Eof
}
