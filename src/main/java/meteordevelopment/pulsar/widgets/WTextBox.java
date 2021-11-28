package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.utils.Color4;
import meteordevelopment.pulsar.utils.ICharFilter;
import meteordevelopment.pulsar.utils.Utils;
import meteordevelopment.pulsar.utils.Vec4;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.pulsar.utils.Utils.combine;
import static org.lwjgl.glfw.GLFW.*;

public class WTextBox extends WContainer {
    protected static final String[] NAMES = combine(WContainer.NAMES, "text-box");

    public Runnable action;
    public Runnable actionOnUnfocused;

    protected String text;
    protected ICharFilter filter;

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

    public WTextBox(String text) {
        this.text = text;
        this.filter = (text1, c) -> true;

        add(new WSelection());
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    protected void onCalculateSize() {
        Vec4 padding = get(Properties.PADDING);
        double s = Renderer.INSTANCE.textHeight(get(Properties.FONT_SIZE));

        width = s + padding.horizontal();
        height = s + padding.vertical();

        calculateTextWidths();
    }

    @Override
    protected boolean onMousePressed(int button, double mouseX, double mouseY, boolean used) {
        if (hovered && !used) {
            if (button == GLFW_MOUSE_BUTTON_RIGHT) {
                if (!text.isEmpty()) {
                    text = "";
                    cursor = 0;
                    selectionStart = 0;
                    selectionEnd = 0;

                    runAction();
                }
            }
            else if (button == GLFW_MOUSE_BUTTON_LEFT) {
                selecting = true;

                double overflowWidth = getOverflowWidthForRender();
                double relativeMouseX = mouseX - x + overflowWidth;
                Vec4 padding = get(Properties.PADDING);

                double smallestDifference = Double.MAX_VALUE;

                cursor = text.length();

                for (int i = 0; i < textWidths.size(); i++) {
                    double difference = Math.abs(textWidths.get(i) + padding.left() - relativeMouseX);

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
            return true;
        }

        if (focused) setFocused(false);

        return false;
    }

    @Override
    protected void onMouseMoved(double mouseX, double mouseY, double deltaMouseX, double deltaMouseY) {
        if (!selecting) return;

        double overflowWidth = getOverflowWidthForRender();
        double relativeMouseX = mouseX - x + overflowWidth;
        Vec4 padding = get(Properties.PADDING);

        double smallestDifference = Double.MAX_VALUE;

        for (int i = 0; i < textWidths.size(); i++) {
            double difference = Math.abs(textWidths.get(i) + padding.left() - relativeMouseX);

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
    protected boolean onMouseReleased(int button, double mouseX, double mouseY) {
        selecting = false;

        if (selectionStart < preSelectionCursor && preSelectionCursor == selectionEnd) {
            cursor = selectionStart;
        }
        else if (selectionEnd > preSelectionCursor && preSelectionCursor == selectionStart) {
            cursor = selectionEnd;
        }

        return false;
    }

    @Override
    protected boolean onKeyPressed(int key, int mods) {
        if (!focused) return false;

        boolean control = Utils.IS_MAC ? mods == GLFW_MOD_SUPER : mods == GLFW_MOD_CONTROL;

        if (control && key == GLFW_KEY_C) {
            if (cursor != selectionStart || cursor != selectionEnd) {
                glfwSetClipboardString(Renderer.INSTANCE.window, text.substring(selectionStart, selectionEnd));
            }
            return true;
        }
        else if (control && key == GLFW_KEY_X) {
            if (cursor != selectionStart || cursor != selectionEnd) {
                glfwSetClipboardString(Renderer.INSTANCE.window, text.substring(selectionStart, selectionEnd));
                clearSelection();
            }

            return true;
        }
        else if (control && key == GLFW_KEY_A) {
            cursor = text.length();
            selectionStart = 0;
            selectionEnd = cursor;
        }
        else if (mods == ((Utils.IS_MAC ? GLFW_MOD_SUPER : GLFW_MOD_CONTROL) | GLFW_MOD_SHIFT) && key == GLFW_KEY_A) {
            resetSelection();
        }
        else if (key == GLFW_KEY_ENTER || key == GLFW_KEY_KP_ENTER) {
            setFocused(false);

            if (actionOnUnfocused != null) actionOnUnfocused.run();
            return true;
        }

        return onKeyRepeated(key, mods);
    }

    @Override
    protected boolean onKeyRepeated(int key, int mods) {
        if (!focused) return false;

        boolean control = Utils.IS_MAC ? mods == GLFW_MOD_SUPER : mods == GLFW_MOD_CONTROL;
        boolean shift = mods == GLFW_MOD_SHIFT;
        boolean controlShift = mods == ((Utils.IS_MAC ? GLFW_MOD_SUPER : GLFW_MOD_ALT) | GLFW_MOD_SHIFT);
        boolean altShift = mods == ((Utils.IS_MAC ? GLFW_MOD_ALT : GLFW_MOD_CONTROL) | GLFW_MOD_SHIFT);

        if (control && key == GLFW_KEY_V) {
            clearSelection();

            String clipboard = glfwGetClipboardString(Renderer.INSTANCE.window);

            if (clipboard != null) {
                String preText = text;
                int addedChars = 0;

                StringBuilder sb = new StringBuilder(text.length() + clipboard.length());
                sb.append(text, 0, cursor);

                for (int i = 0; i < clipboard.length(); i++) {
                    char c = clipboard.charAt(i);
                    if (filter.filter(text, c)) {
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

            return true;
        }
        else if (key == GLFW_KEY_BACKSPACE) {
            if (cursor > 0 && cursor == selectionStart && cursor == selectionEnd) {
                String preText = text;

                int count = mods == (Utils.IS_MAC ? GLFW_MOD_SUPER : GLFW_MOD_ALT)
                        ? cursor
                        : mods == (Utils.IS_MAC ? GLFW_MOD_ALT : GLFW_MOD_CONTROL)
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

            return true;
        }
        else if (key == GLFW_KEY_DELETE) {
            if (cursor < text.length()) {
                if (cursor == selectionStart && cursor == selectionEnd) {
                    String preText = text;

                    int count = mods == (Utils.IS_MAC ? GLFW_MOD_SUPER : GLFW_MOD_ALT)
                            ? text.length() - cursor
                            : mods == (Utils.IS_MAC ? GLFW_MOD_ALT : GLFW_MOD_CONTROL)
                            ? countToNextSpace(false)
                            : 1;

                    text = text.substring(0, cursor) + text.substring(cursor + count);

                    if (!text.equals(preText)) runAction();
                }
                else {
                    clearSelection();
                }
            }

            return true;
        }
        else if (key == GLFW_KEY_LEFT) {
            if (cursor > 0) {
                if (mods == (Utils.IS_MAC ? GLFW_MOD_ALT : GLFW_MOD_CONTROL)) {
                    cursor -= countToNextSpace(true);
                    resetSelection();
                }
                else if (mods == (Utils.IS_MAC ? GLFW_MOD_SUPER : GLFW_MOD_ALT)) {
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
            }
            else if (selectionStart != selectionEnd && selectionStart == 0 && mods == 0) {
                cursor = 0;
                resetSelection();
                cursorChanged();
            }

            return true;
        }
        else if (key == GLFW_KEY_RIGHT) {
            if (cursor < text.length()) {
                if (mods == (Utils.IS_MAC ? GLFW_MOD_ALT : GLFW_MOD_CONTROL)) {
                    cursor += countToNextSpace(false);
                    resetSelection();
                }
                else if (mods == (Utils.IS_MAC ? GLFW_MOD_SUPER : GLFW_MOD_ALT)) {
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
            }
            else if (selectionStart != selectionEnd && selectionEnd == text.length() && mods == 0) {
                cursor = text.length();
                resetSelection();
                cursorChanged();
            }

            return true;
        }

        return false;
    }

    // Rendering

    @Override
    protected void onRender(Renderer renderer, double mouseX, double mouseY, double delta) {
        super.onRender(renderer, mouseX, mouseY, delta);

        if (cursorTimer >= 1) {
            cursorVisible = !cursorVisible;
            cursorTimer = 0;
        }
        else {
            cursorTimer += delta * 1.75;
        }

        Vec4 padding = get(Properties.PADDING);
        double size = get(Properties.FONT_SIZE);
        Color4 color = get(Properties.COLOR);

        double overflowWidth = getOverflowWidthForRender();

        // Text
        if (!text.isEmpty() && color != null) {
            if (overflowWidth > 0) renderer.beginScissor(x + padding.left(), y + padding.bottom(), width - padding.horizontal(), height - padding.vertical());
            renderer.text(x + padding.left() - overflowWidth, y + padding.bottom(), text, size, color);
            if (overflowWidth > 0) renderer.endScissor();
        }

        // Cursor
        animProgress += delta * 10 * (focused && cursorVisible ? 1 : -1);
        animProgress = Utils.clamp(animProgress, 0, 1);

        if (((focused && cursorVisible) || animProgress > 0) && color != null) {
            renderer.alpha(animProgress);
            renderer.quad(x + padding.left() + getTextWidth(cursor) - overflowWidth, y + padding.bottom(), 1, height - padding.vertical(), new Vec4(0), 0, color, null);
            renderer.alpha(1);
        }
    }

    @Override
    public boolean onCharTyped(char c) {
        if (!focused) return false;

        if (filter.filter(text, c)) {
            clearSelection();

            text = text.substring(0, cursor) + c + text.substring(cursor);

            cursor++;
            resetSelection();

            runAction();
            return true;
        }

        return false;
    }

    // Utils

    protected double maxTextWidth() {
        return width - get(Properties.PADDING).horizontal();
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
        double size = get(Properties.FONT_SIZE);

        for (int i = 0; i <= text.length(); i++) {
            textWidths.add(Renderer.INSTANCE.textWidth(text, i, size));
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
        selectionStart = cursor;
        selectionEnd = cursor;

        calculateTextWidths();
        cursorChanged();
    }

    public void setFocused(boolean focused) {
        if (this.focused && !focused && actionOnUnfocused != null) actionOnUnfocused.run();

        boolean wasJustFocused = focused && !this.focused;

        this.focused = focused;

        resetSelection();

        if (wasJustFocused) onCursorChanged();
    }

    public void setCursorMax() {
        cursor = text.length();
    }

    protected class WSelection extends Widget {
        protected static final String[] NAMES = combine(Widget.NAMES, "selection");

        @Override
        public String[] names() {
            return NAMES;
        }

        @Override
        protected void onRender(Renderer renderer, double mouseX, double mouseY, double delta) {
            double overflowWidth = getOverflowWidthForRender();

            if (focused && (cursor != selectionStart || cursor != selectionEnd)) {
                Vec4 padding = WTextBox.this.get(Properties.PADDING);

                double selStart = WTextBox.this.x + padding.left() + getTextWidth(selectionStart) - overflowWidth;
                double selEnd = WTextBox.this.x + padding.left() + getTextWidth(selectionEnd) - overflowWidth;

                x = selStart - 1;
                y = WTextBox.this.y + padding.bottom() - 1;

                width = selEnd - selStart + 2;
                height = WTextBox.this.height - padding.vertical() + 2;

                super.onRender(renderer, mouseX, mouseY, delta);
            }
        }
    }
}
