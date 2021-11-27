package meteordevelopment.pulsar.widgets;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WButton extends WContainer {
    protected static final String[] NAMES = combine(WContainer.NAMES, "button");

    protected boolean pressed;

    public WButton(String text) {
        add(new WText(text).id("button-text"));
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    protected void onMousePressed(int button) {
        if (hovered) {
            pressed = true;
            style = null;
        }
    }

    @Override
    protected void onMouseReleased(int button) {
        if (pressed) {
            pressed = false;
            style = null;
        }
    }

    @Override
    public String state() {
        if (pressed) return "pressed";
        return super.state();
    }
}
