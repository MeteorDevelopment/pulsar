package meteordevelopment.pulsar.input;

/** Input event representing key being pressed or repeated. */
public class KeyEvent extends UsableEvent {
    public int key, mods;

    public KeyEvent() {
        super(EventType.KeyPressed);
    }

    /** Prepares this event for a dispatch. */
    public KeyEvent set(EventType type, int key, int mods) {
        this.type = type;
        this.key = key;
        this.mods = mods;
        this.used = false;

        return this;
    }
}
