package org.meteordev.pulsar.rendering;

import org.meteordev.juno.api.JunoProvider;
import org.meteordev.juno.api.texture.Filter;
import org.meteordev.juno.api.texture.Format;
import org.meteordev.juno.api.texture.Texture;
import org.meteordev.juno.api.texture.Wrap;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.*;

public class TextureAtlas {
    private static final int SIZE = 256;

    private final Texture texture;
    private final ByteBuffer buffer;

    private boolean dirty;
    private int x, y;
    private int rowHeight;

    public TextureAtlas() {
        texture = JunoProvider.get().createTexture(SIZE, SIZE, Format.RGBA, Filter.LINEAR, Filter.LINEAR, Wrap.CLAMP_TO_BORDER);
        buffer = memAlloc(SIZE * SIZE * 4);
    }

    public void clear() {
        dirty = false;
        x = 0;
        y = 0;
    }

    public TextureRegion add(ByteBuffer regionBuffer, int width, int height) {
        checkPos(width, height);

        for (int i = 0; i < height; i++) {
            memCopy(
                    memAddress(regionBuffer, i * width * 4),
                    memAddress(buffer, ((y + i) * SIZE + x) * 4),
                    width * 4L
            );
        }

        TextureRegion region = new TextureRegion((double) x / SIZE, (double) (y + height) / SIZE, (double) (x + width) / SIZE, (double) y / SIZE);

        dirty = true;
        x += width;

        return region;
    }

    public Texture getTexture() {
        if (dirty) {
            texture.write(buffer);
            dirty = false;
        }

        return texture;
    }

    private void checkPos(int width, int height) {
        rowHeight = Math.max(rowHeight, height);

        if (x + width >= SIZE) {
            x = 0;
            y += rowHeight;
            rowHeight = 0;
        }
    }
}
