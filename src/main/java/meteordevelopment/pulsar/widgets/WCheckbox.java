package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.theme.Properties;
import meteordevelopment.pulsar.utils.Vec4;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WCheckbox extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "checkbox");

    public boolean checked;

    public WCheckbox(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    protected void onCalculateSize() {
        Vec4 padding = get(Properties.PADDING);

        width = 10 + padding.horizontal();
        height = 10 + padding.vertical();
    }

    @Override
    protected void onMousePressed(int button) {
        if (hovered) {
            checked = !checked;
            style = null;
        }
    }

    @Override
    public String state() {
        if (checked) return "checked";
        return super.state();
    }
}
