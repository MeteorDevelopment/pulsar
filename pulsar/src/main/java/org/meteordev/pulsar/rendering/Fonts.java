package org.meteordev.pulsar.rendering;

import org.meteordev.pulsar.theme.Theme;
import org.meteordev.pts.utils.Color4;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class Fonts {
    private final Theme theme;

    private final Shader shader = new Shader("/pulsar/shaders/texture.vert", "/pulsar/shaders/text.frag");
    private final Map<String, FontHolder> holders = new HashMap<>();

    public Fonts(Theme theme) {
        this.theme = theme;
    }

    public void dispose() {
        for (FontHolder holder : holders.values()) holder.dispose();
    }

    private SizedFont getFontForRender(String font, double size) {
        SizedFont sizedFont = get(font, size);

        if (!sizedFont.building) {
            sizedFont.mesh.begin();
            sizedFont.building = true;
        }

        return sizedFont;
    }

    public void render(String font, double x, double y, String text, double size, Color4 color) {
        if (font == null || font.isEmpty() || size <= 0) return;

        SizedFont sizedFont = getFontForRender(font, size);
        sizedFont.font.render(sizedFont.mesh, text, x, y, color);
    }

    public void renderChars(String font, double x, double y, char c, int count, double size, Color4 color) {
        if (font == null || font.isEmpty() || count <= 0 || size <= 0) return;

        SizedFont sizedFont = getFontForRender(font, size);
        sizedFont.font.renderChars(sizedFont.mesh, x, y, c, count, color);
    }

    public void end(FloatBuffer projection) {
        shader.bind();
        shader.set("u_Proj", projection);

        for (FontHolder holder : holders.values()) {
            for (SizedFont sizedFont : holder.fonts.values()) {
                if (!sizedFont.building) continue;
                sizedFont.building = false;

                shader.set("u_Texture", sizedFont.font.getTexture().bind());
                sizedFont.mesh.render();
            }
        }
    }

    public double textWidth(String font, String text, int length, double size) {
        if (font == null || font.isEmpty() || length == 0 || size <= 0) return 0;
        return get(font, size).font.getWidth(text, length);
    }

    public double charWidth(String font, char c, double size) {
        if (font == null || font.isEmpty() || size <= 0) return 0;
        return get(font, size).font.getCharWidth(c);
    }

    public double textHeight(String font, double size) {
        if (font == null || font.isEmpty() || size <= 0) return 0;
        return get(font, size).font.getHeight();
    }

    private SizedFont get(String font, double size) {
        return holders.computeIfAbsent(font, s -> {
            if (!s.endsWith(".ttf")) throw new RuntimeException("Font file needs to end with a '.ttf' extension.");
            return new FontHolder(theme.readFile(s));
        }).get(size);
    }

    private static class FontHolder {
        private final FontInfo info;
        private final Map<Integer, SizedFont> fonts = new HashMap<>();

        public FontHolder(ByteBuffer buffer) {
            this.info = new FontInfo(buffer);
        }

        private SizedFont get(double size) {
            return fonts.computeIfAbsent((int) size, integer -> new SizedFont(info, integer));
        }

        public void dispose() {
            info.dispose();
            for (SizedFont sizedFont : fonts.values()) sizedFont.dispose();
        }
    }

    private static class SizedFont {
        public final Font font;
        public final Mesh mesh;

        public boolean building;

        public SizedFont(FontInfo info, double size) {
            font = new Font(info, (int) size);
            mesh = new Mesh(Mesh.Attrib.Vec2, Mesh.Attrib.Vec2, Mesh.Attrib.Color);
        }

        public void dispose() {
            font.dispose();
            mesh.dispose();
        }
    }
}
