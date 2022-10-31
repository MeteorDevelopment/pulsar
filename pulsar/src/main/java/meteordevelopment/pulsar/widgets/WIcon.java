package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pts.properties.Properties;
import meteordevelopment.pts.utils.Color4;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WIcon extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "icon");

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    public void calculateSize() {
        super.calculateSize();

        width = height = Math.max(width, height);
    }

    @Override
    protected void onRender(Renderer renderer, double delta) {
        String path = get(Properties.ICON_PATH);
        Color4 color = get(Properties.COLOR);

        if (path != null && color != null) renderer.icon(x, y, path, width, color);
    }
}
