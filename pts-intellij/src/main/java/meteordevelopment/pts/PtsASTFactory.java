package meteordevelopment.pts;

import com.intellij.lang.DefaultASTFactoryImpl;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import meteordevelopment.pts.psi.PtsColor;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

public class PtsASTFactory extends DefaultASTFactoryImpl {
    @Override
    public @NotNull LeafElement createLeaf(@NotNull IElementType type, @NotNull CharSequence text) {
        if (type instanceof TokenIElementType tokenType && tokenType.getANTLRTokenType() == PtsLexer.HEX_COLOR) {
            return new PtsColor(type, text);
        }

        return super.createLeaf(type, text);
    }
}
