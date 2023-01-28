package org.meteordev.pulsar.widgets;

import org.meteordev.juno.api.JunoProvider;
import org.meteordev.juno.api.texture.Filter;
import org.meteordev.juno.api.texture.Format;
import org.meteordev.juno.api.texture.Texture;
import org.meteordev.juno.api.texture.Wrap;
import org.meteordev.pulsar.rendering.Renderer;

import java.nio.ByteBuffer;

import static org.meteordev.pulsar.utils.Utils.combine;

public class WImage extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "image");

    private final Texture texture;

    public WImage(Texture texture) {
        this.texture = texture;
    }

    public WImage(ByteBuffer data, int width, int height) {
        if (data == null || width == 0 || height == 0) {
            texture = null;
            return;
        }

        texture = JunoProvider.get().createTexture(width, height, Format.RGBA, Filter.NEAREST, Filter.NEAREST, Wrap.CLAMP_TO_BORDER);
        texture.write(data);
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    public void render(Renderer renderer, double delta) {
        renderer.texture(x, y, width, height, texture, null);
    }
}
