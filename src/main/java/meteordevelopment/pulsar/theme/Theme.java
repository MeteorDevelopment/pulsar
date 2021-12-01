package meteordevelopment.pulsar.theme;

import meteordevelopment.pulsar.rendering.FontInfo;
import meteordevelopment.pulsar.widgets.Widget;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Theme {
    private static class WidgetNode {
        public final Map<String, Style> stateStyles = new HashMap<>();
        public Style style;
    }

    public final String title;
    public final Collection<String> authors;

    private final Map<String, WidgetNode> widgetStyles = new HashMap<>();
    private final Map<String, Style> idStyles = new HashMap<>();

    private FontInfo fontInfo;
    private final Map<String, ByteBuffer> buffers = new HashMap<>();

    public Theme(String title, Collection<String> authors) {
        this.title = title;
        this.authors = authors;
    }

    public void dispose() {
        if (fontInfo != null) fontInfo.dispose();
        for (ByteBuffer buffer : buffers.values()) MemoryUtil.memFree(buffer);
    }

    public void setFontInfo(FontInfo fontInfo) {
        if (this.fontInfo != null) this.fontInfo.dispose();
        this.fontInfo = fontInfo;
    }

    public FontInfo getFontInfo() {
        return fontInfo;
    }

    public void addWidgetStyle(String widget, Style style) {
        getWidget(widget, true).style = style;
    }

    public void addIdStyle(String name, Style style) {
        idStyles.put(name, style);
    }

    public void addStateStyle(String widget, String state, Style style) {
        getWidget(widget, true).stateStyles.put(state, style);
    }

    public Style computeStyle(Widget widget) {
        Style style = new Style();

        // Apply widget styles
        for (String name : widget.names()) {
            WidgetNode node = getWidget(name, false);
            if (node != null && node.style != null) style.merge(node.style);
        }

        // Apply id style
        if (widget.id() != null) {
            Style idStyle = idStyles.get(widget.id());
            if (idStyle != null) style.merge(idStyle);
        }

        // Apply state style
        if (widget.state() != null) {
            WidgetNode node = getWidget(widget.names()[widget.names().length - 1], false);

            if (node != null) {
                Style stateStyle = node.stateStyles.get(widget.state());
                if (stateStyle != null) style.merge(stateStyle);
            }
        }

        return style;
    }

    public void putBuffer(String name, ByteBuffer buffer) {
        buffers.put(name, buffer);
    }

    public ByteBuffer getBuffer(String name) {
        return buffers.get(name);
    }

    private WidgetNode getWidget(String name, boolean create) {
        if (create) return widgetStyles.computeIfAbsent(name, s -> new WidgetNode());
        return widgetStyles.get(name);
    }
}
