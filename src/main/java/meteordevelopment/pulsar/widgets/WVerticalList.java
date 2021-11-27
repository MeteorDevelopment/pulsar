package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.utils.Cell;
import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.utils.ListDirection;
import meteordevelopment.pulsar.utils.Vec2;
import meteordevelopment.pulsar.utils.Vec4;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WVerticalList extends WContainer {
    protected static final String[] NAMES = combine(WContainer.NAMES, "vertical-list");

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    protected void onCalculateSize() {
        Vec4 padding = get(Properties.PADDING);
        Vec2 spacing = get(Properties.SPACING);

        width = 0;
        height = padding.vertical();

        for (int i = 0; i < cells.size(); i++) {
            Widget widget = cells.get(i).widget;

            width = Math.max(width, widget.width + padding.horizontal());
            height += widget.height;

            if (i > 0) height += spacing.y();
        }
    }

    @Override
    protected void onCalculateWidgetPositions() {
        Vec4 padding = get(Properties.PADDING);
        Vec2 spacing = get(Properties.SPACING);
        boolean reversed = get(Properties.LIST_DIRECTION) == ListDirection.Reversed;

        double y = padding.x() + this.y;

        for (int i = reversed ? 0 : cells.size() - 1; reversed ? i < cells.size() : i >= 0; i += reversed ? 1 : -1) {
             Cell<?> cell = cells.get(i);

            cell.x = padding.w() + x;
            cell.y = y;

            cell.width = width - padding.horizontal();
            cell.height = cell.widget.height;
            cell.align();

            y += cell.height + spacing.y();
        }
    }
}
