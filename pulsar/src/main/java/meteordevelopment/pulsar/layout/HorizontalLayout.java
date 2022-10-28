package meteordevelopment.pulsar.layout;

import meteordevelopment.pulsar.theme.properties.Properties;
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

            if (it.isNotFirst()) widget.width += spacing.intX();

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

        int x = widget.x + padding.left();

        double expandWidthD = (widget.width - calculatedWidth) / expandCellCount;
        int expandWidth = (int) expandWidthD;
        Cell<?> lastExpandWidth = null;

        for (Widget.CellIterator it = widget.iterator(reversed); it.hasNext();) {
            Cell<?> cell = it.next();
            if (cell.widget().shouldSkipLayout()) continue;

            cell.x = x;
            cell.y = padding.top() + widget.y;

            cell.width = cell.widget().width;
            cell.height = widget.height - padding.vertical();

            if (cell.expandCellX) {
                cell.width += expandWidth;
                lastExpandWidth = cell;
            }

            cell.align();

            x += cell.width + spacing.intX();
        }

        if (lastExpandWidth != null) {
            lastExpandWidth.width += (int) Math.ceil((expandWidthD - expandWidth) * expandCellCount);
            lastExpandWidth.align();
        }
    }
}
