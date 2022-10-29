package meteordevelopment.pts;

import com.intellij.codeInsight.highlighting.PairedBraceMatcherAdapter;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PtsBraceMatcher extends PairedBraceMatcherAdapter {
    public PtsBraceMatcher() {
        super(
                new PairedBraceMatcher() {
                    @Override
                    public BracePair @NotNull [] getPairs() {
                        List<TokenIElementType> types = PSIElementTypeFactory.getTokenIElementTypes(PtsLanguage.INSTANCE);

                        return new BracePair[] {
                                new BracePair(types.get(PtsLexer.OPENING_PAREN), types.get(PtsLexer.CLOSING_PAREN), false),
                                new BracePair(types.get(PtsLexer.OPENING_BRACE), types.get(PtsLexer.CLOSING_BRACE), false),
                                new BracePair(types.get(PtsLexer.OPENING_BRACKET), types.get(PtsLexer.CLOSING_BRACKET), false)
                        };
                    }

                    @Override
                    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
                        return true;
                    }

                    @Override
                    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
                        return openingBraceOffset;
                    }
                },
                PtsLanguage.INSTANCE
        );
    }
}
