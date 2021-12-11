package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.utils.Color4;
import meteordevelopment.pulsar.utils.Vec2;
import meteordevelopment.pulsar.utils.Vec4;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WHorizontalSeparator extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "horizontal-separator");

    protected WText textW;

    public WHorizontalSeparator() {}

    public WHorizontalSeparator(String text) {
        textW = add(new WHSText(text)).expandCellX().widget();
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    protected void onRender(Renderer renderer, double delta) {
        Vec4 radius = get(Properties.RADIUS);
        Color4 backgroundColor = get(Properties.BACKGROUND_COLOR);
        Vec2 size = get(Properties.SIZE);
        Vec2 spacing = get(Properties.SPACING);

        if (backgroundColor != null) {
            double y = Math.round(this.y + height / 2 - size.y() / 2 - 0.1);

            if (textW == null) {
                renderer.quad(x, y, width, size.y(), radius, 0, backgroundColor, null);
            }
            else {
                double w = textW.x - x - spacing.x();
                if (w > 0) renderer.quad(x, y, w, size.y(), radius, 0, backgroundColor, null);

                double x = textW.x + textW.width + spacing.x();
                w = (this.x + width) - x;
                if (w > 0) renderer.quad(x, y, w, size.y(), radius, 0, backgroundColor, null);
            }
        }
    }

    protected static class WHSText extends WText {
        protected static final String[] NAMES = combine(WText.NAMES, "horizontal-separator-text");

        public WHSText(String text) {
            super(text);
        }

        @Override
        public String[] names() {
            return NAMES;
        }
    }
}
