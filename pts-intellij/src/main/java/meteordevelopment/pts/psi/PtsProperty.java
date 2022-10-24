package meteordevelopment.pts.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class PtsProperty extends PtsPsiNode {
    public PtsProperty(@NotNull ASTNode node) {
        super(node);
    }

    public PsiElement getNameElement() {
        return getChildFromStart(0);
    }

    public PsiElement getValueElement() {
        return getChildFromEnd(1);
    }
}
