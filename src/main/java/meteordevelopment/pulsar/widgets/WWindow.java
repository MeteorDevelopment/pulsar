package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.MouseButtonEvent;
import meteordevelopment.pulsar.input.MouseMovedEvent;
import meteordevelopment.pulsar.layout.VerticalLayout;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WWindow extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "window");

    private final Widget body;

    private double lastX = -1, lastY;

    public WWindow(String title) {
        Widget header = super.add(new WHeader()).expandX().widget();
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

    public class WHeader extends Widget {
        protected static final String[] NAMES = combine(Widget.NAMES, "window-header");

        private boolean dragging;

        @Override
        public String[] names() {
            return NAMES;
        }

        @Override
        protected void onMousePressed(MouseButtonEvent event) {
            if (!event.used && isHovered()) {
                dragging = true;
                event.use();
            }
        }

        @Override
        protected void onMouseMoved(MouseMovedEvent event) {
            if (dragging) WWindow.this.move(event.deltaX, event.deltaY);
        }

        @Override
        protected void onMouseReleased(MouseButtonEvent event) {
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
