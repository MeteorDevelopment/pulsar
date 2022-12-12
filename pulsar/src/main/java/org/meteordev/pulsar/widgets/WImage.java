package org.meteordev.pulsar.widgets;

import org.meteordev.pulsar.Pulsar;
import org.meteordev.pulsar.rendering.Renderer;
import org.lwjgl.opengl.GL33C;

import java.nio.ByteBuffer;

import static org.meteordev.pulsar.utils.Utils.combine;

public class WImage extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "image");

    private final int glId;

    public WImage(int glId) {
        this.glId = glId;
    }

    public WImage(ByteBuffer data, int width, int height) {
        if (data == null || width == 0 || height == 0) {
            glId = 0;
            return;
        }

        glId = GL33C.glGenTextures();
        Pulsar.BIND_TEXTURE.accept(glId);

        GL33C.glTexParameteri(GL33C.GL_TEXTURE_2D, GL33C.GL_TEXTURE_MIN_FILTER, GL33C.GL_NEAREST);
        GL33C.glTexParameteri(GL33C.GL_TEXTURE_2D, GL33C.GL_TEXTURE_MAG_FILTER, GL33C.GL_NEAREST);

        GL33C.glTexImage2D(GL33C.GL_TEXTURE_2D, 0, GL33C.GL_RGBA, width, height, 0, GL33C.GL_RGBA, GL33C.GL_UNSIGNED_BYTE, data);
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    public void render(Renderer renderer, double delta) {
        renderer.texture(x, y, width, height, glId, null);
    }
}
