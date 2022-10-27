package meteordevelopment.pulsar.input;

/** Base class for all objects that can receive user input. */
public abstract class EventHandler {
    /** Dispatches an input event to this widget and all of its children. */
    public void dispatch(Event event) {
        switch (event.type) {
            case MousePressed -> onMousePressed((MouseButtonEvent) event);
            case MouseMoved -> onMouseMoved((MouseMovedEvent) event);
            case MouseReleased -> onMouseReleased((MouseButtonEvent) event);
            case MouseScrolled -> onMouseScrolled((MouseScrolledEvent) event);

            case KeyPressed -> onKeyPressed((KeyEvent) event);
            case KeyRepeated -> onKeyRepeated((KeyEvent) event);
            case CharTyped -> onCharTyped((CharTypedEvent) event);
        }
    }

    protected void onMousePressed(MouseButtonEvent event) {}
    protected void onMouseMoved(MouseMovedEvent event) {}
    protected void onMouseReleased(MouseButtonEvent event) {}
    protected void onMouseScrolled(MouseScrolledEvent event) {}

    protected void onKeyPressed(KeyEvent event) {}
    protected void onKeyRepeated(KeyEvent event) {}
    protected void onCharTyped(CharTypedEvent event) {}
}
