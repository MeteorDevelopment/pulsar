package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Properties;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WButton extends WHorizontalList {
    protected static final String[] NAMES = combine(WHorizontalList.NAMES, "button");

    protected boolean pressed;

    protected WIcon iconW;

    public WButton(String text) {
        add(new WText(text).id("button-text"));
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    public void calculateSize() {
        if (iconW != null) {
            remove(iconW);
            iconW = null;
        }

        String icon = get(Properties.ICON);
        if (icon != null) {
            iconW = new WButtonIcon();
            iconW.id(icon).computeStyle(Renderer.INSTANCE.theme);

            cells.add(0, create(iconW));
        }

        super.calculateSize();
    }

    @Override
    protected boolean onMousePressed(int button, double mouseX, double mouseY, boolean used) {
        if (hovered && !used) {
            pressed = true;
            style = null;
            if (iconW != null) iconW.style = null;

            return true;
        }

        return false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY, double deltaMouseX, double deltaMouseY) {
        super.mouseMoved(mouseX, mouseY, deltaMouseX, deltaMouseY);

        if (iconW != null && iconW.hovered != hovered) {
            iconW.style = null;
            iconW.computeStyle(Renderer.INSTANCE.theme);
        }
    }

    @Override
    protected boolean onMouseReleased(int button, double mouseX, double mouseY) {
        if (pressed) {
            pressed = false;
            style = null;
            if (iconW != null) iconW.style = null;
        }

        return false;
    }

    @Override
    public String state() {
        if (pressed) return "pressed";
        return super.state();
    }

    protected class WButtonIcon extends WIcon {
        @Override
        public String state() {
            return WButton.this.state();
        }
    }
}
