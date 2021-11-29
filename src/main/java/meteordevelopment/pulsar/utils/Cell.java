package meteordevelopment.pulsar.utils;

import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.widgets.Widget;

public class Cell<T extends Widget> {
    public final T widget;

    public double x, y;
    public double width, height;

    public boolean expandCellX;
    private boolean expandWidgetX;

    public Cell(T widget) {
        this.widget = widget;
    }

    public Cell<T> expandCellX() {
        expandCellX = true;
        return this;
    }

    public Cell<T> expandX() {
        expandCellX = expandWidgetX = true;
        return this;
    }

    public void align() {
        if (expandWidgetX) widget.width = width;

        switch (widget.get(Properties.ALIGN_X)) {
            case Left ->   widget.x = x;
            case Center -> widget.x = x + width / 2 - widget.width / 2;
            case Right ->  widget.x = x + width - widget.width;
        }

        switch (widget.get(Properties.ALIGN_Y)) {
            case Bottom -> widget.y = y;
            case Center -> widget.y = y + height / 2 - widget.height / 2;
            case Top ->    widget.y = y + height - widget.height;
        }
    }
}
