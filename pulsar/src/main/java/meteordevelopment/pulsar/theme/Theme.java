package meteordevelopment.pulsar.theme;

import meteordevelopment.pulsar.rendering.FontInfo;
import meteordevelopment.pulsar.theme.fileresolvers.IFileResolver;
import meteordevelopment.pulsar.utils.Utils;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Theme {
    public String title;
    public Collection<String> authors;

    private final Styles styles = new Styles();

    private FontInfo fontInfo;
    private IFileResolver fileResolver;

    public void dispose() {
        if (fontInfo != null) fontInfo.dispose();
    }

    public void setFontInfo(FontInfo fontInfo) {
        if (this.fontInfo != null) this.fontInfo.dispose();
        this.fontInfo = fontInfo;
    }

    public FontInfo getFontInfo() {
        return fontInfo;
    }

    public void setFileResolver(IFileResolver fileResolver) {
        this.fileResolver = fileResolver;
    }

    public void addStyle(Style style) {
        styles.add(style);
    }

    public Style computeStyle(IStylable widget) {
        return styles.compute(widget);
    }

    public ByteBuffer readFile(String path) {
        InputStream in = fileResolver.get(path);
        if (in == null) throw new RuntimeException("Failed to read file '" + fileResolver.resolvePath(path) + "'.");

        byte[] bytes = Utils.read(in);
        ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
        buffer.put(bytes).rewind();

        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return buffer;
    }
}
