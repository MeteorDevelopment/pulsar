package meteordevelopment.pts;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import meteordevelopment.pts.psi.PtsColor;
import meteordevelopment.pts.psi.PtsFunction;
import meteordevelopment.pts.psi.PtsProperty;
import meteordevelopment.pts.psi.PtsStyle;

import java.awt.*;

public class PtsElementFactory {
    private static PsiFile createFile(Project project, String text) {
        return PsiFileFactory.getInstance(project).createFileFromText("dummy.pts", PtsLanguage.INSTANCE, text);
    }

    public static PtsColor createColor(Project project, Color color) {
        StringBuilder sb = new StringBuilder("foo { bar: #");

        sb.append(String.format("%02X", color.getRed()));
        sb.append(String.format("%02X", color.getGreen()));
        sb.append(String.format("%02X", color.getBlue()));
        if (color.getAlpha() < 255) sb.append(String.format("%02X", color.getAlpha()));

        sb.append("; }");
        PsiFile file = createFile(project, sb.toString());

        return (PtsColor) ((PtsProperty) ((PtsStyle) file.getFirstChild().getFirstChild().getFirstChild()).getChildFromEnd(1).getFirstChild()).getValueElement().getFirstChild().getFirstChild();
    }

    public static PtsFunction createFunction(Project project, String name, String[] arguments) {
        StringBuilder sb = new StringBuilder("foo { bar: ");
        sb.append(name);
        sb.append('(');

        for (int i = 0; i < arguments.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(arguments[i]);
        }

        sb.append("); }");
        PsiFile file = createFile(project, sb.toString());

        return (PtsFunction) ((PtsProperty) ((PtsStyle) file.getFirstChild().getFirstChild().getFirstChild()).getChildFromEnd(1).getFirstChild()).getValueElement().getFirstChild();
    }
}
