package org.meteordev.pulsar.input;

/** Input event representing mouse wheel being scrolled. */
public class MouseScrolledEvent extends UsableEvent {
    public double value;

    public MouseScrolledEvent() {
        super(EventType.MouseScrolled);
    }

    /** Prepares this event for a dispatch. */
    public MouseScrolledEvent set(double value) {
        this.value = value;
        this.used = false;

        return this;
    }
}
