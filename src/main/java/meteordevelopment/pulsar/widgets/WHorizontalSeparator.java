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

    public WHorizontalSeparator(String text) {
        if (text != null) {
            tag("has-text");
            textW = add(new WHSText(text)).expandCellX().widget();
        }
    }

    public WHorizontalSeparator() {
        this(null);
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
            int y = this.y + height / 2 - size.intY() / 2 - 1;

            if (textW == null) {
                renderer.quad(x, y, width, size.intY(), radius, 0, backgroundColor, null);
            }
            else {
                int w = textW.x - x - spacing.intX();
                if (w > 0) renderer.quad(x, y, w, size.intY(), radius, 0, backgroundColor, null);

                int x = textW.x + textW.width + spacing.intX();
                w = (this.x + width) - x;
                if (w > 0) renderer.quad(x, y, w, size.intY(), radius, 0, backgroundColor, null);
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
