package meteordevelopment.pulsar.widgets;

import meteordevelopment.pts.properties.Properties;
import meteordevelopment.pulsar.input.KeyEvent;
import meteordevelopment.pulsar.input.MouseMovedEvent;
import meteordevelopment.pulsar.layout.Layout;
import meteordevelopment.pulsar.layout.MaxSizeCalculationContext;
import meteordevelopment.pulsar.layout.VerticalLayout;
import meteordevelopment.pulsar.rendering.DebugRenderer;
import meteordevelopment.pulsar.rendering.Renderer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static meteordevelopment.pulsar.utils.Utils.combine;
import static org.lwjgl.glfw.GLFW.*;

public class WRoot extends Widget {
    protected static final String[] NAMES = {};

    private final boolean setMaxSize;
    private final WContainer container;

    private boolean invalid = true;
    private boolean debug;

    private int windowWidth, windowHeight;

    @Override
    public String[] names() {
        return NAMES;
    }

    public WRoot(boolean setMaxSize) {
        this.setMaxSize = setMaxSize;

        container = super.add(new WRootContainer()).expandX().widget();
        container.layout = VerticalLayout.INSTANCE;
    }

    public void setWindowSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;

        if (setMaxSize) {
            container.set(Properties.MAX_WIDTH, (double) width);
            container.set(Properties.MAX_HEIGHT, (double) height);
        }

        invalidateLayout();
    }

    @Override
    public void invalidateLayout() {
        invalid = true;
    }

    @Override
    public <T extends Widget> Cell<T> add(T widget) {
        return container.add(widget);
    }

    @Override
    public boolean remove(Widget widget) {
        return container.remove(widget);
    }

    @Override
    public void clear() {
        container.clear();
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
            invalid = false;
            if (!(layout instanceof RootLayout)) layout = new RootLayout(layout);

            layout.calculateSize(this);
            layout.positionChildren(this);

            MaxSizeCalculationContext ctx = new MaxSizeCalculationContext();
            layout.adjustToMaxSize(ctx, this);

            if (ctx.wasAdjusted()) {
                layout.calculateSize(this);
                layout.positionChildren(this);

                invalid = false;
            }

            layout.afterLayout(this);

            if (invalid) System.err.println("[Pulsar] Invalid was set again when calculating layout. This should not happen.");
        }

        renderer.setup(windowWidth, windowHeight);
        super.render(renderer, delta);
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

    private static class WRootContainer extends WContainer {
        protected static final String[] NAMES = combine(WContainer.NAMES, "root");

        @Override
        protected String[] getSelfNonScrollModeNames() {
            return NAMES;
        }
    }
}
