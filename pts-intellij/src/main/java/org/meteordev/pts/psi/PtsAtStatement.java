package org.meteordev.pts.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class PtsAtStatement extends PtsPsiNode {
    public final boolean isApply;

    public PtsAtStatement(@NotNull ASTNode node, boolean isApply) {
        super(node);

        this.isApply = isApply;
    }

    public PsiElement getKeywordElement() {
        return isApply ? getChildFromStart(0) : getChildFromStart(0).getFirstChild();
    }
}
