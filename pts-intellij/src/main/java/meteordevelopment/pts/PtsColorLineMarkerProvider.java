package meteordevelopment.pts;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.daemon.*;
import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeBundle;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiEditorUtil;
import com.intellij.ui.ColorChooser;
import com.intellij.ui.ColorPicker;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.Function;
import com.intellij.util.FunctionUtil;
import com.intellij.util.ui.ColorIcon;
import com.intellij.util.ui.ColorsIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class PtsColorLineMarkerProvider extends LineMarkerProviderDescriptor {
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<? extends PsiElement> elements, @NotNull Collection<? super LineMarkerInfo<?>> result) {
        for (PsiElement element : elements) {
            Color color = PtsElementColorProvider.INSTANCE.getColorFrom(element);
            if (color == null) continue;

            MyInfo info = new MyInfo(element, color);
            NavigateAction.setNavigateAction(info, IdeBundle.message("dialog.title.choose.color"), null, AllIcons.Actions.Colors);
            result.add(info);
        }
    }

    @Override
    public @Nullable("null means disabled") @GutterName String getName() {
        return CodeInsightBundle.message("gutter.color.preview");
    }

    @Override
    public @Nullable Icon getIcon() {
        return AllIcons.Gutter.Colors;
    }

    private static class MyInfo extends MergeableLineMarkerInfo<PsiElement> {
        private static final Field handlerField;

        private static final Method createElementRefMethod;
        private static final Field elementRefField;

        static {
            try {
                handlerField = LineMarkerInfo.class.getDeclaredField("myNavigationHandler");
                handlerField.setAccessible(true);

                createElementRefMethod = LineMarkerInfo.class.getDeclaredMethod("createElementRef", PsiElement.class, TextRange.class);
                createElementRefMethod.setAccessible(true);

                elementRefField = LineMarkerInfo.class.getDeclaredField("elementRef");
                elementRefField.setAccessible(true);
            }
            catch (NoSuchFieldException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        private final Color color;

        MyInfo(PsiElement element, Color color) {
            super(
                    element,
                    element.getTextRange(),
                    JBUIScale.scaleIcon(new ColorIcon(12, color)),
                    FunctionUtil.<Object, String>nullConstant(),
                    null,
                    GutterIconRenderer.Alignment.LEFT,
                    () -> "IDK"
            );

            try {
                GutterIconNavigationHandler<PsiElement> handler = this::handler;;
                handlerField.set(this, handler);
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            this.color = color;
        }

        private void handler(MouseEvent e, PsiElement elt) {
            if (!elt.isWritable()) return;

            final Editor editor = PsiEditorUtil.findEditor(elt);
            assert editor != null;

            if (Registry.is("ide.new.color.picker")) {
                RelativePoint relativePoint = new RelativePoint(e.getComponent(), e.getPoint());
                ColorPicker.showColorPickerPopup(
                        elt.getProject(),
                        color,
                        (c, l) -> WriteAction.run(() -> setElement(PtsElementColorProvider.INSTANCE.setColorTo(getElement(), c))),
                        relativePoint,
                        true
                );
            }
            else {
                Color c = ColorChooser.chooseColor(editor.getProject(), editor.getComponent(), IdeBundle.message("dialog.title.choose.color"), color, true);
                if (c != null) WriteAction.run(() -> setElement(PtsElementColorProvider.INSTANCE.setColorTo(getElement(), c)));
            }
        }

        private void setElement(PsiElement element) {
            if (element == null) return;

            try {
                elementRefField.set(this, createElementRefMethod.invoke(null, element, element.getTextRange()));
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean canMergeWith(@NotNull MergeableLineMarkerInfo<?> info) {
            return info instanceof MyInfo;
        }

        @Override
        public Icon getCommonIcon(@NotNull List<? extends MergeableLineMarkerInfo<?>> infos) {
            return JBUIScale.scaleIcon(new ColorsIcon(12, infos.stream().map(_info -> ((MyInfo)_info).color).toArray(Color[]::new)));
        }

        @NotNull
        @Override
        public Function<? super PsiElement, String> getCommonTooltip(@NotNull List<? extends MergeableLineMarkerInfo<?>> infos) {
            return FunctionUtil.nullConstant();
        }
    }
}
