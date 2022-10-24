package meteordevelopment.pts;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import meteordevelopment.pts.psi.PtsStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PtsFoldingBuilder extends FoldingBuilderEx {
    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();

        for (PsiElement element : PsiTreeUtil.findChildrenOfType(root, PtsStyle.class)) {
            descriptors.add(new FoldingDescriptor(
                    element,
                    new TextRange(
                            element.getTextRange().getStartOffset(),
                            element.getTextRange().getEndOffset()
                    )
            ));
        }

        return descriptors.toArray(new FoldingDescriptor[0]);
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }

    @Override
    public @Nullable String getPlaceholderText(@NotNull ASTNode node) {
        return ((PtsStyle) node.getPsi()).getSelector();
    }
}
