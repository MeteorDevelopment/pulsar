package org.meteordev.pulsar.widgets;

import org.meteordev.pts.properties.Properties;
import org.meteordev.pts.utils.Color4;
import org.meteordev.pts.utils.Vec2;
import org.meteordev.pts.utils.Vec4;
import org.meteordev.pulsar.input.Event;
import org.meteordev.pulsar.input.EventType;
import org.meteordev.pulsar.input.MouseMovedEvent;
import org.meteordev.pulsar.layout.BasicLayout;
import org.meteordev.pulsar.layout.HorizontalLayout;
import org.meteordev.pulsar.layout.VerticalLayout;
import org.meteordev.pulsar.rendering.Renderer;
import org.meteordev.pulsar.utils.Utils;

import java.lang.reflect.InvocationTargetException;

import static org.meteordev.pulsar.utils.Utils.combine;

public class WDropdown<T> extends WPressable {
    protected static final String[] NAMES = combine(Widget.NAMES, "dropdown");

    public Runnable action;

    private final T[] values;
    private T value;

    private boolean expanded;
    private Vec2 minSize;
    private double animation;

    private WIcon iconW;
    private WText textW;
    private WBody root;

    public WDropdown(T[] values, T value) {
        this.values = values;
        this.value = value;

        init();
    }

    @SuppressWarnings("unchecked")
    public <E extends Enum<?>> WDropdown(E value) {
        Class<?> klass = value.getClass();
        T[] values = null;

        try {
            values = (T[]) klass.getDeclaredMethod("values").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.values = values;
        this.value = (T) value;

        init();
    }

    private void init() {
        String icon = get(Properties.ICON);
        if (icon != null) {
            iconW = add(new WDropdownIcon()).widget();
            iconW.tag(icon);
            iconW.calculateSize();

            layout = new MainHorizontalLayout();
        }
        else {
            layout = new MainLayout();
        }

        textW = add(new WText(value.toString())).expandCellX().widget();
        textW.tag("dropdown-text");

        root = new WBody();

        double maxWidth = 0;
        Vec2 spacing = get(Properties.SPACING);

        for (T value : values) {
            String string = value.toString();

            Widget valueW = root.add(new WValue(value).tag("dropdown-value")).expandX().widget();
            Widget textW = valueW.add(new WText(string).tag("dropdown-value-text")).widget();

            Vec4 padding = valueW.get(Properties.PADDING);
            double size = textW.get(Properties.FONT_SIZE);

            maxWidth = Math.max(maxWidth, padding.horizontal() + Renderer.INSTANCE.textWidth(get(Properties.FONT), string, size) + (iconW != null ? (spacing.x + iconW.width) : 0));
        }

        layout.calculateSize(this);
        minSize = new Vec2(Math.max(maxWidth, width), 0);

        root.layout.calculateSize(root);
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    public void afterLayout() {
        Vec2 spacing = get(Properties.SPACING);

        root.x = x;
        root.y = y + height + spacing.intY();

        root.layout.positionChildren(root);
    }

    @Override
    protected void doAction() {
        expanded = !expanded;
        if (expanded) animation = 0;
    }

    @Override
    public void dispatch(Event event) {
        if (event.type == EventType.MousePressed && expanded && !isHovered() && !root.isHovered()) expanded = false;

        if (expanded) root.dispatch(event);
        super.dispatch(event);
    }

    @Override
    public void invalidStyle() {
        super.invalidStyle();
        if (iconW != null) iconW.invalidStyle();
    }

    @Override
    public void move(int x, int y) {
        super.move(x, y);
        root.move(x, y);
    }

    @Override
    protected void onRender(Renderer renderer, double delta) {
        super.onRender(renderer, delta);

        if (expanded || animation > 0) {
            animation = Utils.clamp(animation + (expanded ? delta * 10 : -delta * 10), 0, 1);
            if (animation == 0) return;

            renderer.after(() -> {
                double preOffsetY = renderer.offsetY;

                if (animation < 1) {
                    renderer.offsetY = -(root.height + get(Properties.SPACING).intY()) * (1 - animation);
                    renderer.beginScissor(root.x, root.y, root.width, root.height);
                }

                root.render(renderer, delta);

                if (animation > 0 && animation < 1) {
                    renderer.endScissor();
                    renderer.offsetY = preOffsetY;
                }
            });
        }
    }

    public void set(T value) {
        this.value = value;
        textW.setText(value.toString());
    }

    private Vec2 getMinSize() {
        Vec2 minSize = get(Properties.SIZE);

        if (minSize == null) return this.minSize;
        else {
            if (this.minSize == null) return minSize;
            return new Vec2(Math.max(minSize.x, this.minSize.x), Math.max(minSize.y, this.minSize.y));
        }
    }

    private class MainLayout extends BasicLayout {
        @Override
        protected Vec2 getMinSize(Widget widget) {
            return WDropdown.this.getMinSize();
        }
    }

    private class MainHorizontalLayout extends HorizontalLayout {
        @Override
        protected Vec2 getMinSize(Widget widget) {
            return WDropdown.this.getMinSize();
        }
    }

    private class WDropdownIcon extends WIcon {
        protected static final String[] NAMES = combine(WIcon.NAMES, "dropdown-icon");

        @Override
        public String[] names() {
            return NAMES;
        }

        @Override
        protected void detectHovered(MouseMovedEvent event) {}

        @Override
        public boolean isHovered() {
            return WDropdown.this.isHovered();
        }

        @Override
        public boolean isPressed() {
            return WDropdown.this.isPressed();
        }
    }

    private class WBody extends WVerticalList {
        public WBody() {
            layout = new BodyLayout();

            tag("dropdown-body");
        }

        @Override
        public void render(Renderer renderer, double delta) {
            super.render(renderer, delta);

            Vec4 radius = get(Properties.RADIUS);
            double outlineSize = get(Properties.OUTLINE_SIZE);
            Color4 outlineColor = get(Properties.OUTLINE_COLOR);

            if (outlineColor != null && outlineSize > 0) renderer.quad(x, y, width, height, radius, outlineSize, null, outlineColor);
        }

        @Override
        protected void onRender(Renderer renderer, double delta) {
            Vec4 radius = get(Properties.RADIUS);
            Color4 backgroundColor = get(Properties.BACKGROUND_COLOR);

            if (backgroundColor != null) renderer.quad(x, y, width, height, radius, 0, backgroundColor, null);
        }

        private class BodyLayout extends VerticalLayout {
            @Override
            protected Vec2 getMinSize(Widget widget) {
                return minSize;
            }
        }
    }

    private class WValue extends WPressable {
        private final T value;

        public WValue(T value) {
            this.value = value;
        }

        @Override
        protected void doAction() {
            boolean different = !WDropdown.this.value.equals(value);
            expanded = false;

            if (different) {
                WDropdown.this.set(value);
                if (action != null) action.run();
            }
        }
    }
}
