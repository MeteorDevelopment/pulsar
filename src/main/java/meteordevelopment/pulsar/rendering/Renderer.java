package meteordevelopment.pulsar.rendering;

import meteordevelopment.pulsar.theme.Theme;
import meteordevelopment.pulsar.utils.ColorFactory;
import meteordevelopment.pulsar.utils.IColor;
import meteordevelopment.pulsar.utils.Vec4;
import org.joml.Matrix4f;

public class Renderer {
    private static final IColor BLANK = ColorFactory.create(0, 0, 0, 0);

    public static Renderer INSTANCE;

    public Theme theme;

    private final Shader rectangleShader = new Shader("/pulsar/shaders/rectangles.vert", "/pulsar/shaders/rectangles.frag");
    private final Mesh rectangleMesh = new Mesh(Mesh.Attrib.Vec2, Mesh.Attrib.Vec2, Mesh.Attrib.Vec2, Mesh.Attrib.Vec4, Mesh.Attrib.UByte, Mesh.Attrib.Color, Mesh.Attrib.Color, Mesh.Attrib.Float);

    private final Fonts fonts = new Fonts();

    public Renderer() {
        INSTANCE = this;
    }

    public void begin() {
        rectangleMesh.begin();
    }

    public void end(int windowWidth, int windowHeight) {
        Matrix4f projection = new Matrix4f().ortho2D(0, windowWidth, 0, windowHeight);

        // Rectangles
        rectangleShader.bind();
        rectangleShader.set("u_Proj", projection);
        rectangleShader.set("u_WindowSize", windowWidth, windowHeight);
        rectangleMesh.render();

        // Text
        fonts.end(projection);
    }

    public void quad(double x, double y, double width, double height, Vec4 radius, double outlineSize, IColor backgroundColor, IColor outlineColor) {
        double s = Math.max(width, height);

        int background = backgroundColor != null ? 1 : 0;
        if (backgroundColor == null) backgroundColor = BLANK;
        if (outlineColor == null) outlineColor = BLANK;

        rectangleMesh.quad(
                rectangleMesh.vec2(x, y).vec2(-1, -1).vec2(width, height).vec4(radius).uByte(background).color(backgroundColor).color(outlineColor).float_(outlineSize).next(),
                rectangleMesh.vec2(x, y + s).vec2(-1, 1).vec2(width, height).vec4(radius).uByte(background).color(backgroundColor).color(outlineColor).float_(outlineSize).next(),
                rectangleMesh.vec2(x + s, y + s).vec2(1, 1).vec2(width, height).vec4(radius).uByte(background).color(backgroundColor).color(outlineColor).float_(outlineSize).next(),
                rectangleMesh.vec2(x + s, y).vec2(1, -1).vec2(width, height).vec4(radius).uByte(background).color(backgroundColor).color(outlineColor).float_(outlineSize).next()
        );
    }

    public void text(double x, double y, String text, double size, IColor color) {
        fonts.render(x, y, text, size, color);
    }

    public double textWidth(String text, double size) {
        return fonts.textWidth(text, size);
    }

    public double textHeight(double size) {
        return fonts.textHeight(size);
    }
}
