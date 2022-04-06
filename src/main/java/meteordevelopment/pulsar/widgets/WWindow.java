package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.Event;
import meteordevelopment.pulsar.input.EventType;
import meteordevelopment.pulsar.input.MouseButtonEvent;
import meteordevelopment.pulsar.input.MouseMovedEvent;
import meteordevelopment.pulsar.layout.VerticalLayout;
import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.utils.Utils;

import static meteordevelopment.pulsar.utils.Utils.combine;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class WWindow extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "window");

    private final Widget header, body;
    private boolean expanded = true;
    private double animation = 1;

    private int lastX = -1, lastY;

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
    public void move(int x, int y) {
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
        renderer.render();
        renderer.begin();

        header.render(renderer, delta);

        if (expanded || animation > 0) {
            animation = Utils.clamp(animation + (expanded ? delta * 10 : -delta * 10), 0, 1);

            if (animation > 0) {
                if (animation < 1) {
                    renderer.offsetY = body.height * (1 - animation);
                    renderer.beginScissor(body.x, body.y, body.width, body.height);
                }

                body.render(renderer, delta);

                if (animation > 0 && animation < 1) {
                    renderer.endScissor();
                    renderer.offsetY = 0;
                }
            }
        }

        renderer.end();
        renderer.begin();
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
            if (shouldToggle && !moved) {
                expanded = !expanded;
                if (expanded) animation = 0;
            }

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
