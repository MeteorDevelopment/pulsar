package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.theme.Properties;

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
}
