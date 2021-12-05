package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Properties;

import static meteordevelopment.pulsar.utils.Utils.combine;

/** Text widget. */
public class WText extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "text");

    private String text;

    public WText(String text) {
        this.text = text;
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    public void calculateSize() {
        double size = get(Properties.FONT_SIZE);

        width = Renderer.INSTANCE.textWidth(getText(), size);
        height = Renderer.INSTANCE.textHeight(size);
    }

    @Override
    protected void onRender(Renderer renderer, double delta) {
        super.onRender(renderer, delta);

        renderText(renderer, x + getOffsetX(), y, getText());
    }

    protected double getOffsetX() {
        return 0;
    }

    public void setText(String text) {
        this.text = text;
        invalidateLayout();
    }

    public String getText() {
        return text;
    }
}
