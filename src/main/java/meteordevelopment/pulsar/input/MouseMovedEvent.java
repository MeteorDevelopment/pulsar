package meteordevelopment.pulsar.input;

/** Input event representing mouse being moved. */
public class MouseMovedEvent extends Event {
    public double x, y;
    public double deltaX, deltaY;

    public MouseMovedEvent() {
        super(EventType.MouseMoved);
    }

    /** Prepares this event for a dispatch. */
    public MouseMovedEvent set(double x, double y, double deltaX, double deltaY) {
        this.x = x;
        this.y = y;
        this.deltaX = deltaX;
        this.deltaY = deltaY;

        return this;
    }
}
