package meteordevelopment.pulsar.widgets;

import meteordevelopment.pts.properties.Properties;
import meteordevelopment.pulsar.layout.MaxSizeCalculationContext;
import meteordevelopment.pulsar.rendering.Renderer;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.pulsar.utils.Utils.combine;

/** Text widget. */
public class WText extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "text");

    private String text;
    private final List<String> lines = new ArrayList<>(1);

    private boolean afterAdjustToMaxSize;
    private int realWidth, realHeight;

    public WText(String text) {
        this.text = text;
    }

    @Override
    public String[] names() {
        return NAMES;
    }


    @Override
    public void calculateSize() {
        if (afterAdjustToMaxSize) {
            afterAdjustToMaxSize = false;
            width = realWidth;
            height = realHeight;

            return;
        }

        String font = get(Properties.FONT);
        double size = get(Properties.FONT_SIZE);

        String override = getTextOverride();
        width = Renderer.INSTANCE.textWidth(font, override != null ? override : text, size);
        height = Renderer.INSTANCE.textHeight(font, size);

        lines.clear();
        lines.add(text);
    }

    @Override
    public boolean adjustToMaxSize(MaxSizeCalculationContext ctx) {
        String override = getTextOverride();
        if (override != null) return false;

        int maxWidth = ctx.peekMaxWidth();
        if (width <= maxWidth) return false;

        lines.clear();
        split(get(Properties.FONT_SIZE), maxWidth);

        afterAdjustToMaxSize = true;
        realWidth = width;
        realHeight = height;

        return true;
    }

    private void split(double size, double maxWidth) {
        String[] words = text.split(" ");
        StringBuilder sb = new StringBuilder();

        String font = get(Properties.FONT);
        int spaceWidth = Renderer.INSTANCE.textWidth(font, " ", size);

        int lineWidth = 0;
        int maxLineWidth = 0;

        int iInLine = 0;

        for (int i = 0; i < words.length; i++) {
            int wordWidth = Renderer.INSTANCE.textWidth(font, words[i], words[i].length(), size);

            int toAdd = wordWidth;
            if (iInLine > 0) toAdd += spaceWidth;

            if (lineWidth + toAdd > maxWidth && sb.length() > 0) {
                lines.add(sb.toString());
                sb.setLength(0);

                lineWidth = 0;
                iInLine = 0;

                i--;
            }
            else {
                if (iInLine > 0) {
                    sb.append(' ');
                    lineWidth += spaceWidth;
                }

                sb.append(words[i]);
                lineWidth += wordWidth;

                maxLineWidth = Math.max(maxLineWidth, lineWidth);
                iInLine++;
            }
        }

        if (sb.length() > 0) lines.add(sb.toString());

        width = maxLineWidth;
        height *= lines.size();
    }

    @Override
    protected void onRender(Renderer renderer, double delta) {
        super.onRender(renderer, delta);

        // Single line override
        String override = getTextOverride();
        if (override != null) {
            renderText(renderer, x + getOffsetX(), y, override);
            return;
        }

        // Multi line
        int y = this.y;
        int h = renderer.textHeight(get(Properties.FONT), get(Properties.FONT_SIZE));

        for (String line : lines) {
            renderText(renderer, x + getOffsetX(), y, line);
            y += h;
        }
    }

    protected int getOffsetX() {
        return 0;
    }

    public void setText(String text) {
        this.text = text;
        invalidateLayout();
    }

    public String getText() {
        String override = getTextOverride();
        return override != null ? override : text;
    }

    /** Called every frame to override the stored text, bypasses max width calculations. */
    protected String getTextOverride() {
        return null;
    }
}
