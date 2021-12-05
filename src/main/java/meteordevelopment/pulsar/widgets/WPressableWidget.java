package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.MouseButtonEvent;

/** Base class for all widgets that can be pressed. */
public abstract class WPressableWidget extends Widget {
    private boolean pressed;

    @Override
    protected void onMousePressed(MouseButtonEvent event) {
        if (isHovered() && !event.used) {
            pressed = true;
            invalidStyle();

            event.use();
        }
    }

    @Override
    protected void onMouseReleased(MouseButtonEvent event) {
        if (pressed) {
            doAction();

            pressed = false;
            invalidStyle();
        }
    }

    protected void doAction() {}

    @Override
    public boolean isPressed() {
        return pressed;
    }
}
