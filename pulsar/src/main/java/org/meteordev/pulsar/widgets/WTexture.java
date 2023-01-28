package org.meteordev.pulsar.widgets;

import org.meteordev.juno.api.texture.Texture;
import org.meteordev.pts.properties.Properties;
import org.meteordev.pts.utils.Color4;
import org.meteordev.pts.utils.ColorFactory;
import org.meteordev.pulsar.rendering.Renderer;

import static org.meteordev.pulsar.utils.Utils.combine;

public class WTexture extends Widget {
    private static final Color4 WHITE = new Color4(ColorFactory.create(255, 255, 255, 255));

    protected static final String[] NAMES = combine(Widget.NAMES, "texture");

    public final Texture texture;
    private final int textureWidth, textureHeight;

    public WTexture(Texture texture, int width, int height) {
        this.texture = texture;
        this.textureWidth = width;
        this.textureHeight = height;
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    public void calculateSize() {
        width = textureWidth;
        height = textureHeight;
    }

    @Override
    protected void onRender(Renderer renderer, double delta) {
        Color4 color = get(Properties.COLOR);
        if (color == null) color = WHITE;

        renderer.texture(x, y, width, height, texture, color);
    }
}
