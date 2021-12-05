package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.MouseMovedEvent;
import meteordevelopment.pulsar.layout.HorizontalLayout;
import meteordevelopment.pulsar.theme.Properties;

import static meteordevelopment.pulsar.utils.Utils.combine;

/** Button widget which can contain text, icon or both. */
public class WButton extends WPressableWidget {
    protected static final String[] NAMES = combine(Widget.NAMES, "button");

    public Runnable action;

    private WText textW;
    private WIcon iconW;

    public WButton(String text) {
        String icon = get(Properties.ICON);
        if (icon != null) {
            iconW = add(new WButtonIcon()).widget();
            iconW.tag(icon);

            layout = new HorizontalLayout();
        }

        this.textW = add(new WText(text)).widget();
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    public void invalidStyle() {
        super.invalidStyle();
        if (iconW != null) iconW.invalidStyle();
    }

    @Override
    protected void doAction() {
        if (action != null) action.run();
    }

    public void setText(String text) {
        if (textW == null) textW = add(new WText(text)).widget();
        else textW.setText(text);
    }

    protected class WButtonIcon extends WIcon {
        protected static final String[] NAMES = combine(WIcon.NAMES, "button-icon");

        @Override
        public String[] names() {
            return NAMES;
        }

        @Override
        protected void detectHovered(MouseMovedEvent event) {}

        @Override
        public boolean isHovered() {
            return WButton.this.isHovered();
        }

        @Override
        public boolean isPressed() {
            return WButton.this.isPressed();
        }
    }
}
