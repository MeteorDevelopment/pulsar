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
    protected boolean onMousePressed(int button, double mouseX, double mouseY, boolean used) {
        if (hovered && !used) {
            pressed = true;
            style = null;

            return true;
        }

        return false;
    }

    @Override
    protected boolean onMouseReleased(int button, double mouseX, double mouseY) {
        if (pressed) {
            pressed = false;
            style = null;
        }

        return false;
    }

    @Override
    public String state() {
        if (pressed) return "pressed";
        return super.state();
    }
}
