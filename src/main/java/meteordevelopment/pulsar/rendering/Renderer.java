package meteordevelopment.pulsar.rendering;

import meteordevelopment.pulsar.theme.Theme;
import meteordevelopment.pulsar.utils.*;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11C.*;

public class Renderer {
    private static final Color4 BLANK = new Color4(ColorFactory.create(0, 0, 0, 0));

    public static Renderer INSTANCE;

    public Theme theme;
    public long window;

    private final Shader rectangleShader = new Shader("/pulsar/shaders/rectangles.vert", "/pulsar/shaders/rectangles.frag");
    private final Mesh rectangleMesh = new Mesh(Mesh.Attrib.Vec2, Mesh.Attrib.Vec2, Mesh.Attrib.Vec2, Mesh.Attrib.Vec4, Mesh.Attrib.UByte, Mesh.Attrib.Color, Mesh.Attrib.Color, Mesh.Attrib.Float);

    private final Fonts fonts = new Fonts();

    private int windowWidth, windowHeight;
    private Matrix4f projection;

    public Renderer() {
        INSTANCE = this;
    }

    public void begin(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        projection = new Matrix4f().ortho2D(0, windowWidth, 0, windowHeight);

        begin();
    }

    private void begin() {
        rectangleMesh.begin();
    }

    public void end() {
        // Rectangles
        rectangleShader.bind();
        rectangleShader.set("u_Proj", projection);
        rectangleShader.set("u_WindowSize", windowWidth, windowHeight);
        rectangleMesh.render();

        // Text
        fonts.end(projection);
    }

    public void beginScissor(double x, double y, double width, double height) {
        end();

        glEnable(GL_SCISSOR_TEST);
        glScissor((int) x, (int) y, (int) width, (int) height);

        begin();
    }

    public void endScissor() {
        end();

        glDisable(GL_SCISSOR_TEST);

        begin();
    }

    public void alpha(double alpha) {
        rectangleMesh.alpha(alpha);
    }

    public void quad(double x, double y, double width, double height, Vec4 radius, double outlineSize, Color4 backgroundColor, Color4 outlineColor) {
        int background = backgroundColor != null ? 1 : 0;
        if (backgroundColor == null) backgroundColor = BLANK;
        if (outlineColor == null) outlineColor = BLANK;

        double hw = width / 2;
        double hh = height / 2;

        double lx = Utils.clamp((width - hh) / hh, -1, 1);
        double ly = Utils.clamp((height - hw) / hw, -1, 1);

        rectangleMesh.quad(
                rectangleMesh.vec2(x, y).vec2(-1, -1).vec2(width, height).vec4(radius).uByte(background).color(backgroundColor.bottomLeft()).color(outlineColor.bottomLeft()).float_(outlineSize).next(),
                rectangleMesh.vec2(x, y + height).vec2(-1, ly).vec2(width, height).vec4(radius).uByte(background).color(backgroundColor.topLeft()).color(outlineColor.topLeft()).float_(outlineSize).next(),
                rectangleMesh.vec2(x + width, y + height).vec2(lx, ly).vec2(width, height).vec4(radius).uByte(background).color(backgroundColor.topRight()).color(outlineColor.topRight()).float_(outlineSize).next(),
                rectangleMesh.vec2(x + width, y).vec2(lx, -1).vec2(width, height).vec4(radius).uByte(background).color(backgroundColor.bottomRight()).color(outlineColor.bottomRight()).float_(outlineSize).next()
        );
    }

    public void text(double x, double y, String text, double size, Color4 color) {
        fonts.render(x, y, text, size, color);
    }

    public double textWidth(String text, double size) {
        return fonts.textWidth(text, text.length(), size);
    }
    public double textWidth(String text, int length, double size) {
        return fonts.textWidth(text, length, size);
    }

    public double textHeight(double size) {
        return fonts.textHeight(size);
    }
}
