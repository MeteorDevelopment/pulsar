package meteordevelopment.pulsar.input;

/** Input event representing mouse button being pressed or released. */
public class MouseButtonEvent extends UsableEvent {
    public double x, y;
    public int button;

    public MouseButtonEvent() {
        super(EventType.MousePressed);
    }

    /** Prepares this event for a dispatch. */
    public MouseButtonEvent set(EventType type, double x, double y, int button) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.button = button;
        this.used = false;

        return this;
    }
}
