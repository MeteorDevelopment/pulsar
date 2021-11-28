package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WCheckbox extends WContainer {
    protected static final String[] NAMES = combine(WContainer.NAMES, "checkbox");

    private final WInner inner;

    public boolean checked;
    private boolean pressed;

    public WCheckbox(boolean checked) {
        this.checked = checked;

        inner = add(new WInner()).widget;
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    protected void onMousePressed(int button) {
        if (hovered) {
            pressed = true;
            checked = !checked;

            style = null;
            inner.style = null;
        }
    }

    @Override
    protected void onMouseReleased(int button) {
        if (pressed) {
            pressed = false;

            style = null;
            inner.style = null;
        }
    }

    @Override
    public void render(Renderer renderer, double mouseX, double mouseY, double delta) {
        inner.visible = checked;
        super.render(renderer, mouseX, mouseY, delta);
    }

    @Override
    public String state() {
        if (pressed) return "pressed";
        return super.state();
    }

    protected class WInner extends Widget {
        protected static final String[] NAMES = combine(Widget.NAMES, "checkbox-inner");

        @Override
        public String[] names() {
            return NAMES;
        }

        @Override
        public String state() {
            if (pressed) return "pressed";
            return super.state();
        }
    }
}
