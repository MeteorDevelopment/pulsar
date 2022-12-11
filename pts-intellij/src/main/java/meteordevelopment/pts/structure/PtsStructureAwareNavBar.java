package meteordevelopment.pts.structure;

import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension;
import com.intellij.lang.Language;
import com.intellij.util.PlatformIcons;
import meteordevelopment.pts.PtsLanguage;
import meteordevelopment.pts.psi.PtsProperty;
import meteordevelopment.pts.psi.PtsPsiNode;
import meteordevelopment.pts.psi.PtsStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PtsStructureAwareNavBar extends StructureAwareNavBarModelExtension {
    @NotNull
    @Override
    protected Language getLanguage() {
        return PtsLanguage.INSTANCE;
    }

    @Override
    public @Nullable String getPresentableText(Object object) {
        if (object instanceof PtsPsiNode node) return node.getName();

        return null;
    }

    @Override
    public @Nullable Icon getIcon(Object object) {
        if (object instanceof PtsStyle) return PlatformIcons.PROPERTIES_ICON;
        if (object instanceof PtsProperty) return PlatformIcons.PROPERTY_ICON;

        return null;
    }
}
