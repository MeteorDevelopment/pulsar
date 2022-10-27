package meteordevelopment.pts.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import meteordevelopment.pts.PtsLexer;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class PtsStyle extends PtsPsiNode {
    public PtsStyle(@NotNull ASTNode node) {
        super(node);
    }

    public void forEachChildInSelector(Consumer<PsiElement> callback) {
        PsiElement child = getFirstChild();

        while (child != null) {
            IElementType type = child.getNode().getElementType();
            if (type instanceof TokenIElementType tokenType && tokenType.getANTLRTokenType() == PtsLexer.OPENING_BRACE) break;

            if (!(child instanceof PsiWhiteSpace) && !(child instanceof PsiComment)) callback.accept(child);

            child = child.getNextSibling();
        }
    }

    public String getSelector() {
        StringBuilder sb = new StringBuilder();

        forEachChildInSelector(child -> sb.append(child.getText()));

        return sb.toString().trim();
    }
}
