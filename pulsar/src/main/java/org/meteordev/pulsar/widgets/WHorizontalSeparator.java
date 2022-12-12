package org.meteordev.pulsar.widgets;

import org.meteordev.pts.properties.Properties;
import org.meteordev.pts.utils.Color4;
import org.meteordev.pts.utils.Vec2;
import org.meteordev.pts.utils.Vec4;
import org.meteordev.pulsar.rendering.Renderer;
import org.meteordev.pulsar.utils.Utils;

public class WHorizontalSeparator extends Widget {
    protected static final String[] NAMES = Utils.combine(Widget.NAMES, "horizontal-separator");

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
        protected static final String[] NAMES = Utils.combine(WText.NAMES, "horizontal-separator-text");

        public WHSText(String text) {
            super(text);
        }

        @Override
        public String[] names() {
            return NAMES;
        }
    }
}
