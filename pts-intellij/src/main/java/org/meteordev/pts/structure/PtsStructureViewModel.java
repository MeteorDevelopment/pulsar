package org.meteordev.pts.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.meteordev.pts.psi.PtsProperty;
import org.meteordev.pts.psi.PtsStyle;

public class PtsStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
    public PtsStructureViewModel(Editor editor, PsiFile file) {
        super(file, editor, new PtsStructureViewElement(file));

        withSorters(Sorter.ALPHA_SORTER);
        withSuitableClasses(PtsStyle.class, PtsProperty.class);
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return element.getValue() instanceof PtsProperty;
    }
}
