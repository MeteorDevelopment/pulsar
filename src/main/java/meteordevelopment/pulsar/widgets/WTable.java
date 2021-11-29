package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.utils.Cell;
import meteordevelopment.pulsar.utils.Vec2;
import meteordevelopment.pulsar.utils.Vec4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WTable extends WContainer {
    protected static final String[] NAMES = combine(WContainer.NAMES, "table");

    private final List<List<Cell<?>>> rows = new ArrayList<>();
    private int rowI;

    private final List<Double> rowHeights = new ArrayList<>();
    private final List<Double> columnWidths = new ArrayList<>();

    private final List<Double> rowWidths = new ArrayList<>();
    private final List<Integer> rowExpandCellXCounts = new ArrayList<>();

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    public <T extends Widget> Cell<T> add(T widget) {
        Cell<T> cell = super.add(widget);

        if (rows.size() <= rowI) {
            List<Cell<?>> row = new ArrayList<>();
            row.add(cell);
            rows.add(row);
        }
        else rows.get(rowI).add(cell);

        return cell;
    }

    @Override
    public void clear() {
        super.clear();
        rows.clear();
        rowI = 0;
    }

    public void row() {
        rowI++;
    }

    public int rowI() {
        return rowI;
    }

    public void removeRow(int i) {
        for (Cell<?> cell : rows.remove(i)) {
            for (Iterator<Cell<?>> it = cells.iterator(); it.hasNext();) {
                if (it.next() == cell) {
                    it.remove();
                    break;
                }
            }
        }

        rowI--;
    }

    @Override
    protected void onCalculateSize() {
        calculateInfo();

        Vec4 padding = get(Properties.PADDING);
        Vec2 spacing = get(Properties.SPACING);

        // Reset
        rowWidths.clear();

        width = 0;
        height = 0;

        // Loop over rows
        for (int rowI = 0; rowI < rows.size(); rowI++) {
            List<Cell<?>> row = rows.get(rowI);

            double rowWidth = 0;

            // Loop over cells in the row
            for (int cellI = 0; cellI < row.size(); cellI++) {
                // Calculate row width
                if (cellI > 0) rowWidth += spacing.x();
                rowWidth += columnWidths.get(cellI);
            }

            // Store row width
            rowWidths.add(rowWidth);
            width = Math.max(width, rowWidth);

            // Calculate height
            if (rowI > 0) height += spacing.y();
            height += rowHeights.get(rowI);
        }

        width += padding.horizontal();
        height += padding.vertical();
    }

    @Override
    protected void onCalculateWidgetPositions() {
        Vec4 padding = get(Properties.PADDING);
        Vec2 spacing = get(Properties.SPACING);

        double y = this.y + padding.bottom();

        // Loop over rows
        for (int rowI = rows.size() - 1; rowI >= 0; rowI--) {
            List<Cell<?>> row = rows.get(rowI);

            if (rowI < rows.size() - 1) y += spacing.y();

            double x = this.x + padding.left();
            double rowHeight = rowHeights.get(rowI);

            double expandXAdd = rowExpandCellXCounts.get(rowI) > 0 ? (width - rowWidths.get(rowI)) / rowExpandCellXCounts.get(rowI) : 0;

            // Loop over cells in the row
            for (int cellI = 0; cellI < row.size(); cellI++) {
                Cell<?> cell = row.get(cellI);

                if (cellI > 0) x += spacing.x();
                double columnWidth = columnWidths.get(cellI);

                cell.x = x;
                cell.y = y;

                cell.width = columnWidth + (/*cell.expandCellX ? expandXAdd : */0);
                cell.height = rowHeight;

                cell.align();

                x += columnWidth + (/*cell.expandCellX ? expandXAdd : */0);
            }

            y += rowHeight;
        }
    }

    private void calculateInfo() {
        // Reset
        rowHeights.clear();
        columnWidths.clear();
        rowExpandCellXCounts.clear();

        // Loop over rows
        for (List<Cell<?>> row : rows) {
            double rowHeight = 0;
            int rowExpandXCount = 0;

            // Loop over cells in the row
            for (int i = 0; i < row.size(); i++) {
                Cell<?> cell = row.get(i);

                // Calculate row height
                rowHeight = Math.max(rowHeight, cell.widget.height);

                // Calculate column width
                double cellWidth = cell.widget.width;
                if (columnWidths.size() <= i) columnWidths.add(cellWidth);
                else columnWidths.set(i, Math.max(columnWidths.get(i), cellWidth));

                // Calculate row expandX count
                //if (cell.expandCellX) rowExpandXCount++;
            }

            // Store calculated info
            rowHeights.add(rowHeight);
            rowExpandCellXCounts.add(rowExpandXCount);
        }
    }
}
