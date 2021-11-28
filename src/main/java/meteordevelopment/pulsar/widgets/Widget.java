package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.theme.Property;
import meteordevelopment.pulsar.theme.Style;
import meteordevelopment.pulsar.theme.Theme;
import meteordevelopment.pulsar.utils.Color4;
import meteordevelopment.pulsar.utils.Vec2;
import meteordevelopment.pulsar.utils.Vec4;

public class Widget {
    protected static final String[] NAMES = { "widget" };

    public boolean visible = true;
    protected Widget parent;

    private String id;
    protected Style style;

    public double x, y;
    public double width, height;
    private double minWidth, minHeight;

    protected boolean hovered;

    public String[] names() {
        return NAMES;
    }

    // Layout

    public void calculateSize() {
        onCalculateSize();

        width = Math.round(Math.max(width, minWidth));
        height = Math.round(Math.max(height, minHeight));
    }

    protected void onCalculateSize() {
        Vec2 size = get(Properties.SIZE);

        width = size.x();
        height = size.y();
    }

    public void calculateWidgetPositions() {
        x = Math.round(x);
        y = Math.round(y);

        onCalculateWidgetPositions();
    }

    protected void onCalculateWidgetPositions() {

    }

    protected void move(double x, double y) {
        this.x += x;
        this.y += y;
    }

    // Rendering

    public void render(Renderer renderer, double mouseX, double mouseY, double delta) {
        computeStyle(renderer.theme);

        onRender(renderer, mouseX, mouseY, delta);
    }

    protected void onRender(Renderer renderer, double mouseX, double mouseY, double delta) {
        Vec4 radius = get(Properties.RADIUS);
        double outlineSize = get(Properties.OUTLINE_SIZE);
        Color4 backgroundColor = get(Properties.BACKGROUND_COLOR);
        Color4 outlineColor = get(Properties.OUTLINE_COLOR);

        if (backgroundColor != null || outlineSize > 0) renderer.quad(x, y, width, height, radius, outlineSize, backgroundColor, outlineColor);
    }

    // Input

    public boolean mousePressed(int button, double mouseX, double mouseY, boolean used) {
        return onMousePressed(button, mouseX, mouseY, used);
    }
    protected boolean onMousePressed(int button, double mouseX, double mouseY, boolean used) {
        return false;
    }

    public void mouseMoved(double mouseX, double mouseY, double deltaMouseX, double deltaMouseY) {
        boolean preHovered = hovered;
        hovered = isOver(mouseX, mouseY);
        if (hovered != preHovered) {
            style = null;
            computeStyle(Renderer.INSTANCE.theme);
        }

        onMouseMoved(mouseX, mouseY, deltaMouseX, deltaMouseY);
    }
    protected void onMouseMoved(double mouseX, double mouseY, double deltaMouseX, double deltaMouseY) {}

    public boolean mouseReleased(int button, double mouseX, double mouseY) {
        return onMouseReleased(button, mouseX, mouseY);
    }
    protected boolean onMouseReleased(int button, double mouseX, double mouseY) {
        return false;
    }

    public boolean keyPressed(int key, int mods) {
        return onKeyPressed(key, mods);
    }
    protected boolean onKeyPressed(int key, int mods) {
        return false;
    }

    public boolean keyRepeated(int key, int mods) {
        return onKeyRepeated(key, mods);
    }
    protected boolean onKeyRepeated(int key, int mods) {
        return false;
    }

    public boolean charTyped(char c) {
        return onCharTyped(c);
    }
    protected boolean onCharTyped(char c) {
        return false;
    }

    // Other

    public void invalidate() {
        if (parent != null) parent.invalidate();
    }

    protected boolean isOver(double x, double y) {
        return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
    }

    public Widget minWidth(double minWidth) {
        this.minWidth = minWidth;
        return this;
    }

    public Widget minHeight(double minHeight) {
        this.minHeight = minHeight;
        return this;
    }

    // Style

    public void computeStyle(Theme theme) {
        if (style == null) style = theme.computeStyle(this);
    }

    public <T> T get(Property<T> property) {
        return style.get(property);
    }

    public Widget id(String id) {
        this.id = id;
        return this;
    }

    public String id() {
        return id;
    }

    public String state() {
        return hovered ? "hovered" : null;
    }
}
