package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.Event;
import meteordevelopment.pulsar.input.KeyEvent;
import meteordevelopment.pulsar.input.MouseMovedEvent;
import meteordevelopment.pulsar.input.UsableEvent;
import meteordevelopment.pulsar.layout.Layout;
import meteordevelopment.pulsar.rendering.DebugRenderer;
import meteordevelopment.pulsar.rendering.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static meteordevelopment.pulsar.utils.Utils.combine;
import static org.lwjgl.glfw.GLFW.*;

public class WRoot extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "root");

    private final List<WWindow> windows = new ArrayList<>();

    private boolean invalid = true;
    private boolean debug;

    private int windowWidth, windowHeight;

    @Override
    public String[] names() {
        return NAMES;
    }

    public void setWindowSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;

        invalidateLayout();
    }

    @Override
    public void invalidateLayout() {
        invalid = true;
    }

    @Override
    public <T extends Widget> Cell<T> add(T widget) {
        if (widget instanceof WWindow window) windows.add(window);
        return super.add(widget);
    }

    @Override
    public boolean remove(Widget widget) {
        if (widget instanceof WWindow window) windows.remove(window);
        return super.remove(widget);
    }

    @Override
    public void clear() {
        windows.clear();
        super.clear();
    }

    @Override
    public void dispatch(Event event) {
        // Dispatch to windows and reorder them accordingly
        UsableEvent usableEvent = event instanceof UsableEvent ? (UsableEvent) event : null;
        boolean wasUsed = false;
        int firstWindow = -1;

        for (int i = windows.size() - 1; i >= 0; i--) {
            WWindow window = windows.get(i);
            window.dispatch(event);

            if (usableEvent != null && !wasUsed && usableEvent.used) {
                wasUsed = true;
                firstWindow = i;
            }
        }

        if (firstWindow != -1) {
            int i = windows.size() - 1;
            WWindow temp = windows.get(i);

            windows.set(i, windows.get(firstWindow));
            windows.set(firstWindow, temp);
        }

        // Dispatch to children which are not windows
        for (Cell<?> cell : cells) {
            if (!(cell.widget() instanceof WWindow)) cell.widget().dispatch(event);
        }

        // Dispatch to self
        dispatchToSelf(event);
    }

    @Override
    protected void onMouseMoved(MouseMovedEvent event) {
        Renderer.INSTANCE.mouseX = event.x;
        Renderer.INSTANCE.mouseY = event.y;
    }

    @Override
    protected void onKeyPressed(KeyEvent event) {
        if (!event.used && (event.mods == GLFW_MOD_CONTROL || event.mods == GLFW_MOD_SUPER) && event.key == GLFW_KEY_9) {
            debug = !debug;
            event.use();
        }
        else if (!event.used && event.key == GLFW_KEY_TAB) {
            AtomicReference<WTextBox> firstTextBox = new AtomicReference<>(null);
            AtomicBoolean done = new AtomicBoolean(false);
            AtomicBoolean foundFocused = new AtomicBoolean(false);

            loopWidgets(this, wWidget -> {
                if (done.get() || !(wWidget instanceof WTextBox textBox)) return;

                if (foundFocused.get()) {
                    textBox.setFocused(true);
                    textBox.setCursorMax();

                    done.set(true);
                }
                else {
                    if (textBox.isFocused()) {
                        textBox.setFocused(false);
                        foundFocused.set(true);
                    }
                }

                if (firstTextBox.get() == null) firstTextBox.set(textBox);
            });

            if (!done.get() && firstTextBox.get() != null) {
                firstTextBox.get().setFocused(true);
                firstTextBox.get().setCursorMax();
            }

            event.use();
        }
    }

    private void loopWidgets(Widget widget, Consumer<Widget> action) {
        action.accept(widget);
        for (Cell<?> cell : widget) loopWidgets(cell.widget(), action);
    }

    @Override
    public void render(Renderer renderer, double delta) {
        if (invalid) {
            if (!(layout instanceof RootLayout)) layout = new RootLayout(layout);

            layout.calculateSize(this);
            layout.positionChildren(this);

            invalid = false;
        }

        renderer.setup(windowWidth, windowHeight);

        // Render self
        onRender(renderer, delta);

        // Render children which are not windows
        for (Cell<?> cell : cells) {
            if (!(cell.widget() instanceof WWindow)) cell.widget().render(renderer, delta);
        }

        // Render windows
        for (WWindow window : windows) window.render(renderer, delta);

        renderer.render();

        if (debug) DebugRenderer.render(this, windowWidth, windowHeight);
    }

    private class RootLayout extends Layout {
        private final Layout layout;

        public RootLayout(Layout layout) {
            this.layout = layout;
        }

        @Override
        public void calculateSize(Widget widget) {
            layout.calculateSize(widget);

            if (widget == WRoot.this) {
                widget.width = windowWidth;
                widget.height = windowHeight;
            }
        }

        @Override
        public void positionChildren(Widget widget) {
            layout.positionChildren(widget);
        }

        @Override
        public void onAdd(Widget widget, Cell<?> cell) {
            layout.onAdd(widget, cell);
        }

        @Override
        public void onClear(Widget widget) {
            super.onClear(widget);
        }

        @Override
        protected void calculateSizeImpl(Widget widget) {}

        @Override
        protected void positionChildrenImpl(Widget widget) {}
    }
}
