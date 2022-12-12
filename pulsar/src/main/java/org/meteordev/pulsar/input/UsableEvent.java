package org.meteordev.pulsar.input;

public abstract class UsableEvent extends Event {
    public boolean used;

    public UsableEvent(EventType type) {
        super(type);
    }

    /** Sets the {@link #used} field to true. */
    public void use() {
        used = true;
    }
}
