package meteordevelopment.pulsar.input;

/** Base class for all input events. */
public abstract class Event {
    public EventType type;

    public Event(EventType type) {
        this.type = type;
    }
}
