package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.utils.Cell;
import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.theme.Theme;
import meteordevelopment.pulsar.utils.Vec4;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WContainer extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "container");

    protected final List<Cell<?>> cells = new ArrayList<>();

    public <T extends Widget> Cell<T> add(T widget) {
        Cell<T> cell = new Cell<>(widget);
        widget.parent = this;

        cells.add(cell);

        invalidate();
        return cell;
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    // Layout

    @Override
    public void calculateSize() {
        for (Cell<?> cell : cells) cell.widget.calculateSize();
        super.calculateSize();
    }

    @Override
    protected void onCalculateSize() {
        Vec4 padding = get(Properties.PADDING);

        width = 0;
        height = 0;

        for (Cell<?> cell : cells) {
            width = Math.max(width, cell.widget.width + padding.horizontal());
            height = Math.max(height, cell.widget.height + padding.vertical());
        }
    }

    @Override
    public void calculateWidgetPositions() {
        super.calculateWidgetPositions();
        for (Cell<?> cell : cells) cell.widget.calculateWidgetPositions();
    }

    @Override
    protected void onCalculateWidgetPositions() {
        Vec4 padding = get(Properties.PADDING);

        for (Cell<?> cell : cells) {
            cell.x = padding.left() + x;
            cell.y = padding.bottom() + y;

            cell.width = width - padding.horizontal();
            cell.height = height - padding.vertical();

            cell.align();
        }
    }

    // Rendering

    @Override
    public void render(Renderer renderer, double mouseX, double mouseY, double delta) {
        super.render(renderer, mouseX, mouseY, delta);
        for (Cell<?> cell : cells) cell.widget.render(renderer, mouseX, mouseY, delta);
    }

    // Other

    @Override
    public WContainer minWidth(double minWidth) {
        return (WContainer) super.minWidth(minWidth);
    }

    @Override
    public WContainer minHeight(double minHeight) {
        return (WContainer) super.minHeight(minHeight);
    }

    @Override
    protected void move(double x, double y) {
        super.move(x, y);
        for (Cell<?> cell : cells) cell.widget.move(x, y);
    }

    // Input

    @Override
    public void mouseMoved(double mouseX, double mouseY, double deltaMouseX, double deltaMouseY) {
        for (Cell<?> cell : cells) cell.widget.mouseMoved(mouseX, mouseY, deltaMouseX, deltaMouseY);
        super.mouseMoved(mouseX, mouseY, deltaMouseX, deltaMouseY);
    }

    @Override
    public void mousePressed(int button) {
        for (Cell<?> cell : cells) cell.widget.mousePressed(button);
        super.mousePressed(button);
    }

    @Override
    public void mouseReleased(int button) {
        for (Cell<?> cell : cells) cell.widget.mouseReleased(button);
        super.mouseReleased(button);
    }

    // Style

    @Override
    public void computeStyle(Theme theme) {
        if (style != null) return;

        super.computeStyle(theme);
        for (Cell<?> cell : cells) cell.widget.computeStyle(theme);
    }

    @Override
    public WContainer id(String id) {
        return (WContainer) super.id(id);
    }
}
