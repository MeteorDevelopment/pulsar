package meteordevelopment.pulsar.layout;

import meteordevelopment.pts.properties.Properties;
import meteordevelopment.pts.utils.Vec2;
import meteordevelopment.pulsar.widgets.Cell;
import meteordevelopment.pulsar.widgets.Widget;

/** Base class for all layouts */
public abstract class Layout {
    /** Calculates size for this specified widget and all its children. */
    public void calculateSize(Widget widget) {
        for (Cell<?> cell : widget) cell.widget().layout.calculateSize(cell.widget());

        widget.width = widget.height = 0;

        if (widget.hasChildren()) calculateSizeImpl(widget);
        else widget.calculateSize();

        Vec2 minSize = getMinSize(widget);
        if (minSize != null) {
            widget.width = Math.max(widget.width, minSize.intX());
            widget.height = Math.max(widget.height, minSize.intY());
        }
    }

    /** The actual implementation for size calculation. */
    protected abstract void calculateSizeImpl(Widget widget);

    protected Vec2 getMinSize(Widget widget) {
        return widget.get(Properties.MINIMUM_SIZE);
    }

    /** Position's children widgets and its children according to this layout. */
    public void positionChildren(Widget widget) {
        positionChildrenImpl(widget);

        for (Cell<?> cell : widget) cell.widget().layout.positionChildren(cell.widget());
    }

    /** The actual implementation for positioning children. */
    protected abstract void positionChildrenImpl(Widget widget);

    /** Tries to satisfy max-width and max-height properties. */
    public void adjustToMaxSize(MaxSizeCalculationContext ctx, Widget widget) {
        ctx.pushMaxSize(widget);

        for (Cell<?> cell : widget) cell.widget().layout.adjustToMaxSize(ctx, cell.widget());

        if (widget.adjustToMaxSize(ctx)) ctx.setAdjusted();

        ctx.popMaxSize();
    }

    /** Last step in the layout calculation process. */
    public void afterLayout(Widget widget) {
        widget.afterLayout();

        for (Cell<?> cell : widget) cell.widget().layout.afterLayout(cell.widget());
    }

    /** Called when a cell is added to widget. */
    public void onAdd(Widget widget, Cell<?> cell) {}

    /** Called when all widget's children are removed. */
    public void onClear(Widget widget) {}
}
