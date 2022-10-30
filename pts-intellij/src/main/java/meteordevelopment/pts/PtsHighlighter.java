package meteordevelopment.pts;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.XmlHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import meteordevelopment.pts.psi.*;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class PtsHighlighter implements HighlightVisitor {
    public static final TextAttributesKey KEYWORD = createTextAttributesKey("PTS_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);

    public static final TextAttributesKey WIDGET_NAME = createTextAttributesKey("PTS_WIDGET_NAME", XmlHighlighterColors.HTML_TAG_NAME);
    public static final TextAttributesKey TAG_NAME = createTextAttributesKey("PTS_TAG_NAME", XmlHighlighterColors.HTML_TAG_NAME);
    public static final TextAttributesKey STATE_NAME = createTextAttributesKey("PTS_STATE_NAME", XmlHighlighterColors.HTML_TAG_NAME);

    public static final TextAttributesKey PROPERTY_NAME = createTextAttributesKey("PTS_PROPERTY_NAME", XmlHighlighterColors.HTML_ATTRIBUTE_NAME);
    public static final TextAttributesKey PROPERTY_VALUE = createTextAttributesKey("PTS_PROPERTY_VALUE", XmlHighlighterColors.HTML_ATTRIBUTE_VALUE);

    public static final TextAttributesKey FUNCTION = createTextAttributesKey("PTS_FUNCTION", XmlHighlighterColors.HTML_TAG_NAME);

    private HighlightInfoHolder holder;

    @Override
    public boolean suitableForFile(@NotNull PsiFile file) {
        return file instanceof PtsPSIFileRoot;
    }

    @Override
    public void visit(@NotNull PsiElement element) {
        // At statements
        if (element instanceof PtsAtStatement at) {
            apply(at.getKeywordElement(), KEYWORD);

            if (at.isApply) apply(at.getLastChild().getPrevSibling(), PROPERTY_VALUE);
        }
        // @var
        else if (element instanceof PtsAtVar var) {
            apply(var.getNameElement(), PROPERTY_NAME);
            apply(var.getTypeElement(), PROPERTY_VALUE);

            visitValueElement(var.getValueElement());
        }
        // Styles
        else if (element instanceof PtsStyle style) {
            style.forEachChildInSelector(child -> {
                char first = child.getText().charAt(0);

                if (first == '.') apply(child, 1, TAG_NAME);
                else if (first == ':') apply(child, 1, STATE_NAME);
                else apply(child, WIDGET_NAME);
            }, false);
        }
        // Properties
        else if (element instanceof PtsProperty property) {
            apply(property.getNameElement(), PROPERTY_NAME);
            visitValueElement(property.getValueElement());
        }
        // Functions
        else if (element instanceof PtsFunction function) {
            apply(function.getNameElement(), FUNCTION);
        }
    }

    private void visitValueElement(PsiElement element) {
        if (element == null) return;

        PsiElement el = element.getFirstChild();
        if (el == null) return;

        IElementType type = el.getNode().getElementType();

        if (type instanceof RuleIElementType rule) {
            int index = rule.getRuleIndex();

            if (index == PtsParser.RULE_identifier) apply(el, PROPERTY_VALUE);
            else if (index == PtsParser.RULE_variable) apply(el, 1, PROPERTY_VALUE);
        }
    }

    @Override
    public boolean analyze(@NotNull PsiFile file, boolean updateWholeFile, @NotNull HighlightInfoHolder holder, @NotNull Runnable action) {
        this.holder = holder;
        action.run();
        this.holder = null;

        return true;
    }

    private void apply(PsiElement element, int start, int end, TextAttributesKey key) {
        if (element == null) return;

        int offset = element.getTextRange().getStartOffset();
        holder.add(
                HighlightInfo
                        .newHighlightInfo(HighlightInfoType.INFORMATION)
                        .range(offset + start, end == -1 ? element.getTextRange().getEndOffset() : offset + end)
                        .textAttributes(key)
                        .create()
        );
    }
    private void apply(PsiElement element, int offset, TextAttributesKey key) {
        apply(element, offset, -1, key);
    }
    private void apply(PsiElement element, TextAttributesKey key) {
        apply(element, 0, -1, key);
    }

    @Override
    public @NotNull HighlightVisitor clone() {
        return new PtsHighlighter();
    }
}
