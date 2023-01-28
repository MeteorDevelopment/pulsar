package org.meteordev.pulsar.rendering;

import com.github.bsideup.jabel.Desugar;
import org.joml.Matrix4f;
import org.meteordev.juno.api.Juno;
import org.meteordev.juno.api.JunoProvider;
import org.meteordev.juno.api.pipeline.Pipeline;
import org.meteordev.juno.api.pipeline.PipelineInfo;
import org.meteordev.juno.api.pipeline.state.BlendFunc;
import org.meteordev.juno.api.pipeline.state.WriteMask;
import org.meteordev.juno.api.pipeline.vertexformat.*;
import org.meteordev.juno.api.shader.ShaderInfo;
import org.meteordev.juno.api.shader.ShaderType;
import org.meteordev.juno.api.texture.Texture;
import org.meteordev.juno.api.utils.MeshBuilder;
import org.meteordev.juno.api.utils.ScissorStack;
import org.meteordev.pts.utils.Color4;
import org.meteordev.pts.utils.ColorFactory;
import org.meteordev.pts.utils.IColor;
import org.meteordev.pts.utils.Vec4;
import org.meteordev.pulsar.theme.Theme;
import org.meteordev.pulsar.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private static final Color4 BLANK = new Color4(ColorFactory.create(0, 0, 0, 0));
    private static final Color4 WHITE = new Color4(ColorFactory.create(255, 255, 255, 255));

    public static Renderer INSTANCE;

    public Theme theme;
    public long window;

    public double mouseX, mouseY;
    public double offsetY;

    private final Juno juno = JunoProvider.get();

    private final Pipeline rectanglesPipeline = juno.findPipeline(new PipelineInfo()
            .setVertexFormat(new VertexFormat(
                    StandardAttributes.POSITION_2D,
                    StandardAttributes.POSITION_2D,
                    StandardAttributes.POSITION_2D,
                    new VertexAttribute(VertexType.FLOAT, 4, false),
                    new VertexAttribute(VertexType.UNSIGNED_BYTE, 1, false),
                    StandardAttributes.COLOR,
                    StandardAttributes.COLOR,
                    new VertexAttribute(VertexType.FLOAT, 1, false)
            ))
            .setShaders(
                    ShaderInfo.resource(ShaderType.VERTEX, "/pulsar/shaders/rectangles.vert"),
                    ShaderInfo.resource(ShaderType.FRAGMENT, "/pulsar/shaders/rectangles.frag")
            )
            .setBlendFunc(BlendFunc.alphaBlend())
            .setWriteMask(WriteMask.COLOR)
    );
    private final MeshBuilder rectanglesMb = new MeshBuilder(rectanglesPipeline.getInfo().vertexFormat);

    private final Icons icons = new Icons();
    private final Pipeline iconsPipeline = juno.findPipeline(new PipelineInfo()
            .setVertexFormat(StandardFormats.POSITION_2D_UV_COLOR)
            .setShaders(
                    ShaderInfo.resource(ShaderType.VERTEX, "/pulsar/shaders/texture.vert"),
                    ShaderInfo.resource(ShaderType.FRAGMENT, "/pulsar/shaders/icon.frag")
            )
            .setBlendFunc(BlendFunc.alphaBlend())
            .setWriteMask(WriteMask.COLOR)
    );
    private final MeshBuilder iconsMb = new MeshBuilder(iconsPipeline.getInfo().vertexFormat);

    private final List<Texture_> textures = new ArrayList<>();
    private final Pipeline texturesPipeline = juno.findPipeline(new PipelineInfo()
            .setVertexFormat(iconsPipeline.getInfo().vertexFormat)
            .setShaders(
                    ShaderInfo.resource(ShaderType.VERTEX, "/pulsar/shaders/texture.vert"),
                    ShaderInfo.resource(ShaderType.FRAGMENT, "/pulsar/shaders/texture.frag")
            )
            .setBlendFunc(BlendFunc.alphaBlend())
            .setWriteMask(WriteMask.COLOR)
    );

    private final List<Runnable> afterRunnables = new ArrayList<>();
    private Fonts fonts;

    private int windowWidth, windowHeight;
    private Matrix4f projection;

    private final ScissorStack scissorStack = new ScissorStack();

    public Renderer() {
        INSTANCE = this;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;

        icons.setTheme(theme);

        if (fonts != null) fonts.dispose();
        fonts = new Fonts(theme);
    }

    public void setup(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        projection = new Matrix4f().ortho2D(0, windowWidth, windowHeight, 0);

        begin();
    }

    public void begin() {
        rectanglesMb.begin();
        iconsMb.begin();
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
        juno.bind(rectanglesPipeline);
        rectanglesPipeline.getProgram().getMatrix4Uniform("u_Proj").set(projection);
        rectanglesPipeline.getProgram().getFloat2Uniform("u_WindowSize").set(windowWidth, windowHeight);
        rectanglesMb.draw();

        // Icons
        juno.bind(iconsPipeline);
        iconsPipeline.getProgram().getMatrix4Uniform("u_Proj").set(projection);
        iconsPipeline.getProgram().getTextureUniform("u_Texture").set(juno.bind(icons.getTexture(), 0));
        iconsMb.draw();

        // Textures
        if (textures.size() > 0) {
            juno.bind(texturesPipeline);
            texturesPipeline.getProgram().getMatrix4Uniform("u_Proj").set(projection);

            for (Texture_ texture : textures) {
                texturesPipeline.getProgram().getTextureUniform("u_Texture").set(juno.bind(texture.texture, 0));

                Color4 color = texture.color;
                if (color == null) color = WHITE;

                iconsMb.begin();
                iconsMb.quad(
                        iconVertex(texture.x, texture.y, 0, 0, color.topLeft),
                        iconVertex(texture.x, texture.y + texture.height, 0, 1, color.bottomLeft),
                        iconVertex(texture.x + texture.width, texture.y + texture.height, 1, 1, color.bottomRight),
                        iconVertex(texture.x + texture.width, texture.y, 1, 0, color.topRight)
                );
                iconsMb.draw();
            }

            textures.clear();
        }

        // Text
        fonts.end(projection);
    }

    public void beginScissor(double x, double y, double width, double height) {
        y += offsetY;

        end();

        ScissorStack.Entry scissor = scissorStack.push((int) x, windowHeight - (int) (y + height), (int) width, (int) height);
        juno.enableScissor(scissor.x, scissor.y, scissor.width, scissor.height);

        begin();
    }

    public void endScissor() {
        end();

        scissorStack.pop();
        if (scissorStack.peek() == null) juno.disableScissor();

        begin();
    }

    public void alpha(double alpha) {
        rectanglesMb.setAlpha(alpha);
        iconsMb.setAlpha(alpha);
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

        rectanglesMb.quad(
                rectangleVertex(x, y, -1, -1, width, height, radius, background, backgroundColor.topLeft, outlineColor.topLeft, outlineSize),
                rectangleVertex(x, y + height, -1, ly, width, height, radius, background, backgroundColor.bottomLeft, outlineColor.bottomLeft, outlineSize),
                rectangleVertex(x + width, y + height, lx, ly, width, height, radius, background, backgroundColor.bottomRight, outlineColor.bottomRight, outlineSize),
                rectangleVertex(x + width, y, lx, -1, width, height, radius, background, backgroundColor.topRight, outlineColor.topRight, outlineSize)
        );
    }

    public void text(String font, int x, int y, String text, int size, Color4 color) {
        fonts.render(font, x, y + offsetY, text, size, color);
    }

    public void chars(String font, int x, int y, char c, int count, double size, Color4 color) {
        fonts.renderChars(font, x, y + offsetY, c, count, size, color);
    }

    public void icon(int x, int y, String path, double size, Color4 color) {
        y += offsetY;

        size = (int) size;
        TextureRegion region = icons.get(path, (int) size);

        iconsMb.quad(
                iconVertex(x, y, region.x1(), region.y2(), color.topLeft),
                iconVertex(x, y + size, region.x1(), region.y1(), color.bottomLeft),
                iconVertex(x + size, y + size, region.x2(), region.y1(), color.bottomRight),
                iconVertex(x + size, y, region.x2(), region.y2(), color.topRight)
        );
    }

    public void texture(int x, int y, int width, int height, Texture texture, Color4 color) {
        textures.add(new Texture_(x, y + offsetY, width, height, texture, color));
    }

    public int textWidth(String font, String text, double size) {
        return (int) Math.ceil(fonts.textWidth(font, text, text.length(), size));
    }
    public int textWidth(String font, String text, int length, double size) {
        return (int) Math.ceil(fonts.textWidth(font, text, length, size));
    }

    public int charWidth(String font, char c, double size) {
        return (int) Math.ceil(fonts.charWidth(font, c, size));
    }

    public int textHeight(String font, double size) {
        return (int) Math.ceil(fonts.textHeight(font, size));
    }

    public void after(Runnable runnable) {
        afterRunnables.add(runnable);
    }

    private int rectangleVertex(double x, double y, double x2, double y2, double width, double height, Vec4 radius, int background, IColor backgroundColor, IColor outlineColor, double outlineSize) {
        return rectanglesMb
                .float2(x, y)
                .float2(x2, y2)
                .float2(width, height)
                .float4(radius.x, radius.y, radius.z, radius.w)
                .uByte1(background)
                .color(backgroundColor.getR(), backgroundColor.getG(), backgroundColor.getB(), backgroundColor.getA())
                .color(outlineColor.getR(), outlineColor.getG(), outlineColor.getB(), outlineColor.getA())
                .float1(outlineSize)
                .next();
    }

    private int iconVertex(double x, double y, double u, double v, IColor color) {
        return iconsMb
                .float2(x, y)
                .float2(u, v)
                .color(color.getR(), color.getG(), color.getB(), color.getA())
                .next();
    }

    @Desugar
    private record Texture_(double x, double y, double width, double height, Texture texture, Color4 color) {}
}
