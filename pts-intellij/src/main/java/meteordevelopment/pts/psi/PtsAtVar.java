package meteordevelopment.pts.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class PtsAtVar extends PtsPsiNode {
    public PtsAtVar(@NotNull ASTNode node) {
        super(node);
    }

    public PsiElement getNameElement() {
        return getChildFromStart(1);
    }

    public PsiElement getTypeElement() {
        return getChildFromStart(3);
    }

    public PsiElement getValueElement() {
        return getChildFromEnd(1);
    }
}
