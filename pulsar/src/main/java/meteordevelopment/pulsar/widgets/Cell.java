package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.theme.properties.Properties;
import meteordevelopment.pulsar.utils.AlignX;
import meteordevelopment.pulsar.utils.AlignY;

/** Wrapper for a widget that is placed inside another widget. */
public class Cell<T extends Widget> {
    private final T widget;

    public int x, y;
    public int width, height;

    public boolean expandCellX;
    private boolean expandWidgetX;

    public Cell(T widget) {
        this.widget = widget;

        if (widget instanceof WHorizontalSeparator) expandX();
    }

    /** @return the widget this cell wraps. */
    public T widget() {
        return widget;
    }

    /** Makes it so this cell will try to take all available width. If multiple cells in the same row try to do so then the available width will be equally split. */
    public Cell<T> expandCellX() {
        expandCellX = true;
        return this;
    }

    /** Same as {@link #expandCellX()} but also expands the widget to the same width as this cell. */
    public Cell<T> expandX() {
        expandCellX = expandWidgetX = true;
        return this;
    }

    /** Adds or removes specified tag based on if this widget already contains the tag. */
    public Cell<T> tag(String tag) {
        widget.tag(tag);
        return this;
    }

    /** Adds or removes specified tag based on if this widget already contains the tag. */
    public Cell<T> tag(String tag, boolean shouldHave) {
        widget.tag(tag, shouldHave);
        return this;
    }

    /** Aligns the widget to the bounds of this cell. */
    public void align() {
        if (expandWidgetX) widget.width = width;

        switch (widget.get(Properties.ALIGN_X)) {
            case Left -> widget.x = x;
            case Center -> widget.x = x + width / 2 - widget.width / 2;
            case Right -> widget.x = x + width - widget.width;
        }

        switch (widget.get(Properties.ALIGN_Y)) {
            case Bottom -> widget.y = y;
            case Center -> widget.y = y + height / 2 - widget.height / 2;
            case Top -> widget.y = y + height - widget.height;
        }
    }

    // Alignment

    /** Sets {@link Properties#ALIGN_X} to {@link AlignX#Left} for this widget. */
    public Cell<T> left() {
        widget.set(Properties.ALIGN_X, AlignX.Left);
        return this;
    }

    /** Sets {@link Properties#ALIGN_X} to {@link AlignX#Center} for this widget. */
    public Cell<T> centerX() {
        widget.set(Properties.ALIGN_X, AlignX.Center);
        return this;
    }

    /** Sets {@link Properties#ALIGN_X} to {@link AlignX#Right} for this widget. */
    public Cell<T> right() {
        widget.set(Properties.ALIGN_X, AlignX.Right);
        return this;
    }

    /** Sets {@link Properties#ALIGN_Y} to {@link AlignY#Bottom} for this widget. */
    public Cell<T> bottom() {
        widget.set(Properties.ALIGN_Y, AlignY.Bottom);
        return this;
    }

    /** Sets {@link Properties#ALIGN_Y} to {@link AlignY#Center} for this widget. */
    public Cell<T> centerY() {
        widget.set(Properties.ALIGN_Y, AlignY.Center);
        return this;
    }

    /** Sets {@link Properties#ALIGN_Y} to {@link AlignY#Top} for this widget. */
    public Cell<T> top() {
        widget.set(Properties.ALIGN_Y, AlignY.Top);
        return this;
    }

    /** Same as calling {@link #centerX()} and {@link #centerY()}. */
    public Cell<T> center() {
        centerX();
        return centerY();
    }
}
