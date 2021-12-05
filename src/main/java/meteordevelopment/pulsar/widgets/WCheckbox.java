package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WCheckbox extends WPressableWidget {
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

        @Override
        public String[] names() {
            return NAMES;
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
            if (checked) super.render(renderer, delta);
        }
    }
}
