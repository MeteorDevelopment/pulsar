package org.meteordev.pulsar.widgets;

import org.meteordev.pulsar.input.Event;
import org.meteordev.pulsar.input.MouseButtonEvent;
import org.meteordev.pulsar.input.MouseMovedEvent;
import org.meteordev.pulsar.layout.VerticalLayout;
import org.meteordev.pulsar.rendering.Renderer;
import org.meteordev.pts.properties.Property;
import org.meteordev.pulsar.utils.Utils;

import static org.meteordev.pulsar.utils.Utils.combine;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class WWindow extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "window");

    private final WHeader header;
    private final WBody body;

    private boolean expandable, expanded = true;
    private double animation = 1;

    private int lastX = -1, lastY;

    public WWindow(String title) {
        this(title, true);
    }

    public WWindow(String title, boolean expandable) {
        this.expandable = expandable;

        header = super.add(new WHeader()).expandX().widget();
        header.add(new WText(title).tag("window-title"));

        body = super.add(new WBody()).expandX().widget();

        layout = VerticalLayout.INSTANCE;
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    public <T extends Widget> Cell<T> add(T widget) {
        return body.add(widget);
    }

    @Override
    public boolean remove(Widget widget) {
        return body.remove(widget);
    }

    @Override
    public void clear() {
        body.clear();
    }

    @Override
    public void afterLayout() {
        if (lastX != -1) {
            move(lastX - x, lastY - y);
        }
    }

    @Override
    public void move(int x, int y) {
        super.move(x, y);

        lastX = this.x;
        lastY = this.y;
    }

    @Override
    public void dispatch(Event event) {
        header.dispatch(event);
        if (expanded) body.dispatch(event);

        dispatchToSelf(event);
    }

    @Override
    public void render(Renderer renderer, double delta) {
        renderer.render();
        renderer.begin();

        header.tag("expanded", expanded || animation > 0);
        header.render(renderer, delta);

        if (expanded || animation > 0) {
            animation = Utils.clamp(animation + (expanded ? delta * 10 : -delta * 10), 0, 1);

            if (animation > 0) {
                double preOffsetY = renderer.offsetY;

                if (animation < 1) {
                    double height = body.height * (1 - animation);

                    renderer.beginScissor(body.x, body.y, body.width, body.height - height);
                    renderer.offsetY = -height;
                }

                body.render(renderer, delta);

                if (animation > 0 && animation < 1) {
                    renderer.endScissor();
                    renderer.offsetY = preOffsetY;
                }
            }
        }

        renderer.end();
        renderer.begin();
    }

    public <T> void headerSet(Property<T> property, T value) {
        header.set(property, value);
    }
    public <T> T headerGet(Property<T> property) {
        return header.get(property);
    }

    public <T> void bodySet(Property<T> property, T value) {
        body.set(property, value);
    }
    public <T> T bodyGet(Property<T> property) {
        return body.get(property);
    }
    public void bodySetScrollOnlyWhenHovered(boolean scrollOnlyWhenHovered) {
        body.scrollOnlyWhenHovered = scrollOnlyWhenHovered;
    }

    public class WHeader extends Widget {
        protected static final String[] NAMES = combine(Widget.NAMES, "window-header");

        private boolean dragging, moved;
        private boolean shouldToggle;

        @Override
        public String[] names() {
            return NAMES;
        }

        @Override
        protected void onMousePressed(MouseButtonEvent event) {
            shouldToggle = false;

            if (!event.used && isHovered()) {
                dragging = true;
                moved = false;
                if (event.button == GLFW_MOUSE_BUTTON_RIGHT) shouldToggle = true;

                event.use();
            }
        }

        @Override
        protected void onMouseMoved(MouseMovedEvent event) {
            if (dragging) {
                WWindow.this.move((int) event.deltaX, (int) event.deltaY);
                moved = true;
            }
        }

        @Override
        protected void onMouseReleased(MouseButtonEvent event) {
            if (shouldToggle && !moved && expandable) {
                expanded = !expanded;
                if (expanded) animation = 0;
            }

            dragging = false;
        }
    }

    public static class WBody extends WVerticalList {
        protected static final String[] SELF_NON_SCROLL_MODE_NAMES = combine(WVerticalList.SELF_NON_SCROLL_MODE_NAMES, "window-body");
        protected static final String[] SELF_SCROLL_MODE_NAMES = combine(WVerticalList.SELF_SCROLL_MODE_NAMES, "window-body");

        @Override
        protected String[] getSelfNonScrollModeNames() {
            return SELF_NON_SCROLL_MODE_NAMES;
        }

        @Override
        public String[] getSelfScrollModeNames() {
            return SELF_SCROLL_MODE_NAMES;
        }
    }
}
