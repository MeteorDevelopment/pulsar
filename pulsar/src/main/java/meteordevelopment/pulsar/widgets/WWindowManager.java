package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.Event;
import meteordevelopment.pulsar.input.UsableEvent;
import meteordevelopment.pulsar.layout.HorizontalLayout;
import meteordevelopment.pulsar.rendering.Renderer;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.pulsar.utils.Utils.combine;

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
