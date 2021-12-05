package meteordevelopment.pulsar.theme;

import meteordevelopment.pulsar.rendering.FontInfo;
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

    private final Styles styles = new Styles();

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

    public void addStyle(Style style) {
        styles.add(style);
    }

    public Style computeStyle(IStylable widget) {
        return styles.compute(widget);
    }

    public void putBuffer(String name, ByteBuffer buffer) {
        buffers.put(name, buffer);
    }

    public ByteBuffer getBuffer(String name) {
        return buffers.get(name);
    }
}
