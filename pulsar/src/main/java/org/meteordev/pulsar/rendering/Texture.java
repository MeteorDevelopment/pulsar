package org.meteordev.pulsar.rendering;

import org.meteordev.pulsar.Pulsar;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL12C.GL_UNPACK_IMAGE_HEIGHT;
import static org.lwjgl.opengl.GL12C.GL_UNPACK_SKIP_IMAGES;

public class Texture {
    public final int id;
    private final int width, height;
    private final boolean font;

    public Texture(int width, int height, ByteBuffer buffer, boolean font) {
        this.width = width;
        this.height = height;
        this.font = font;

        id = glGenTextures();
        Pulsar.BIND_TEXTURE.accept(id);

        glPixelStorei(GL_UNPACK_SWAP_BYTES, GL_FALSE);
        glPixelStorei(GL_UNPACK_LSB_FIRST, GL_FALSE);
        glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
        glPixelStorei(GL_UNPACK_IMAGE_HEIGHT, 0);
        glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
        glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
        glPixelStorei(GL_UNPACK_SKIP_IMAGES, 0);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 4);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        upload(buffer);
    }

    public void upload(ByteBuffer buffer) {
        if (buffer != null) buffer.rewind();

        Pulsar.BIND_TEXTURE.accept(id);
        glTexImage2D(GL_TEXTURE_2D, 0, font ? GL_RED : GL_RGBA, width, height, 0, font ? GL_RED : GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    }

    public Texture bind() {
        Pulsar.BIND_TEXTURE.accept(id);

        return this;
    }

    public int getSlot() {
        return 0;
    }

    public void dispose() {
        glDeleteTextures(id);
    }
}
