package meteordevelopment.pts.psi;

import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PtsColor extends LeafPsiElement {
    public PtsColor(@NotNull IElementType type, @NotNull CharSequence text) {
        super(type, text);
    }

    @SuppressWarnings("UseJBColor")
    public Color getColor() {
        String text = getText();

        return new Color(
                Integer.parseInt(text.substring(1, 3), 16),
                Integer.parseInt(text.substring(3, 5), 16),
                Integer.parseInt(text.substring(5, 7), 16),
                text.length() > 7 ? Integer.parseInt(text.substring(7, 9), 16) : 255
        );
    }
}
