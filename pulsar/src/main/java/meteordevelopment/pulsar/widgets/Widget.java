package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.Event;
import meteordevelopment.pulsar.input.EventHandler;
import meteordevelopment.pulsar.input.EventType;
import meteordevelopment.pulsar.input.MouseMovedEvent;
import meteordevelopment.pulsar.layout.BasicLayout;
import meteordevelopment.pulsar.layout.Layout;
import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.IStylable;
import meteordevelopment.pulsar.theme.Style;
import meteordevelopment.pulsar.theme.properties.Properties;
import meteordevelopment.pulsar.theme.properties.Property;
import meteordevelopment.pulsar.utils.Color4;
import meteordevelopment.pulsar.utils.Vec2;
import meteordevelopment.pulsar.utils.Vec4;

import java.util.*;

/** Base class for all widgets */
public class Widget extends EventHandler implements IStylable, Iterable<Cell<?>> {
    protected static final String[] NAMES = { "widget" };

    protected Widget parent;

    protected final List<String> tags = new ArrayList<>();
    protected final List<Cell<?>> cells = new ArrayList<>();

    public Layout layout = BasicLayout.INSTANCE;

    public int x, y;
    public int width, height;

    private boolean hovered;
    public Style style;
    private Map<Property<?>, Object> properties;

    public Widget() {
        style = Renderer.INSTANCE.theme.computeStyle(this);
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    // Cells

    protected <T extends Widget> Cell<T> create(T widget) {
        Cell<T> cell = new Cell<>(widget);

        widget.parent = this;
        invalidateLayout();

        return cell;
    }

    /**
     * Adds the specified widget as a children of this widget.
     * @return cell which contains the newly added widget.
     */
    public <T extends Widget> Cell<T> add(T widget) {
        Cell<T> cell = create(widget);

        cells.add(cell);
        layout.onAdd(this, cell);

        return cell;
    }

    /**
     * Removes the specified widget from the children of this widget.
     * @return true if the widget was removed.
     */
    public boolean remove(Widget widget) {
        for (Iterator<Cell<?>> it = cells.iterator(); it.hasNext();) {
            if (it.next().widget() == widget) {
                it.remove();

                invalidateLayout();
                return true;
            }
        }

        return false;
    }

    /** Removes every child widget. */
    public void clear() {
        cells.clear();
        layout.onClear(this);

        invalidateLayout();
    }

    public boolean hasChildren() {
        return cells.size() > 0;
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;

        for (Cell<?> cell : cells) {
            cell.x += x;
            cell.y += y;

            cell.widget().move(x, y);
        }
    }

    @Override
    public Iterator<Cell<?>> iterator() {
        return cells.iterator();
    }

    public CellIterator iterator(boolean reversed) {
        return new CellIterator(reversed);
    }

    // Layout

    /** If this widget should not be included in layout calculations. */
    public boolean shouldSkipLayout() {
        return false;
    }

    /** Called when the widget has no children. */
    public void calculateSize() {
        Vec2 size = get(Properties.SIZE);

        if (size != null) {
            Vec4 padding = get(Properties.PADDING);

            width = size.intX() + padding.horizontal();
            height = size.intY() + padding.vertical();
        }
    }

    /** Invalidates all widgets in this root causing a recalculation of layouts on the next frame. */
    public void invalidateLayout() {
        if (parent != null) parent.invalidateLayout();
    }

    /** Called after children widget positions have been calculated. */
    public void afterLayout() {}

    // Input

    @Override
    public void dispatch(Event event) {
        for (Cell<?> cell : cells) cell.widget().dispatch(event);
        dispatchToSelf(event);
    }

    protected void dispatchToSelf(Event event) {
        super.dispatch(event);
        if (event.type == EventType.MouseMoved) detectHovered((MouseMovedEvent) event);
    }

    // State

    protected void detectHovered(MouseMovedEvent event) {
        boolean lastHovered = hovered;
        hovered = event.x >= x && event.x < x + width && event.y >= y + 1 && event.y <= y + height;

        if (hovered != lastHovered) invalidStyle();
    }

    @Override
    public boolean isHovered() {
        return hovered;
    }

    @Override
    public boolean isPressed() {
        return false;
    }

    // Rendering

    public void render(Renderer renderer, double delta) {
        onRender(renderer, delta);
        for (Cell<?> cell : cells) cell.widget().render(renderer, delta);
    }

    protected void onRender(Renderer renderer, double delta) {
        Vec4 radius = get(Properties.RADIUS);
        double outlineSize = get(Properties.OUTLINE_SIZE);
        Color4 backgroundColor = get(Properties.BACKGROUND_COLOR);
        Color4 outlineColor = get(Properties.OUTLINE_COLOR);

        if (backgroundColor != null || outlineSize > 0) renderer.quad(x, y, width, height, radius, outlineSize, backgroundColor, outlineColor);
    }

    protected void renderText(Renderer renderer, int x, int y, String text) {
        int size = (int) Math.ceil(get(Properties.FONT_SIZE));

        // Shadow
        Color4 color = get(Properties.TEXT_SHADOW);
        Vec2 offset = get(Properties.TEXT_SHADOW_OFFSET);
        if (color != null) renderTextComponent(renderer, x + offset.intX(), y + offset.intY(), text, size, color);

        // Text
        color = get(Properties.COLOR);
        if (color != null) renderTextComponent(renderer, x, y, text, size, color);
    }

    protected void renderTextComponent(Renderer renderer, int x, int y, String text, int size, Color4 color) {
        renderer.text(x, y, text, size, color);
    }

    // Style

    /** Sets a custom property that only applies to this widget. */
    public <T> void set(Property<T> property, T value) {
        if (properties == null) properties = new HashMap<>();
        properties.put(property, value);
    }

    /** @return a value for the specified property. */
    @SuppressWarnings("unchecked")
    public <T> T get(Property<T> property) {
        if (properties != null) {
            T value = (T) properties.get(property);
            if (value != null) return value;
        }

        return style.get(property);
    }

    public void invalidStyle() {
        style = Renderer.INSTANCE.theme.computeStyle(this);
    }

    // Tags

    @Override
    public List<String> tags() {
        return tags;
    }

    /** Adds or removes specified tag based on if this widget already contains the tag. */
    public Widget tag(String tag) {
        if (tags().contains(tag)) tags().remove(tag);
        else tags().add(tag);

        invalidStyle();
        return this;
    }

    /** Adds or removes specified tag based on if this widget already contains the tag. */
    public Widget tag(String tag, boolean shouldHave) {
        boolean contains = tags().contains(tag);

        if (contains && !shouldHave) {
            tags().remove(tag);
            invalidStyle();
        }
        else if (!contains && shouldHave) {
            tags().add(tag);
            invalidStyle();
        }

        return this;
    }

    /** @return true if this widget has the specified tag. */
    public boolean hasTag(String tag) {
        return tags().contains(tag);
    }

    public class CellIterator implements Iterator<Cell<?>> {
        private final boolean reversed;
        private int i;
        private int count;

        public CellIterator(boolean reversed) {
            //reversed = !reversed;

            this.reversed = reversed;
            this.i = reversed ? cells.size() : -1;
        }

        @Override
        public boolean hasNext() {
            return reversed ? i >= 1 : i < cells.size() - 1;
        }

        @Override
        public Cell<?> next() {
            count++;
            return cells.get(i += (reversed ? -1 : 1));
        }

        public boolean isNotFirst() {
            return count > 1;
        }
    }
}
