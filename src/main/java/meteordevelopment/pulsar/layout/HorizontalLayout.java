package meteordevelopment.pulsar.layout;

import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.utils.ListDirection;
import meteordevelopment.pulsar.utils.Vec2;
import meteordevelopment.pulsar.utils.Vec4;
import meteordevelopment.pulsar.widgets.Cell;
import meteordevelopment.pulsar.widgets.Widget;

/** Layout which positions all widgets horizontally next to each other. */
public class HorizontalLayout extends Layout {
    private int expandCellCount;
    private double calculatedWidth;

    @Override
    protected void calculateSizeImpl(Widget widget) {
        Vec4 padding = widget.get(Properties.PADDING);
        Vec2 spacing = widget.get(Properties.SPACING);

        expandCellCount = 0;

        for (Widget.CellIterator it = widget.iterator(false); it.hasNext();) {
            Cell<?> cell = it.next();
            if (cell.widget().shouldSkipLayout()) continue;

            if (it.isNotFirst()) widget.width += spacing.x();

            widget.width += cell.widget().width;
            widget.height = Math.max(widget.height, cell.widget().height + padding.vertical());

            if (cell.expandCellX) expandCellCount++;
        }

        widget.width += padding.horizontal();

        calculatedWidth = widget.width;
    }

    @Override
    public void positionChildrenImpl(Widget widget) {
        Vec4 padding = widget.get(Properties.PADDING);
        Vec2 spacing = widget.get(Properties.SPACING);
        boolean reversed = widget.get(Properties.LIST_DIRECTION) == ListDirection.Reversed;

        double x = widget.x + padding.x();
        double expandWidth = (widget.width - calculatedWidth) / expandCellCount;

        for (Widget.CellIterator it = widget.iterator(reversed); it.hasNext();) {
            Cell<?> cell = it.next();
            if (cell.widget().shouldSkipLayout()) continue;

            cell.x = x;
            cell.y = padding.bottom() + widget.y;

            cell.width = cell.widget().width;
            cell.height = widget.height - padding.vertical();

            if (cell.expandCellX) cell.width += expandWidth;
            cell.align();

            x += cell.width + spacing.x();
        }
    }
}
