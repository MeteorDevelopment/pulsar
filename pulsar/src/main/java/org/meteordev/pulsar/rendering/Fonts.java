package org.meteordev.pulsar.rendering;

import org.joml.Matrix4f;
import org.meteordev.juno.api.Juno;
import org.meteordev.juno.api.JunoProvider;
import org.meteordev.juno.api.pipeline.Pipeline;
import org.meteordev.juno.api.pipeline.PipelineInfo;
import org.meteordev.juno.api.pipeline.state.BlendFunc;
import org.meteordev.juno.api.pipeline.state.WriteMask;
import org.meteordev.juno.api.pipeline.vertexformat.StandardFormats;
import org.meteordev.juno.api.shader.ShaderInfo;
import org.meteordev.juno.api.shader.ShaderType;
import org.meteordev.juno.api.utils.MeshBuilder;
import org.meteordev.pts.utils.Color4;
import org.meteordev.pulsar.theme.Theme;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Fonts {
    private final Theme theme;

    private final Juno juno = JunoProvider.get();

    private final Pipeline pipeline = juno.findPipeline(new PipelineInfo()
            .setVertexFormat(StandardFormats.POSITION_2D_UV_COLOR)
            .setShaders(
                    ShaderInfo.resource(ShaderType.VERTEX, "/pulsar/shaders/texture.vert"),
                    ShaderInfo.resource(ShaderType.FRAGMENT, "/pulsar/shaders/text.frag")
            )
            .setBlendFunc(BlendFunc.alphaBlend())
            .setWriteMask(WriteMask.COLOR)
    );

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
            sizedFont.mb.begin();
            sizedFont.building = true;
        }

        return sizedFont;
    }

    public void render(String font, double x, double y, String text, double size, Color4 color) {
        if (font == null || font.isEmpty() || size <= 0) return;

        SizedFont sizedFont = getFontForRender(font, size);
        sizedFont.font.render(sizedFont.mb, text, x, y, color);
    }

    public void renderChars(String font, double x, double y, char c, int count, double size, Color4 color) {
        if (font == null || font.isEmpty() || count <= 0 || size <= 0) return;

        SizedFont sizedFont = getFontForRender(font, size);
        sizedFont.font.renderChars(sizedFont.mb, x, y, c, count, color);
    }

    public void end(Matrix4f projection) {
        juno.bind(pipeline);
        pipeline.getProgram().setUniform("u_Proj", projection);

        for (FontHolder holder : holders.values()) {
            for (SizedFont sizedFont : holder.fonts.values()) {
                if (!sizedFont.building) continue;
                sizedFont.building = false;

                pipeline.getProgram().setUniform("u_Texture", juno.bind(sizedFont.font.getTexture(), 0));
                sizedFont.mb.draw();
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

    private class FontHolder {
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

    private class SizedFont {
        public final Font font;
        public final MeshBuilder mb;

        public boolean building;

        public SizedFont(FontInfo info, double size) {
            font = new Font(info, (int) size);
            mb = new MeshBuilder(pipeline.getInfo().vertexFormat);
        }

        public void dispose() {
            font.destroy();
            mb.destroy();
        }
    }
}
