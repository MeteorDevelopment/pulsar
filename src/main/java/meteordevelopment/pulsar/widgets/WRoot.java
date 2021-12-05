package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.KeyEvent;
import meteordevelopment.pulsar.input.MouseMovedEvent;
import meteordevelopment.pulsar.layout.Layout;
import meteordevelopment.pulsar.rendering.DebugRenderer;
import meteordevelopment.pulsar.rendering.Renderer;

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
    }

    @Override
    public void render(Renderer renderer, double delta) {
        if (invalid) {
            layout.calculateSize(this, renderer);
            layout.positionChildren(this);

            invalid = false;
        }

        renderer.begin(windowWidth, windowHeight);
        super.render(renderer, delta);
        renderer.end();

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
