package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.KeyEvent;
import meteordevelopment.pulsar.input.MouseMovedEvent;
import meteordevelopment.pulsar.layout.Layout;
import meteordevelopment.pulsar.rendering.DebugRenderer;
import meteordevelopment.pulsar.rendering.Renderer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static meteordevelopment.pulsar.utils.Utils.combine;
import static org.lwjgl.glfw.GLFW.*;

/** Root for all widgets. */
public class WRoot extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "root");

    private boolean invalid = true;
    private boolean debug;

    protected int windowWidth, windowHeight;

    /** Sets the window size, not the widget size. */
    public void setWindowSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    @Override
    public void invalidateLayout() {
        invalid = true;
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
            layout.calculateSize(this, renderer);
            layout.positionChildren(this);

            invalid = false;
        }

        renderer.setup(windowWidth, windowHeight);
        super.render(renderer, delta);
        renderer.render();

        if (debug) DebugRenderer.render(this, windowWidth, windowHeight);
    }

    /** Root widget that covers the entire screen. */
    public static class FullScreen extends WRoot {
        public FullScreen() {
            layout = new Layout() {
                @Override
                protected void calculateSizeImpl(Widget widget) {
                    widget.width = windowWidth;
                    widget.height = windowHeight;
                }

                @Override
                protected void positionChildrenImpl(Widget widget) {
                    for (Cell<?> cell : widget) {
                        cell.x = widget.width / 2 - cell.widget().width / 2;
                        cell.y = widget.height / 2 - cell.widget().height / 2;

                        cell.width = cell.widget().width;
                        cell.height = cell.widget().height;

                        cell.align();
                    }
                }
            };
        }

        @Override
        public void setWindowSize(int width, int height) {
            super.setWindowSize(width, height);

            invalidateLayout();
        }
    }
}
