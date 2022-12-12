package org.meteordev.pulsar.rendering;

import org.meteordev.pts.utils.Color4;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Font {
    private final Texture texture;
    private final int height;
    private final float scale;
    private final float ascent;
    private final CharData[] charData;

    public Font(FontInfo info, int height) {
        this.height = height;

        // Allocate STBTTPackedchar buffer
        charData = new CharData[128];
        STBTTPackedchar.Buffer cdata = STBTTPackedchar.create(charData.length);
        ByteBuffer bitmap = BufferUtils.createByteBuffer(2048 * 2048);

        // Create font texture
        STBTTPackContext packContext = STBTTPackContext.create();
        STBTruetype.stbtt_PackBegin(packContext, bitmap, 2048, 2048, 0, 1);
        STBTruetype.stbtt_PackSetOversampling(packContext, 2, 2);
        STBTruetype.stbtt_PackFontRange(packContext, info.buffer, 0, height, 32, cdata);
        STBTruetype.stbtt_PackEnd(packContext);
        info.buffer.rewind();

        // Create texture object and get font scale
        texture = new Texture(2048, 2048, bitmap, true);
        scale = STBTruetype.stbtt_ScaleForPixelHeight(info.fontInfo, height);

        // Get font vertical ascent
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer ascent = stack.mallocInt(1);
            STBTruetype.stbtt_GetFontVMetrics(info.fontInfo, ascent, null, null);
            this.ascent = ascent.get(0);
        }

        // Populate charData array
        for (int i = 0; i < charData.length; i++) {
            STBTTPackedchar packedChar = cdata.get(i);

            float ipw = 1f / 2048;
            float iph = 1f / 2048;

            charData[i] = new CharData(
                    packedChar.xoff(),
                    packedChar.yoff(),
                    packedChar.xoff2(),
                    packedChar.yoff2(),
                    packedChar.x0() * ipw,
                    packedChar.y0() * iph,
                    packedChar.x1() * ipw,
                    packedChar.y1() * iph,
                    packedChar.xadvance()
            );
        }
    }

    public void dispose() {
        texture.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public double getWidth(String string, int length) {
        double width = 0;

        for (int i = 0; i < length; i++) {
            int cp = string.charAt(i);
            if (cp < 32 || cp > 128) cp = 32;
            CharData c = charData[cp - 32];

            width += c.xAdvance();
        }

        return width;
    }

    public double getCharWidth(char c) {
        CharData data = charData[c - 32];
        return data == null ? 0 : data.xAdvance();
    }

    public double getHeight() {
        return height;
    }

    public double render(Mesh mesh, String string, double x, double y, Color4 color) {
        y += ascent * scale + 1;

        for (int i = 0; i < string.length(); i++) {
            int cp = string.charAt(i);
            if (cp < 32 || cp > 128) cp = 32;
            CharData c = charData[cp - 32];

            renderChar(mesh, x, y, c, color);

            x += c.xAdvance();
        }

        return x;
    }

    public double renderChars(Mesh mesh, double x, double y, char c, int count, Color4 color) {
        y += ascent * scale + 1;

        CharData data = charData[c - 32];
        if (data == null) return x;

        for (int i = 0; i < count; i++) {
            renderChar(mesh, x, y, data, color);

            x += data.xAdvance();
        }

        return x;
    }

    private void renderChar(Mesh mesh, double x, double y, CharData c, Color4 color) {
        mesh.quad(
                mesh.vec2(x + c.x0(), y + c.y0()).vec2(c.u0(), c.v0()).color(color.bottomLeft).next(),
                mesh.vec2(x + c.x0(), y + c.y1()).vec2(c.u0(), c.v1()).color(color.topLeft).next(),
                mesh.vec2(x + c.x1(), y + c.y1()).vec2(c.u1(), c.v1()).color(color.topRight).next(),
                mesh.vec2(x + c.x1(), y + c.y0()).vec2(c.u1(), c.v0()).color(color.bottomRight).next()
        );
    }
}
