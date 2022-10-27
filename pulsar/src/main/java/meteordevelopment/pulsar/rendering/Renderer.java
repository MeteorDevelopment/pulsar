package meteordevelopment.pulsar.rendering;

import meteordevelopment.pulsar.Pulsar;
import meteordevelopment.pulsar.theme.Theme;
import meteordevelopment.pulsar.utils.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11C.*;

public class Renderer {
    private static final Color4 BLANK = new Color4(ColorFactory.create(0, 0, 0, 0));

    public static Renderer INSTANCE;

    public Theme theme;
    public long window;

    public double mouseX, mouseY;
    public double offsetY;

    private final Shader rectangleShader = new Shader("/pulsar/shaders/rectangles.vert", "/pulsar/shaders/rectangles.frag");
    private final Mesh rectangleMesh = new Mesh(Mesh.Attrib.Vec2, Mesh.Attrib.Vec2, Mesh.Attrib.Vec2, Mesh.Attrib.Vec4, Mesh.Attrib.UByte, Mesh.Attrib.Color, Mesh.Attrib.Color, Mesh.Attrib.Float);

    private final Icons icons = new Icons();
    private final Shader iconShader = new Shader("/pulsar/shaders/texture.vert", "/pulsar/shaders/icon.frag");
    private final Mesh iconMesh = new Mesh(Mesh.Attrib.Vec2, Mesh.Attrib.Vec2, Mesh.Attrib.Color);

    private final Shader textureShader = new Shader("/pulsar/shaders/texture.vert", "/pulsar/shaders/texture.frag");
    private final List<Texture> textures = new ArrayList<>();

    private final List<Runnable> afterRunnables = new ArrayList<>();
    private Fonts fonts;

    public int windowWidth, windowHeight;
    private final FloatBuffer projection = MemoryUtil.memAllocFloat(16);

    public Renderer() {
        INSTANCE = this;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;

        icons.setTheme(theme);

        if (fonts != null) fonts.dispose();
        fonts = new Fonts(theme.getFontInfo());
    }

    public void setup(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        Matrix.ortho(projection, 0, windowWidth, 0, windowHeight, -10000, 10000);

        begin();
    }

    public void begin() {
        rectangleMesh.begin();
        iconMesh.begin();
    }

    public void render() {
        end();

        if (afterRunnables.size() > 0) {
            begin();

            for (Runnable runnable : afterRunnables) runnable.run();
            afterRunnables.clear();

            end();
        }
    }

    public void end() {
        // Rectangles
        rectangleShader.bind();
        rectangleShader.set("u_Proj", projection);
        rectangleShader.set("u_WindowSize", windowWidth, windowHeight);
        rectangleMesh.render();

        // Icons
        iconShader.bind();
        iconShader.set("u_Proj", projection);
        iconShader.set("u_Texture", icons.bind());
        iconMesh.render();

        // Textures
        if (textures.size() > 0) {
            textureShader.bind();
            textureShader.set("u_Proj", projection);
            textureShader.set("u_Texture", 0);

            for (Texture texture : textures) {
                Pulsar.BIND_TEXTURE.accept(texture.glId);

                iconMesh.begin();
                iconMesh.quad(
                        iconMesh.vec2(texture.x, texture.y).vec2(0, 0).color(texture.color.bottomLeft).next(),
                        iconMesh.vec2(texture.x, texture.y + texture.height).vec2(0, 1).color(texture.color.topLeft).next(),
                        iconMesh.vec2(texture.x + texture.width, texture.y + texture.height).vec2(1, 1).color(texture.color.topRight).next(),
                        iconMesh.vec2(texture.x + texture.width, texture.y).vec2(1, 0).color(texture.color.bottomRight).next()
                );
                iconMesh.render();
            }

            textures.clear();
        }

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
        iconMesh.alpha(alpha);
    }

    public void quad(int x, int y, int width, int height, Vec4 radius, double outlineSize, Color4 backgroundColor, Color4 outlineColor) {
        y += offsetY;

        int background = backgroundColor != null ? 1 : 0;
        if (backgroundColor == null) backgroundColor = BLANK;
        if (outlineColor == null) outlineColor = BLANK;

        double hw = width / 2.0;
        double hh = height / 2.0;

        double lx = Utils.clamp((width - hh) / hh, -1, 1);
        double ly = Utils.clamp((height - hw) / hw, -1, 1);

        rectangleMesh.quad(
                rectangleMesh.vec2(x, y).vec2(-1, -1).vec2(width, height).vec4(radius).uByte(background).color(backgroundColor.bottomLeft).color(outlineColor.bottomLeft).float_(outlineSize).next(),
                rectangleMesh.vec2(x, y + height).vec2(-1, ly).vec2(width, height).vec4(radius).uByte(background).color(backgroundColor.topLeft).color(outlineColor.topLeft).float_(outlineSize).next(),
                rectangleMesh.vec2(x + width, y + height).vec2(lx, ly).vec2(width, height).vec4(radius).uByte(background).color(backgroundColor.topRight).color(outlineColor.topRight).float_(outlineSize).next(),
                rectangleMesh.vec2(x + width, y).vec2(lx, -1).vec2(width, height).vec4(radius).uByte(background).color(backgroundColor.bottomRight).color(outlineColor.bottomRight).float_(outlineSize).next()
        );
    }

    public void text(int x, int y, String text, int size, Color4 color) {
        fonts.render(x, y + offsetY, text, size, color);
    }

    public void chars(int x, int y, char c, int count, double size, Color4 color) {
        fonts.renderChars(x, y + offsetY, c, count, size, color);
    }

    public void icon(int x, int y, String path, double size, Color4 color) {
        y += offsetY;

        size = (int) size;
        TextureRegion region = icons.get(path, (int) size);

        iconMesh.quad(
                iconMesh.vec2(x, y).vec2(region.x1(), region.y1()).color(color.bottomLeft).next(),
                iconMesh.vec2(x, y + size).vec2(region.x1(), region.y2()).color(color.topLeft).next(),
                iconMesh.vec2(x + size, y + size).vec2(region.x2(), region.y2()).color(color.topRight).next(),
                iconMesh.vec2(x + size, y).vec2(region.x2(), region.y1()).color(color.bottomRight).next()
        );
    }

    public void texture(int x, int y, int width, int height, int glId, Color4 color) {
        textures.add(new Texture(x, y + offsetY, width, height, glId, color));
    }

    public int textWidth(String text, double size) {
        return (int) Math.ceil(fonts.textWidth(text, text.length(), size));
    }
    public int textWidth(String text, int length, double size) {
        return (int) Math.ceil(fonts.textWidth(text, length, size));
    }

    public int charWidth(char c, double size) {
        return (int) Math.ceil(fonts.charWidth(c, size));
    }

    public int textHeight(double size) {
        return (int) Math.ceil(fonts.textHeight(size));
    }

    public void after(Runnable runnable) {
        afterRunnables.add(runnable);
    }

    private record Texture(double x, double y, double width, double height, int glId, Color4 color) {}
}
