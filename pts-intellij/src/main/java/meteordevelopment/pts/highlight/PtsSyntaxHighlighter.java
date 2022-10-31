package meteordevelopment.pts.highlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.XmlHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import meteordevelopment.pts.PtsLanguage;
import meteordevelopment.pts.PtsLexer;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class PtsSyntaxHighlighter extends SyntaxHighlighterBase {
    private static final TextAttributesKey[] EMPTY = new TextAttributesKey[0];

    public static final TextAttributesKey NUMBER = createTextAttributesKey("PTS_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey COLOR = createTextAttributesKey("PTS_COLOR", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey STRING = createTextAttributesKey("PTS_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey IDENTIFIER = createTextAttributesKey("PTS_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);

    public static final TextAttributesKey BRACES = createTextAttributesKey("PTS_BRACES", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey BRACKETS = createTextAttributesKey("PTS_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey COMMA = createTextAttributesKey("PTS_COMMA", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey SEMICOLON = createTextAttributesKey("PTS_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey VARIABLE = createTextAttributesKey("PTS_VARIABLE", XmlHighlighterColors.HTML_TAG_NAME);

    public static final TextAttributesKey LINE_COMMENT = createTextAttributesKey("PTS_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BLOCK_COMMENT = createTextAttributesKey("PTS_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

    public static final TextAttributesKey UNKNOWN = createTextAttributesKey("PTS_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        PtsLexer lexer = new PtsLexer(null);
        return new ANTLRLexerAdaptor(PtsLanguage.INSTANCE, lexer);
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (!(tokenType instanceof TokenIElementType myType)) return EMPTY;

        int type = myType.getANTLRTokenType();
        TextAttributesKey key;

        switch (type) {
            case PtsLexer.NUMBER, PtsLexer.PX -> key = NUMBER;
            case PtsLexer.HEX_COLOR    -> key = COLOR;
            case PtsLexer.STRING       -> key = STRING;
            case PtsLexer.IDENTIFIER   -> key = IDENTIFIER;

            case PtsLexer.OPENING_BRACE, PtsLexer.CLOSING_BRACE -> key = BRACES;
            case PtsLexer.OPENING_BRACKET, PtsLexer.CLOSING_BRACKET -> key = BRACKETS;
            case PtsLexer.COMMA        -> key = COMMA;
            case PtsLexer.SEMICOLON    -> key = SEMICOLON;
            case PtsLexer.BANG         -> key = VARIABLE;

            case PtsLexer.COMMENT      -> key = BLOCK_COMMENT;
            case PtsLexer.LINE_COMMENT -> key = LINE_COMMENT;

            case PtsLexer.UNKNOWN      -> key = UNKNOWN;

            default -> { return EMPTY; }
        }

        return new TextAttributesKey[] { key };
    }
}
