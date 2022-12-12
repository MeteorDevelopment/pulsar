package org.meteordev.pts;

import com.intellij.psi.PsiElement;
import org.meteordev.pts.psi.PtsColor;
import org.meteordev.pts.psi.PtsFunction;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;

import java.awt.*;

public class PtsElementColorProvider {
    public static PtsElementColorProvider INSTANCE = new PtsElementColorProvider();

    @SuppressWarnings("UseJBColor")
    public Color getColorFrom(PsiElement element) {
        if (element instanceof PtsColor color) {
            // Hex color
            return color.getColor();
        }
        else if (element.getNode().getElementType() instanceof TokenIElementType type && type.getANTLRTokenType() == PtsLexer.IDENTIFIER) {
            // Rgb / rgba color
            if (element.getParent() instanceof PtsFunction function) {
                String name = function.getNameElement().getText();

                if (name.equals("rgb") || name.equals("rgba")) {
                    int r = 255;
                    int g = 255;
                    int b = 255;
                    int a = 255;

                    int i = 0;
                    for (PsiElement arg : function.argumentIterator()) {
                        int number = (int) Double.parseDouble(arg.getText());

                        switch (i++) {
                            case 0 -> r = number;
                            case 1 -> g = number;
                            case 2 -> b = number;
                            case 3 -> a = number;
                        }
                    }

                    return new Color(r, g, b, a);
                }
            }
        }

        return null;
    }

    public PsiElement setColorTo(PsiElement element, Color color) {
        if (element instanceof PtsColor) {
            // Hex color
            return element.replace(PtsElementFactory.createColor(element.getProject(), color));
        }
        else {
            // Rgb / rgba color
            boolean rgba = color.getAlpha() < 255;
            String[] arguments = new String[rgba ? 4 : 3];

            arguments[0] = Integer.toString(color.getRed());
            arguments[1] = Integer.toString(color.getGreen());
            arguments[2] = Integer.toString(color.getBlue());
            if (rgba) arguments[3] = Integer.toString(color.getAlpha());

            PtsFunction el = PtsElementFactory.createFunction(element.getProject(), rgba ? "rgba" : "rgb", arguments);
            return element.getParent().replace(el).getFirstChild();
        }
    }
}
