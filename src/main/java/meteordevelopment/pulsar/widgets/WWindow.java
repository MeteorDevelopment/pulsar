package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.Event;
import meteordevelopment.pulsar.input.EventType;
import meteordevelopment.pulsar.input.MouseButtonEvent;
import meteordevelopment.pulsar.input.MouseMovedEvent;
import meteordevelopment.pulsar.layout.VerticalLayout;
import meteordevelopment.pulsar.rendering.Renderer;

import static meteordevelopment.pulsar.utils.Utils.combine;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class WWindow extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "window");

    private final Widget header, body;
    private boolean expanded = true;

    private double lastX = -1, lastY;

    public WWindow(String title) {
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
    public void move(double x, double y) {
        super.move(x, y);

        lastX = this.x;
        lastY = this.y;
    }

    @Override
    public void dispatch(Event event) {
        header.dispatch(event);
        if (expanded) body.dispatch(event);

        if (event.type == EventType.MouseMoved) detectHovered((MouseMovedEvent) event);
    }

    @Override
    public void render(Renderer renderer, double delta) {
        header.render(renderer, delta);
        if (expanded) body.render(renderer, delta);
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
                WWindow.this.move(event.deltaX, event.deltaY);
                moved = true;
            }
        }

        @Override
        protected void onMouseReleased(MouseButtonEvent event) {
            if (shouldToggle && !moved)
                expanded = !expanded;

            dragging = false;
        }
    }

    public static class WBody extends WVerticalList {
        protected static final String[] NAMES = combine(WVerticalList.NAMES, "window-body");

        @Override
        public String[] names() {
            return NAMES;
        }
    }
}
