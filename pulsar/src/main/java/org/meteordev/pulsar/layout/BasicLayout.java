package org.meteordev.pulsar.layout;

import org.meteordev.pts.properties.Properties;
import org.meteordev.pts.utils.Vec4;
import org.meteordev.pulsar.widgets.Cell;
import org.meteordev.pulsar.widgets.Widget;

/** Basic layout which positions all widgets on top of each other. */
public class BasicLayout extends Layout {
    public static final BasicLayout INSTANCE = new BasicLayout();

    protected BasicLayout() {}

    @Override
    protected void calculateSizeImpl(Widget widget) {
        Vec4 padding = widget.get(Properties.PADDING);

        for (Cell<?> cell : widget) {
            if (cell.widget().shouldSkipLayout()) continue;

            widget.width = Math.max(widget.width, cell.widget().width + padding.horizontal());
            widget.height = Math.max(widget.height, cell.widget().height + padding.vertical());
        }
    }

    @Override
    public void positionChildrenImpl(Widget widget) {
        Vec4 padding = widget.get(Properties.PADDING);

        for (Cell<?> cell : widget) {
            if (cell.widget().shouldSkipLayout()) continue;

            cell.x = padding.left() + widget.x;
            cell.y = padding.top() + widget.y;

            cell.width = widget.width - padding.horizontal();
            cell.height = widget.height - padding.vertical();

            cell.align();
        }
    }
}
