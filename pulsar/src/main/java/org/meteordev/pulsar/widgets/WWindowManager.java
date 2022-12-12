package org.meteordev.pulsar.widgets;

import org.meteordev.pulsar.input.Event;
import org.meteordev.pulsar.input.MouseButtonEvent;
import org.meteordev.pulsar.input.MouseMovedEvent;
import org.meteordev.pulsar.layout.HorizontalLayout;
import org.meteordev.pulsar.rendering.Renderer;

import java.util.ArrayList;
import java.util.List;

import static org.meteordev.pulsar.utils.Utils.combine;

public class WWindowManager extends Widget {
    protected static String[] NAMES = combine(Widget.NAMES, "window-manager");

    private final List<WWindow> windows = new ArrayList<>();

    public WWindowManager() {
        layout = new HorizontalLayout();
    }

    @Override
    public String[] names() {
        return NAMES;
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
        if (event instanceof MouseMovedEvent) {
            for (WWindow window : windows) {
                window.dispatch(event);
            }
        } else {

            // Find hovered with lowest z index
            WWindow lastHoveredWindow = null;
            for (int i = windows.size() - 1; i >= 0; i--) {
                WWindow window = windows.get(i);
                if (window.isHovered()) {
                    lastHoveredWindow = window;
                    break;
                }
            }

            if (lastHoveredWindow != null) {
                // Dispatch single window
                lastHoveredWindow.dispatch(event);

                // If window with lowest z index isn't the highest, make it the highest
                if (event instanceof MouseButtonEvent) {
                    if (windows.indexOf(lastHoveredWindow) != windows.size() - 1) {
                        windows.remove(lastHoveredWindow);
                        windows.add(lastHoveredWindow);
                    }
                }
            }
        }

        // Dispatch to children which are not windows
        for (Cell<?> cell : cells) {
            if (!(cell.widget() instanceof WWindow)) cell.widget().dispatch(event);
        }

        // Dispatch to self
        dispatchToSelf(event);
    }

    @Override
    public void render(Renderer renderer, double delta) {
        // Render self
        onRender(renderer, delta);

        // Render children which are not windows
        for (Cell<?> cell : cells) {
            if (!(cell.widget() instanceof WWindow)) cell.widget().render(renderer, delta);
        }

        // Render windows
        for (WWindow window : windows) window.render(renderer, delta);
    }
}
