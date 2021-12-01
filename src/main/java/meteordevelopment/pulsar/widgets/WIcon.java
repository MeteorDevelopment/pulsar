package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.utils.Color4;
import meteordevelopment.pulsar.utils.Vec2;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WIcon extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "icon");

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    protected void onCalculateSize() {
        Vec2 size = get(Properties.SIZE);

        if (size != null) width = height = Math.max(size.x(), size.y());
        else width = height = 0;
    }

    @Override
    protected void onRender(Renderer renderer, double mouseX, double mouseY, double delta) {
        String path = get(Properties.ICON_PATH);
        Vec2 size = get(Properties.SIZE);
        Color4 color = get(Properties.COLOR);

        if (path != null && size != null && color != null) renderer.icon(x, y, path, width, color);
    }
}
