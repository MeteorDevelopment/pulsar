package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.CharTypedEvent;
import meteordevelopment.pulsar.input.KeyEvent;
import meteordevelopment.pulsar.input.MouseButtonEvent;
import meteordevelopment.pulsar.input.MouseMovedEvent;
import meteordevelopment.pulsar.layout.HorizontalLayout;
import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.theme.Selector;
import meteordevelopment.pulsar.theme.Style;
import meteordevelopment.pulsar.utils.*;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.pulsar.utils.Utils.combine;
import static org.lwjgl.glfw.GLFW.*;

public class WTextBox extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "text-box");

    public Runnable action;
    public Runnable actionOnUnfocused;

    private final String placeholder;
    private final char replacementChar;

    protected String text;
    protected ICharFilter filter = CharFilters.ALL;

    protected final WText textW;
    protected final WSelection selectionW;
    protected WIcon iconW;

    protected boolean focused;
    protected List<Double> textWidths = new ArrayList<>();

    protected int cursor;
    protected double textStart;

    protected boolean selecting;
    protected int selectionStart, selectionEnd;
    private int preSelectionCursor;

    private boolean cursorVisible;
    private double cursorTimer;

    private double animProgress;
    private boolean scissor;

    public WTextBox(String text, String placeholder, char replacementChar) {
        this.text = text;
        this.placeholder = placeholder;
        this.replacementChar = replacementChar;

        String icon = get(Properties.ICON);
        if (icon != null) {
            iconW = add(new WIcon()).widget();
            iconW.tag(icon);

            layout = new HorizontalLayout();
        }

        selectionW = add(new WSelection()).widget();
        textW = add(new WTextBoxText()).expandX().widget();

        calculateTextWidths();
    }

    public WTextBox(String text, String placeholder) {
        this(text, placeholder, '\0');
    }

    public WTextBox(String text) {
        this(text, null, '\0');
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    protected void onMousePressed(MouseButtonEvent event) {
        if (!event.used && isHovered()) {
            if (event.button == GLFW_MOUSE_BUTTON_RIGHT) {
                if (!text.isEmpty()) {
                    text = "";
                    cursor = 0;
                    selectionStart = 0;
                    selectionEnd = 0;

                    runAction();
                }
            }
            else if (event.button == GLFW_MOUSE_BUTTON_LEFT) {
                selecting = true;

                double overflowWidth = getOverflowWidthForRender();
                double relativeMouseX = event.x - textW.x + overflowWidth;

                double smallestDifference = Double.MAX_VALUE;

                cursor = text.length();

                for (int i = 0; i < textWidths.size(); i++) {
                    double difference = Math.abs(textWidths.get(i) - relativeMouseX);

                    if (difference < smallestDifference) {
                        smallestDifference = difference;
                        cursor = i;
                    }
                }

                preSelectionCursor = cursor;
                resetSelection();
                cursorChanged();
            }

            setFocused(true);

            event.use();
            return;
        }

        if (focused) setFocused(false);
    }

    @Override
    protected void onMouseMoved(MouseMovedEvent event) {
        if (!selecting) return;

        double overflowWidth = getOverflowWidthForRender();
        double relativeMouseX = event.x - textW.x + overflowWidth;

        double smallestDifference = Double.MAX_VALUE;

        for (int i = 0; i < textWidths.size(); i++) {
            double difference = Math.abs(textWidths.get(i) - relativeMouseX);

            if (difference < smallestDifference) {
                smallestDifference = difference;
                if (i < preSelectionCursor) {
                    selectionStart = i;
                    cursor = i;
                }
                else if (i > preSelectionCursor) {
                    selectionEnd = i;
                    cursor = i;
                }
                else {
                    cursor = preSelectionCursor;
                    resetSelection();
                }
            }
        }
    }

    @Override
    protected void onMouseReleased(MouseButtonEvent event) {
        selecting = false;

        if (selectionStart < preSelectionCursor && preSelectionCursor == selectionEnd) {
            cursor = selectionStart;
        }
        else if (selectionEnd > preSelectionCursor && preSelectionCursor == selectionStart) {
            cursor = selectionEnd;
        }
    }

    @Override
    protected void onKeyPressed(KeyEvent event) {
        if (!focused || event.used) return;

        boolean control = Utils.IS_MAC ? event.mods == GLFW_MOD_SUPER : event.mods == GLFW_MOD_CONTROL;

        if (control && event.key == GLFW_KEY_C) {
            if (cursor != selectionStart || cursor != selectionEnd) {
                glfwSetClipboardString(Renderer.INSTANCE.window, text.substring(selectionStart, selectionEnd));
            }

            event.use();
        }
        else if (control && event.key == GLFW_KEY_X) {
            if (cursor != selectionStart || cursor != selectionEnd) {
                glfwSetClipboardString(Renderer.INSTANCE.window, text.substring(selectionStart, selectionEnd));
                clearSelection();
            }

            event.use();
        }
        else if (control && event.key == GLFW_KEY_A) {
            cursor = text.length();
            selectionStart = 0;
            selectionEnd = cursor;

            event.use();
        }
        else if (event.mods == ((Utils.IS_MAC ? GLFW_MOD_SUPER : GLFW_MOD_CONTROL) | GLFW_MOD_SHIFT) && event.key == GLFW_KEY_A) {
            resetSelection();
            event.use();
        }
        else if (event.key == GLFW_KEY_ENTER || event.key == GLFW_KEY_KP_ENTER) {
            setFocused(false);

            if (actionOnUnfocused != null) actionOnUnfocused.run();
            event.use();
        }

        onKeyRepeated(event);
    }

    @Override
    protected void onKeyRepeated(KeyEvent event) {
        if (!focused || event.used) return;

        boolean control = Utils.IS_MAC ? event.mods == GLFW_MOD_SUPER :event. mods == GLFW_MOD_CONTROL;
        boolean shift = event.mods == GLFW_MOD_SHIFT;
        boolean controlShift = event.mods == ((Utils.IS_MAC ? GLFW_MOD_SUPER : GLFW_MOD_ALT) | GLFW_MOD_SHIFT);
        boolean altShift = event.mods == ((Utils.IS_MAC ? GLFW_MOD_ALT : GLFW_MOD_CONTROL) | GLFW_MOD_SHIFT);

        if (control && event.key == GLFW_KEY_V) {
            clearSelection();

            String clipboard = glfwGetClipboardString(Renderer.INSTANCE.window);

            if (clipboard != null) {
                String preText = text;
                int addedChars = 0;

                StringBuilder sb = new StringBuilder(text.length() + clipboard.length());
                sb.append(text, 0, cursor);

                for (int i = 0; i < clipboard.length(); i++) {
                    char c = clipboard.charAt(i);
                    if (filter.filter(text, c, cursor)) {
                        sb.append(c);
                        addedChars++;
                    }
                }

                sb.append(text, cursor, text.length());

                text = sb.toString();
                cursor += addedChars;
                resetSelection();

                if (!text.equals(preText)) runAction();
            }

            event.use();
        }
        else if (event.key == GLFW_KEY_BACKSPACE) {
            if (cursor > 0 && selectionStart == selectionEnd) {
                String preText = text;

                int count = event.mods == (Utils.IS_MAC ? GLFW_MOD_SUPER : GLFW_MOD_ALT)
                        ? cursor
                        : event.mods == (Utils.IS_MAC ? GLFW_MOD_ALT : GLFW_MOD_CONTROL)
                        ? countToNextSpace(true)
                        : 1;

                text = text.substring(0, cursor - count) + text.substring(cursor);
                cursor -= count;
                resetSelection();

                if (!text.equals(preText)) runAction();
            }
            else if (cursor != selectionStart || cursor != selectionEnd) {
                clearSelection();
            }

            event.use();
        }
        else if (event.key == GLFW_KEY_DELETE) {
            if (cursor < text.length()) {
                if (selectionStart == selectionEnd) {
                    String preText = text;

                    int count = event.mods == (Utils.IS_MAC ? GLFW_MOD_SUPER : GLFW_MOD_ALT)
                            ? text.length() - cursor
                            : event.mods == (Utils.IS_MAC ? GLFW_MOD_ALT : GLFW_MOD_CONTROL)
                            ? countToNextSpace(false)
                            : 1;

                    text = text.substring(0, cursor) + text.substring(cursor + count);

                    if (!text.equals(preText)) runAction();
                }
                else {
                    clearSelection();
                }
            }

            event.use();
        }
        else if (event.key == GLFW_KEY_LEFT) {
            if (cursor > 0) {
                if (event.mods == (Utils.IS_MAC ? GLFW_MOD_ALT : GLFW_MOD_CONTROL)) {
                    cursor -= countToNextSpace(true);
                    resetSelection();
                }
                else if (event.mods == (Utils.IS_MAC ? GLFW_MOD_SUPER : GLFW_MOD_ALT)) {
                    cursor = 0;
                    resetSelection();
                }
                else if (altShift) {
                    if (cursor == selectionEnd && cursor != selectionStart) {
                        cursor -= countToNextSpace(true);
                        selectionEnd = cursor;
                    }
                    else {
                        cursor -= countToNextSpace(true);
                        selectionStart = cursor;
                    }
                }
                else if (controlShift) {
                    if (cursor == selectionEnd && cursor != selectionStart) {
                        selectionEnd = selectionStart;
                    }
                    selectionStart = 0;

                    cursor = 0;
                }
                else if (shift) {
                    if (cursor == selectionEnd && cursor != selectionStart) {
                        selectionEnd = cursor - 1;
                    }
                    else {
                        selectionStart = cursor - 1;
                    }

                    cursor--;
                }
                else {
                    if (cursor == selectionEnd && cursor != selectionStart) {
                        cursor = selectionStart;
                    }
                    else {
                        cursor--;
                    }

                    resetSelection();
                }

                cursorChanged();
                event.use();
            }
            else if (selectionStart != selectionEnd && selectionStart == 0 && event.mods == 0) {
                cursor = 0;
                resetSelection();
                cursorChanged();

                event.use();
            }
        }
        else if (event.key == GLFW_KEY_RIGHT) {
            if (cursor < text.length()) {
                if (event.mods == (Utils.IS_MAC ? GLFW_MOD_ALT : GLFW_MOD_CONTROL)) {
                    cursor += countToNextSpace(false);
                    resetSelection();
                }
                else if (event.mods == (Utils.IS_MAC ? GLFW_MOD_SUPER : GLFW_MOD_ALT)) {
                    cursor = text.length();
                    resetSelection();
                }
                else if (altShift) {
                    if (cursor == selectionStart && cursor != selectionEnd) {
                        cursor += countToNextSpace(false);
                        selectionStart = cursor;
                    }
                    else {
                        cursor += countToNextSpace(false);
                        selectionEnd = cursor;
                    }
                }
                else if (controlShift) {
                    if (cursor == selectionStart && cursor != selectionEnd) {
                        selectionStart = selectionEnd;
                    }
                    cursor = text.length();
                    selectionEnd = cursor;
                }
                else if (shift) {
                    if (cursor == selectionStart && cursor != selectionEnd) {
                        selectionStart = cursor + 1;
                    }
                    else {
                        selectionEnd = cursor + 1;
                    }

                    cursor++;
                }
                else {
                    if (cursor == selectionStart && cursor != selectionEnd) {
                        cursor = selectionEnd;
                    }
                    else {
                        cursor++;
                    }

                    resetSelection();
                }

                cursorChanged();
                event.use();
            }
            else if (selectionStart != selectionEnd && selectionEnd == text.length() && event.mods == 0) {
                cursor = text.length();
                resetSelection();
                cursorChanged();

                event.use();
            }
        }
    }

    @Override
    protected void onCharTyped(CharTypedEvent event) {
        if (!focused || event.used) return;

        if (filter.filter(text, event.c, cursor)) {
            clearSelection();

            text = text.substring(0, cursor) + event.c + text.substring(cursor);

            cursor++;
            resetSelection();

            runAction();
            event.use();
        }
    }

    // Rendering

    @Override
    public void render(Renderer renderer, double delta) {
        if (iconW != null) iconW.render(renderer, delta);

        onRender(renderer, delta);
        textW.render(renderer, delta);
        selectionW.render(renderer, delta);

        if (scissor) renderer.endScissor();
    }

    @Override
    protected void onRender(Renderer renderer, double delta) {
        super.onRender(renderer, delta);

        if (cursorTimer >= 1) {
            cursorVisible = !cursorVisible;
            cursorTimer = 0;
        }
        else {
            cursorTimer += delta * 1.75;
        }

        Vec4 padding = get(Properties.PADDING);
        Color4 color = get(Properties.COLOR);

        double overflowWidth = getOverflowWidthForRender();

        scissor = textWidths.get(textWidths.size() - 1) > maxTextWidth();
        if (scissor) renderer.beginScissor(textW.x - 1, y + padding.bottom() - 1, maxTextWidth() + 2, height - padding.vertical() + 2);

        // Cursor
        animProgress += delta * 10 * (focused && cursorVisible ? 1 : -1);
        animProgress = Utils.clamp(animProgress, 0, 1);

        if (((focused && cursorVisible) || animProgress > 0) && color != null) {
            renderer.alpha(animProgress);
            renderer.quad(textW.x + getTextWidth(cursor) - overflowWidth, y + padding.bottom(), 1, height - padding.vertical(), new Vec4(0), 0, color, null);
            renderer.alpha(1);
        }
    }

    // Utils

    protected double maxTextWidth() {
        return textW.width;
    }

    private void clearSelection() {
        if (selectionStart == selectionEnd) return;

        String preText = text;

        text = text.substring(0, selectionStart) + text.substring(selectionEnd);

        cursor = selectionStart;
        selectionEnd = cursor;

        if (!text.equals(preText)) runAction();
    }

    private void resetSelection() {
        selectionStart = cursor;
        selectionEnd = cursor;
    }

    private int countToNextSpace(boolean toLeft) {
        int count = 0;
        boolean hadNonSpace = false;

        for (int i = cursor; toLeft ? i >= 0 : i < text.length(); i += toLeft ? -1 : 1) {
            int j = i;
            if (toLeft) j--;

            if (j >= text.length()) continue;
            if (j < 0) break;

            if (hadNonSpace && Character.isWhitespace(text.charAt(j))) break;
            else if (!Character.isWhitespace(text.charAt(j))) hadNonSpace = true;

            count++;
        }

        return count;
    }

    private void calculateTextWidths() {
        textWidths.clear();
        double size = textW.get(Properties.FONT_SIZE);

        for (int i = 0; i <= text.length(); i++) {
            if (replacementChar != '\0') textWidths.add(Renderer.INSTANCE.charWidth(replacementChar, size) * i);
            else textWidths.add(Renderer.INSTANCE.textWidth(text, i, size));
        }
    }

    private void runAction() {
        calculateTextWidths();
        cursorChanged();

        if (action != null) action.run();
    }

    private double textWidth() {
        return textWidths.isEmpty() ? 0 : textWidths.get(textWidths.size() - 1);
    }

    private void cursorChanged() {
        double cursor = getCursorTextWidth(-2);
        if (cursor < textStart) {
            textStart -= textStart - cursor;
        }

        cursor = getCursorTextWidth(2);
        if (cursor > textStart + maxTextWidth()) {
            textStart += cursor - (textStart + maxTextWidth());
        }

        textStart = Utils.clamp(textStart, 0, Math.max(textWidth() - maxTextWidth(), 0));

        onCursorChanged();
    }

    protected void onCursorChanged() {
        cursorVisible = true;
        cursorTimer = 0;
    }

    protected double getTextWidth(int pos) {
        if (textWidths.isEmpty()) return 0;

        if (pos < 0) pos = 0;
        else if (pos >= textWidths.size()) pos = textWidths.size() - 1;

        return textWidths.get(pos);
    }

    protected double getCursorTextWidth(int offset) {
        return getTextWidth(cursor + offset);
    }

    protected double getOverflowWidthForRender() {
        return textStart;
    }

    public String get() {
        return text;
    }

    public void set(String text) {
        this.text = text;

        cursor = Utils.clamp(cursor, 0, text.length());
        resetSelection();

        calculateTextWidths();
        cursorChanged();
    }

    public void setFocused(boolean focused) {
        if (this.focused && !focused && actionOnUnfocused != null) actionOnUnfocused.run();

        boolean wasJustFocused = focused && !this.focused;

        this.focused = focused;

        cursor = Utils.clamp(cursor, 0, text.length());
        resetSelection();

        if (wasJustFocused) onCursorChanged();
    }

    public boolean isFocused() {
        return focused;
    }

    public void setCursorMax() {
        cursor = text.length();
    }

    protected class WTextBoxText extends WText {
        protected static final String[] NAMES = combine(WText.NAMES, "text-box-text");
        protected static final String[] PLACEHOLDER_NAMES = combine(WText.NAMES, "text-box-placeholder");

        private Color4 placeholderColor;
        private boolean isPlaceholder;

        public WTextBoxText() {
            super(null);

            Style placeholderStyle = Renderer.INSTANCE.theme.computeStyle(new Selector(PLACEHOLDER_NAMES, null, false, false));
            if (placeholderStyle != null) placeholderColor = placeholderStyle.get(Properties.COLOR);
        }

        @Override
        public String[] names() {
            return NAMES;
        }

        @Override
        protected void renderText(Renderer renderer, double x, double y, String text) {
            isPlaceholder = false;

            if (text.isEmpty() && placeholder != null && placeholderColor != null) {
                isPlaceholder = true;
                super.renderText(renderer, x, y, placeholder);
            }
            else super.renderText(renderer, x, y, text);
        }

        @Override
        protected void renderTextComponent(Renderer renderer, double x, double y, String text, double size, Color4 color) {
            if (replacementChar != '\0' && !isPlaceholder) renderer.chars(x, y, replacementChar, text.length(), size, color);
            else super.renderTextComponent(renderer, x, y, text, size, isPlaceholder ? placeholderColor : color);
        }

        @Override
        protected double getOffsetX() {
            return -WTextBox.this.getOverflowWidthForRender();
        }

        @Override
        public String getText() {
            return WTextBox.this.text;
        }
    }

    protected class WSelection extends Widget {
        protected static final String[] NAMES = combine(Widget.NAMES, "selection");

        @Override
        public String[] names() {
            return NAMES;
        }

        @Override
        public boolean shouldSkipLayout() {
            return true;
        }

        @Override
        protected void onRender(Renderer renderer, double delta) {
            double overflowWidth = getOverflowWidthForRender();

            if (focused && selectionStart != selectionEnd) {
                Vec4 padding = WTextBox.this.get(Properties.PADDING);

                double selStart = WTextBox.this.textW.x + getTextWidth(selectionStart) - overflowWidth;
                double selEnd = WTextBox.this.textW.x + getTextWidth(selectionEnd) - overflowWidth;

                x = selStart - 1;
                y = WTextBox.this.y + padding.bottom() - 1;

                width = selEnd - selStart + 2;
                height = WTextBox.this.height - padding.vertical() + 2;

                super.onRender(renderer, delta);
            }
        }
    }
}
