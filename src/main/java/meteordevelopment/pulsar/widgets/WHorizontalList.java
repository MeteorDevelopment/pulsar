package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.utils.Cell;
import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.utils.ListDirection;
import meteordevelopment.pulsar.utils.Vec2;
import meteordevelopment.pulsar.utils.Vec4;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WHorizontalList extends WContainer {
    protected static final String[] NAMES = combine(WContainer.NAMES, "horizontal-list");

    protected double calculatedWidth;
    protected int fillXCount;

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    protected void onCalculateSize() {
        Vec4 padding = get(Properties.PADDING);
        Vec2 spacing = get(Properties.SPACING);

        width = padding.horizontal();
        height = 0;

        fillXCount = 0;

        for (int i = 0; i < cells.size(); i++) {
            Cell<?> cell = cells.get(i);

            width += cell.widget.width;
            height = Math.max(height, cell.widget.height + padding.vertical());

            if (i > 0) width += spacing.x();
            if (cell.expandCellX) fillXCount++;
        }

        calculatedWidth = width;
    }

    @Override
    protected void onCalculateWidgetPositions() {
        Vec4 padding = get(Properties.PADDING);
        Vec2 spacing = get(Properties.SPACING);
        boolean reversed = get(Properties.LIST_DIRECTION) == ListDirection.Reversed;

        double x = padding.left() + this.x;
        double fillXWidth = (width - calculatedWidth) / fillXCount;

        for (int i = reversed ? cells.size() - 1 : 0; reversed ? i >= 0 : i < cells.size(); i += reversed ? -1 : 1) {
            Cell<?> cell = cells.get(i);

            cell.x = x;
            cell.y = padding.bottom() + y;

            cell.width = cell.widget.width;
            cell.height = height - padding.vertical();

            if (cell.expandCellX) cell.width += fillXWidth;
            cell.align();

            x += cell.width + spacing.x();
        }
    }
}
