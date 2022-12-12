package org.meteordev.pts.structure;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PlatformIcons;
import org.meteordev.pts.psi.PtsPSIFileRoot;
import org.meteordev.pts.psi.PtsProperty;
import org.meteordev.pts.psi.PtsStyle;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PtsStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
    private final NavigatablePsiElement element;

    public PtsStructureViewElement(NavigatablePsiElement element) {
        this.element = element;
    }

    @Override
    public Object getValue() {
        return element;
    }

    @Override
    public @NotNull String getAlphaSortKey() {
        String name = element.getName();
        return name != null ? name : "";
    }

    @Override
    public @NotNull ItemPresentation getPresentation() {
        if (element instanceof PtsStyle) return new PresentationData(element.getName(), null, PlatformIcons.PROPERTIES_ICON, null);
        if (element instanceof PtsProperty) return new PresentationData(element.getName(), null, PlatformIcons.PROPERTY_ICON, null);

        ItemPresentation presentation = element.getPresentation();
        return presentation != null ? presentation : new PresentationData();
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        if (element instanceof PtsPSIFileRoot) {
            Collection<PtsStyle> styles = PsiTreeUtil.findChildrenOfType(element, PtsStyle.class);
            TreeElement[] elements = new TreeElement[styles.size()];

            int i = 0;
            for (PtsStyle style : styles) {
                elements[i++] = new PtsStructureViewElement(style);
            }

            return elements;
        }
        else if (element instanceof PtsStyle) {
            Collection<PtsProperty> properties = PsiTreeUtil.findChildrenOfType(element, PtsProperty.class);
            TreeElement[] elements = new TreeElement[properties.size()];

            int i = 0;
            for (PtsProperty property : properties) {
                elements[i++] = new PtsStructureViewElement(property);
            }

            return elements;
        }

        return EMPTY_ARRAY;
    }

    @Override
    public void navigate(boolean requestFocus) {
        element.navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
        return element.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return element.canNavigateToSource();
    }
}
