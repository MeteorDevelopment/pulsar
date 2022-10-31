package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.MouseButtonEvent;
import meteordevelopment.pulsar.input.MouseMovedEvent;
import meteordevelopment.pulsar.input.MouseScrolledEvent;
import meteordevelopment.pulsar.layout.BasicLayout;
import meteordevelopment.pulsar.layout.HorizontalLayout;
import meteordevelopment.pulsar.layout.Layout;
import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pts.properties.Properties;
import meteordevelopment.pts.utils.AlignY;
import meteordevelopment.pulsar.utils.Lists;
import meteordevelopment.pulsar.utils.Utils;
import meteordevelopment.pts.utils.Vec4;

import java.util.List;

import static meteordevelopment.pulsar.utils.Utils.combine;

/** Base class for all containers with support for vertical scrolling. By default, uses {@link meteordevelopment.pulsar.layout.BasicLayout}. */
public class WContainer extends Widget {
    protected static final String[] SELF_NON_SCROLL_MODE_NAMES = combine(Widget.NAMES, "container");
    protected static final String[] SELF_SCROLL_MODE_NAMES = { "scrollbar-container" };
    protected static final String[] CONTENTS_NON_SCROLL_MODE_NAMES = combine(Widget.NAMES, "container");

    public boolean scrollOnlyWhenHovered;

    public boolean scrollMode;
    private int realHeight, scroll, targetScroll;

    private WContents contents;
    private WScrollbar scrollbar;

    public WContainer(boolean scrollOnlyWhenHovered) {
        this.scrollOnlyWhenHovered = scrollOnlyWhenHovered;
    }

    public WContainer() {
        this(true);
    }

    protected String[] getSelfNonScrollModeNames() {
        return SELF_NON_SCROLL_MODE_NAMES;
    }

    protected String[] getSelfScrollModeNames() {
        return SELF_SCROLL_MODE_NAMES;
    }

    protected String[] getContentsScrollModeNames() {
        return CONTENTS_NON_SCROLL_MODE_NAMES;
    }

    @Override
    public String[] names() {
        return scrollMode ? getSelfScrollModeNames() : getSelfNonScrollModeNames();
    }

    @Override
    public List<String> tags() {
        return scrollMode ? Lists.of() : tags;
    }

    // region Overwrite children handling methods

    @Override
    public <T extends Widget> Cell<T> add(T widget) {
        return scrollMode ? contents.add(widget) : super.add(widget);
    }

    @Override
    public boolean remove(Widget widget) {
        return scrollMode ? contents.remove(widget) : super.remove(widget);
    }

    @Override
    public void clear() {
        if (scrollMode) contents.clear();
        else super.clear();
    }

    // endregion
    // region Entering and exiting scroll mode

    @Override
    public void afterLayout() {
        int maxHeight = get(Properties.MAX_HEIGHT).intValue();
        if (maxHeight <= 0) return;

        boolean preScrollMode = scrollMode;

        if (height > maxHeight && !scrollMode) enterScrollMode();
        else if (height < maxHeight && scrollMode) exitScrollMode();

        if (preScrollMode && scrollMode) {
            clampScroll();
            contents.move(0, -scroll);
        }
    }

    private void enterScrollMode() {
        scrollMode = true;
        realHeight = height;
        scroll = 0;

        contents = new WContents();
        contents.set(Properties.ALIGN_Y, AlignY.Top);
        transferCellsAndLayout(this, contents, new ScrollModeLayout());
        super.add(contents).expandX();

        scrollbar = super.add(new WScrollbar()).widget();

        height = get(Properties.MAX_HEIGHT).intValue();

        invalidateLayout();
        invalidStyle();
    }

    private void exitScrollMode() {
        scrollMode = false;
        scroll = 0;

        transferCellsAndLayout(contents, this, BasicLayout.INSTANCE);
        contents = null;

        scrollbar = null;

        invalidateLayout();
        invalidStyle();
    }

    private void transferCellsAndLayout(Widget from, Widget to, Layout fromNewLayout) {
        to.cells.clear();

        for (Cell<?> cell : from.cells) {
            to.cells.add(cell);
            cell.widget().parent = to;
        }

        from.cells.clear();

        to.layout = from.layout;
        from.layout = fromNewLayout;
    }

    // endregion
    // region Helper methods

    private int getMaxScroll() {
        return realHeight - height;
    }

    private void clampScroll() {
        scroll = Utils.clamp(scroll, 0, getMaxScroll());
        targetScroll = Utils.clamp(targetScroll, 0, getMaxScroll());
    }

    private double getScrollProgress() {
        return (double) scroll / getMaxScroll();
    }

    private int getScrollbarHeight() {
        return height - get(Properties.PADDING).vertical() - scrollbar.get(Properties.PADDING).vertical();
    }

    private int getScrollbarHeightMinusHandle() {
        return getScrollbarHeight() - scrollbar.handle.height;
    }

    private int getScrollbarBaseY() {
        return y + get(Properties.PADDING).bottom() + scrollbar.get(Properties.PADDING).bottom();
    }

    // endregion
    // region Input handlers

    @Override
    protected void onMouseScrolled(MouseScrolledEvent event) {
        if (scrollOnlyWhenHovered && !isHovered()) return;

        targetScroll -= event.value * 50;
        clampScroll();
    }

    // endregion
    // region Rendering

    @Override
    public void render(Renderer renderer, double delta) {
        if (scrollMode) {
            // Smooth scrolling
            int scrollDifference = targetScroll - scroll;

            if (scrollDifference != 0) {
                int preScroll = scroll;

                int scrollDelta = (int) Math.round(delta * 300 + delta * 100 * (Math.abs(scrollDifference) / 10.0));
                if (scrollDifference > 0) {
                    scroll += scrollDelta;
                    if (scroll > targetScroll) scroll = targetScroll;
                }
                else {
                    scroll -= scrollDelta;
                    if (scroll < targetScroll) scroll = targetScroll;
                }

                clampScroll();
                if (scroll - preScroll != 0) contents.move(0, preScroll - scroll);
            }

            // Calculate position and size of scrollbar handle
            scrollbar.handle.height = scrollbar.handle.widget().height = (int) Math.round((double) height / realHeight * getScrollbarHeight());
            scrollbar.handle.y = scrollbar.handle.widget().y = getScrollbarBaseY() + (int) Math.round(getScrollProgress() * getScrollbarHeightMinusHandle());

            // Render self
            onRender(renderer, delta);

            // Render contents
            Vec4 padding = get(Properties.PADDING);

            renderer.beginScissor(x + padding.left(), y + padding.bottom(), width - padding.horizontal(), height - padding.vertical());
            contents.render(renderer, delta);
            renderer.endScissor();

            // Render scrollbar
            scrollbar.render(renderer, delta);
        }
        else {
            super.render(renderer, delta);
        }
    }

    // endregion
    // region Needed classes

    private class ScrollModeLayout extends HorizontalLayout {
        @Override
        protected void calculateSizeImpl(Widget widget) {
            super.calculateSizeImpl(widget);

            int maxHeight = get(Properties.MAX_HEIGHT).intValue();

            if (widget.height > maxHeight) {
                realHeight = widget.height;
                widget.height = maxHeight;
            }
        }
    }

    private class WContents extends Widget {
        public WContents() {
            tags.add("has-scrollbar");
            invalidStyle();
        }

        @Override
        public String[] names() {
            return getContentsScrollModeNames();
        }

        @Override
        public List<String> tags() {
            return WContainer.this.tags;
        }
    }

    private class WScrollbar extends Widget {
        protected static final String[] NAMES = combine(WContainer.NAMES, "scrollbar");

        private final Cell<Widget> handle;

        public WScrollbar() {
            handle = add(new WHandle());
        }

        @Override
        public String[] names() {
            return NAMES;
        }
    }

    private class WHandle extends Widget {
        protected static final String[] NAMES = combine(Widget.NAMES, "scrollbar-handle");

        private boolean dragging;
        private double dragged;

        @Override
        public String[] names() {
            return NAMES;
        }

        @Override
        protected void onMousePressed(MouseButtonEvent event) {
            if (isHovered()) {
                dragging = true;
                dragged = 0;
            }
        }

        @Override
        protected void onMouseReleased(MouseButtonEvent event) {
            dragging = false;
        }

        @Override
        protected void onMouseMoved(MouseMovedEvent event) {
            if (!dragging) return;

            dragged += event.deltaY * ((double) realHeight / getScrollbarHeight());
            int scrolled = (int) dragged;

            if (scrolled != 0) {
                int preScroll = scroll;

                scroll += scrolled;
                targetScroll += scrolled;
                clampScroll();

                if (scroll != preScroll) {
                    dragged -= scroll - preScroll;
                    contents.move(0, preScroll - scroll);
                }
            }
        }
    }

    // endregion
}
