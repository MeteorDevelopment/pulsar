package meteordevelopment.pts;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;

public class PtsQuoteHandler extends SimpleTokenSetQuoteHandler {
    public static final TokenSet QUOTE = PSIElementTypeFactory.createTokenSet(PtsLanguage.INSTANCE, PtsLexer.QUOTE);

    public PtsQuoteHandler() {
        super(QUOTE);
    }
}
