package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.MouseMovedEvent;
import meteordevelopment.pulsar.layout.HorizontalLayout;
import meteordevelopment.pulsar.theme.Properties;

import static meteordevelopment.pulsar.utils.Utils.combine;

/** Button widget which can contain text, icon or both. */
public class WButton extends WPressable {
    protected static final String[] NAMES = combine(Widget.NAMES, "button");

    public Runnable action;

    private WText textW;
    private WIcon iconW;

    public WButton(String text) {
        checkIcon();

        this.textW = add(new WButtonText(text)).expandCellX().widget();
    }

    // TODO: Bad
    public void checkIcon() {
        if (iconW != null) {
            remove(iconW);
            iconW = null;
        }

        String icon = get(Properties.ICON);
        if (icon != null && !icon.equals("none")) {
            iconW = new WButtonIcon();
            iconW.tag(icon);

            cells.add(0, create(iconW));
            layout = new HorizontalLayout();
        }
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

    @Override
    public Widget tag(String tag) {
        textW.tag(tag);
        if (iconW != null) iconW.tag(tag);
        return super.tag(tag);
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

    protected static class WButtonText extends WText {
        protected static final String[] NAMES = combine(WText.NAMES, "button-text");

        public WButtonText(String text) {
            super(text);
        }

        @Override
        public String[] names() {
            return NAMES;
        }
    }
}
