package org.meteordev.pulsar.theme;

import org.meteordev.pulsar.theme.fileresolvers.IFileResolver;
import org.meteordev.pulsar.utils.Utils;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collection;

public class Theme {
    public String title;
    public Collection<String> authors;

    private final Styles styles = new Styles();

    private IFileResolver fileResolver;

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
