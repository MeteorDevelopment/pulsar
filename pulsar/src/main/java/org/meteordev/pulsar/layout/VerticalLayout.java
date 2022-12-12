package org.meteordev.pulsar.layout;

import org.meteordev.pts.properties.Properties;
import org.meteordev.pts.utils.ListDirection;
import org.meteordev.pts.utils.Vec2;
import org.meteordev.pts.utils.Vec4;
import org.meteordev.pulsar.widgets.Cell;
import org.meteordev.pulsar.widgets.Widget;

/** Layout which positions all widgets vertically next to each other. */
public class VerticalLayout extends Layout {
    public static final VerticalLayout INSTANCE = new VerticalLayout();

    protected VerticalLayout() {}

    @Override
    protected void calculateSizeImpl(Widget widget) {
        Vec4 padding = widget.get(Properties.PADDING);
        Vec2 spacing = widget.get(Properties.SPACING);

        for (Widget.CellIterator it = widget.iterator(false); it.hasNext();) {
            Cell<?> cell = it.next();
            if (cell.widget().shouldSkipLayout()) continue;

            if (it.isNotFirst()) widget.height += spacing.intY();

            widget.width = Math.max(widget.width, cell.widget().width + padding.horizontal());
            widget.height += cell.widget().height;
        }

        widget.height += padding.vertical();
    }

    @Override
    public void positionChildrenImpl(Widget widget) {
        Vec4 padding = widget.get(Properties.PADDING);
        Vec2 spacing = widget.get(Properties.SPACING);
        boolean reversed = widget.get(Properties.LIST_DIRECTION) == ListDirection.Reversed;

        int y = widget.y + padding.top();

        for (Widget.CellIterator it = widget.iterator(reversed); it.hasNext();) {
            Cell<?> cell = it.next();
            if (cell.widget().shouldSkipLayout()) continue;

            cell.x = padding.left() + widget.x;
            cell.y = y;

            cell.width = widget.width - padding.horizontal();
            cell.height = cell.widget().height;

            cell.align();

            y += cell.height + spacing.intY();
        }
    }
}
