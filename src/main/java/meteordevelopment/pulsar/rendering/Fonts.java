package meteordevelopment.pulsar.rendering;

import meteordevelopment.pulsar.utils.Color4;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

public class Fonts {
    private final FontInfo fontInfo;

    private final Shader shader = new Shader("/pulsar/shaders/text.vert", "/pulsar/shaders/text.frag");
    private final Map<Integer, SizedFont> fonts = new HashMap<>();

    public Fonts(FontInfo fontInfo) {
        this.fontInfo = fontInfo;
    }

    public void dispose() {
        for (SizedFont sizedFont : fonts.values()) {
            sizedFont.font.dispose();
            sizedFont.mesh.dispose();
        }
    }

    public void render(double x, double y, String text, double size, Color4 color) {
        SizedFont font = get(size);

        if (!font.building) {
            font.mesh.begin();
            font.building = true;
        }

        font.font.render(font.mesh, text, x, y, color);
    }

    public void end(Matrix4f projection) {
        shader.bind();
        shader.set("u_Proj", projection);

        for (SizedFont font : fonts.values()) {
            if (!font.building) continue;
            font.building = false;

            shader.set("u_Texture", font.font.getTexture().bind());
            font.mesh.render();
        }
    }

    public double textWidth(String text, int length, double size) {
        return get(size).font.getWidth(text, length);
    }

    public double textHeight(double size) {
        return get(size).font.getHeight();
    }

    private SizedFont get(double size) {
        return fonts.computeIfAbsent((int) size, SizedFont::new);
    }

    private class SizedFont {
        public final Font font;
        public final Mesh mesh;

        public boolean building;

        public SizedFont(double size) {
            font = new Font(fontInfo, (int) size);
            mesh = new Mesh(Mesh.Attrib.Vec2, Mesh.Attrib.Vec2, Mesh.Attrib.Color);
        }
    }
}
