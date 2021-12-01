package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.utils.Cell;
import meteordevelopment.pulsar.utils.Color4;
import meteordevelopment.pulsar.utils.IColor;
import meteordevelopment.pulsar.utils.Vec4;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WWindow extends WVerticalList {
    protected static final String[] NAMES = combine(WVerticalList.NAMES, "window");

    private final WContainer body;

    public WWindow(String title) {
        // Header
        WContainer header = super.add(new WHeader()).expandX().widget;
        header.add(new WText(title).id("window-title"));

        // Body
        body = super.add(new WVerticalList().id("window-body")).expandX().widget;
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
    public void remove(Widget widget) {
        body.remove(widget);
    }

    @Override
    public void clear() {
        body.clear();
    }

    @Override
    public void render(Renderer renderer, double mouseX, double mouseY, double delta) {
        Vec4 radius = get(Properties.RADIUS);
        double outlineSize = get(Properties.OUTLINE_SIZE);
        Color4 backgroundColor = get(Properties.BACKGROUND_COLOR);
        Color4 outlineColor = get(Properties.OUTLINE_COLOR);

        if (backgroundColor != null) renderer.quad(x, y, width, height, radius, 0, backgroundColor, null);

        super.render(renderer, mouseX, mouseY, delta);

        if (outlineSize > 0) renderer.quad(x, y, width, height, radius, outlineSize, null, outlineColor);
    }

    @Override
    protected void onRender(Renderer renderer, double mouseX, double mouseY, double delta) {}

    public class WHeader extends WContainer {
        protected static final String[] NAMES = combine(WContainer.NAMES, "window-header");

        protected boolean dragging;

        @Override
        public String[] names() {
            return NAMES;
        }

        @Override
        protected boolean onMousePressed(int button, double mouseX, double mouseY, boolean used) {
            if (hovered && !used) {
                dragging = true;

                return true;
            }

            return false;
        }

        @Override
        protected boolean onMouseReleased(int button, double mouseX, double mouseY) {
            dragging = false;
            return false;
        }

        @Override
        protected void onMouseMoved(double mouseX, double mouseY, double deltaMouseX, double deltaMouseY) {
            if (dragging) WWindow.this.move(deltaMouseX, deltaMouseY);
        }
    }
}
