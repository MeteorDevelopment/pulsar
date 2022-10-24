package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.properties.Properties;
import meteordevelopment.pulsar.utils.Color4;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WCheckbox extends WPressable {
    protected static final String[] NAMES = combine(Widget.NAMES, "checkbox");

    public Runnable action;
    public boolean checked;

    private final Widget inner;

    public WCheckbox(boolean checked) {
        this.checked = checked;

        inner = add(new WInner()).widget();
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    public void invalidStyle() {
        super.invalidStyle();
        inner.invalidStyle();
    }

    @Override
    protected void doAction() {
        checked = !checked;
        if (action != null) action.run();
    }

    protected class WInner extends Widget {
        protected static final String[] NAMES = combine(Widget.NAMES, "checkbox-inner");
        protected static final String[] ICON_NAMES = combine(WIcon.NAMES, "checkbox-inner");

        private final boolean isIcon;

        public WInner() {
            isIcon = get(Properties.ICON_PATH) != null;
        }

        @Override
        public String[] names() {
            if (isIcon) return ICON_NAMES;
            return NAMES;
        }

        @Override
        public void calculateSize() {
            super.calculateSize();

            if (isIcon) width = height = Math.max(width, height);
        }

        @Override
        public boolean isHovered() {
            return WCheckbox.this.isHovered();
        }

        @Override
        public boolean isPressed() {
            return WCheckbox.this.isPressed();
        }

        @Override
        public void render(Renderer renderer, double delta) {
            if (checked) {
                if (isIcon) {
                    String path = get(Properties.ICON_PATH);
                    Color4 color = get(Properties.COLOR);

                    if (path != null && color != null) renderer.icon(x, y, path, width, color);
                }
                else super.render(renderer, delta);
            }
        }
    }
}
