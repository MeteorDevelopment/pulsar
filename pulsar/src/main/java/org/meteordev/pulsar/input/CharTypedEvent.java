package org.meteordev.pulsar.input;

/** Input event representing character being typed. */
public class CharTypedEvent extends UsableEvent {
    public char c;

    public CharTypedEvent() {
        super(EventType.CharTyped);
    }

    /** Prepares this event for a dispatch. */
    public CharTypedEvent set(char c) {
        this.c = c;
        this.used = false;

        return this;
    }
}
