package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.utils.Color4;
import meteordevelopment.pulsar.utils.IColor;
import meteordevelopment.pulsar.utils.Vec2;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WText extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "text");

    private final String text;

    public WText(String text) {
        this.text = text;
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    protected void onCalculateSize() {
        double size = get(Properties.FONT_SIZE);

        width = Renderer.INSTANCE.textWidth(text, size);
        height = Renderer.INSTANCE.textHeight(size);
    }

    @Override
    protected void onRender(Renderer renderer, double mouseX, double mouseY, double delta) {
        super.onRender(renderer, mouseX, mouseY, delta);

        renderText(renderer, x, y, text);
    }
}
