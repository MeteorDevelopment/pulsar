package org.meteordev.pulsar.layout;

import org.meteordev.pts.properties.Properties;
import org.meteordev.pts.utils.Vec2;
import org.meteordev.pts.utils.Vec4;
import org.meteordev.pulsar.widgets.Cell;
import org.meteordev.pulsar.widgets.Widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Layout which positions all widgets using a virtual table. */
public class TableLayout extends Layout {
    private final List<List<Cell<?>>> rows = new ArrayList<>();
    public int rowI;

    private final List<Integer> rowHeights = new ArrayList<>();
    private final List<Integer> columnWidths = new ArrayList<>();

    private final List<Integer> rowWidths = new ArrayList<>();
    private final List<Integer> rowExpandCellXCounts = new ArrayList<>();

    public void row() {
        rowI++;
    }

    public void removeRow(Widget widget, int i) {
        for (Cell<?> cell : rows.remove(i)) {
            for (Iterator<Cell<?>> it = widget.iterator(); it.hasNext();) {
                if (it.next() == cell) {
                    it.remove();
                    break;
                }
            }
        }

        rowI--;
    }

    @Override
    public void onAdd(Widget widget, Cell<?> cell) {
        if (rows.size() <= rowI) {
            List<Cell<?>> row = new ArrayList<>();
            row.add(cell);
            rows.add(row);
        }
        else rows.get(rowI).add(cell);
    }

    @Override
    public void onClear(Widget widget) {
        rows.clear();
        rowI = 0;
    }

    @Override
    protected void calculateSizeImpl(Widget widget) {
        calculateInfo();

        Vec4 padding = widget.get(Properties.PADDING);
        Vec2 spacing = widget.get(Properties.SPACING);

        // Reset
        rowWidths.clear();

        // Loop over rows
        for (int rowI = 0; rowI < rows.size(); rowI++) {
            List<Cell<?>> row = rows.get(rowI);

            int rowWidth = 0;

            // Loop over cells in the row
            for (int cellI = 0; cellI < row.size(); cellI++) {
                // Calculate row width
                if (cellI > 0) rowWidth += spacing.intX();
                rowWidth += columnWidths.get(cellI);
            }

            // Store row width
            rowWidths.add(rowWidth);
            widget.width = Math.max(widget.width, rowWidth);

            // Calculate height
            if (rowI > 0) widget.height += spacing.intY();
            widget.height += rowHeights.get(rowI);
        }

        widget.width += padding.horizontal();
        widget.height += padding.vertical();
    }

    @Override
    protected void positionChildrenImpl(Widget widget) {
        Vec4 padding = widget.get(Properties.PADDING);
        Vec2 spacing = widget.get(Properties.SPACING);

        int y = widget.y + padding.top();

        // Loop over rows
        for (int rowI = 0; rowI < rows.size(); rowI++) {
            List<Cell<?>> row = rows.get(rowI);

            if (rowI > 0) y += spacing.intY();

            int x = widget.x + padding.left();
            int rowHeight = rowHeights.get(rowI);

            double expandXAddD = rowExpandCellXCounts.get(rowI) > 0 ? ((double) widget.width - rowWidths.get(rowI)) / rowExpandCellXCounts.get(rowI) : 0;
            int expandXAdd = (int) expandXAddD;
            Cell<?> lastExpandX = null;

            // Loop over cells in the row
            for (int cellI = 0; cellI < row.size(); cellI++) {
                Cell<?> cell = row.get(cellI);

                if (cellI > 0) x += spacing.intX();
                int columnWidth = columnWidths.get(cellI);

                cell.x = x;
                cell.y = y;

                cell.width = columnWidth + (cell.expandCellX ? expandXAdd : 0);
                cell.height = rowHeight;

                cell.align();

                x += columnWidth + (cell.expandCellX ? expandXAdd : 0);
                if (cell.expandCellX) lastExpandX = cell;
            }

            if (lastExpandX != null) {
                lastExpandX.width += (int) Math.ceil((expandXAddD - expandXAdd) * rowExpandCellXCounts.get(rowI));
                lastExpandX.align();
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
            int rowHeight = 0;
            int rowExpandXCount = 0;

            // Loop over cells in the row
            for (int i = 0; i < row.size(); i++) {
                Cell<?> cell = row.get(i);

                // Calculate row height
                rowHeight = Math.max(rowHeight, cell.widget().height);

                // Calculate column width
                int cellWidth = cell.widget().width;
                if (columnWidths.size() <= i) columnWidths.add(cellWidth);
                else columnWidths.set(i, Math.max(columnWidths.get(i), cellWidth));

                // Calculate row expandX count
                if (cell.expandCellX) rowExpandXCount++;
            }

            // Store calculated info
            rowHeights.add(rowHeight);
            rowExpandCellXCounts.add(rowExpandXCount);
        }
    }
}
