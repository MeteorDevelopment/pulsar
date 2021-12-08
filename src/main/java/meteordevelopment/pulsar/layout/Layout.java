package meteordevelopment.pulsar.layout;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.utils.Vec2;
import meteordevelopment.pulsar.widgets.Cell;
import meteordevelopment.pulsar.widgets.Widget;

/** Base class for all layouts */
public abstract class Layout {
    /** Calculates size for this specified widget and all its children. */
    public final void calculateSize(Widget widget, Renderer renderer) {
        for (Cell<?> cell : widget)
            cell.widget().layout.calculateSize(cell.widget(), renderer);

        widget.width = widget.height = 0;

        if (widget.hasChildren()) calculateSizeImpl(widget);
        else widget.calculateSize();

        Vec2 minSize = getMinSize(widget);
        if (minSize != null) {
            widget.width = Math.max(widget.width, minSize.x());
            widget.height = Math.max(widget.height, minSize.y());
        }

        widget.width = Math.round(widget.width);
        widget.height = Math.round(widget.height);
    }

    /** The actual implementation for size calculation. */
    protected abstract void calculateSizeImpl(Widget widget);

    protected Vec2 getMinSize(Widget widget) {
        return widget.get(Properties.MINIMUM_SIZE);
    }

    /** Position's children widgets and its children according to this layout. */
    public final void positionChildren(Widget widget) {
        positionChildrenImpl(widget);

        for (Cell<?> cell : widget) {
            cell.widget().layout.positionChildren(cell.widget());

            cell.widget().x = Math.round(cell.widget().x);
            cell.widget().y = Math.round(cell.widget().y);

            cell.widget().afterLayout();
        }
    }

    /** The actual implementation for positioning children. */
    protected abstract void positionChildrenImpl(Widget widget);

    /** Called when a cell is added to widget. */
    public void onAdd(Widget widget, Cell<?> cell) {}

    /** Called when all widget's children are removed. */
    public void onClear(Widget widget) {}
}
