package org.meteordev.pulsar.rendering;

import org.lwjgl.nanovg.NSVGImage;
import org.lwjgl.nanovg.NanoSVG;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.meteordev.juno.api.texture.Texture;
import org.meteordev.pulsar.theme.Theme;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Icons {
    private Theme theme;

    private final TextureAtlas atlas = new TextureAtlas();
    private final Map<String, Map<Integer, TextureRegion>> icons = new HashMap<>();

    public void setTheme(Theme theme) {
        this.theme = theme;

        atlas.clear();
        icons.clear();
    }

    public TextureRegion get(String path, int size) {
        Map<Integer, TextureRegion> icons = this.icons.computeIfAbsent(path, s -> new HashMap<>());

        TextureRegion region = icons.get(size);
        if (region != null) return region;

        long rast = NanoSVG.nsvgCreateRasterizer();
        if (rast == MemoryUtil.NULL)
            throw new IllegalStateException("Failed to create SVG rasterizer");
        ByteBuffer terminated = terminate(theme.readFile(path));
        NSVGImage svg = NanoSVG.nsvgParse(terminated, MemoryStack.stackASCII("px"), 96f);
        ByteBuffer image = MemoryUtil.memAlloc(size * size * 4);
        NanoSVG.nsvgRasterize(rast, svg, 0, 0, size / Math.max(svg.height(), svg.width()), image, size, size, size * 4);
        NanoSVG.nsvgDeleteRasterizer(rast);
        region = atlas.add(image, size, size);
        MemoryUtil.memFree(image);
        NanoSVG.nsvgDelete(svg);
        MemoryUtil.memFree(terminated);

        icons.put(size, region);
        return region;
    }

    public Texture getTexture() {
        return atlas.getTexture();
    }

    private ByteBuffer terminate(ByteBuffer string) {
        ByteBuffer result = MemoryUtil.memAlloc(string.capacity() + 1);
        result.clear();
        result.put(string);
        result.put((byte) 0);
        result.rewind();
        return result;
    }
}
