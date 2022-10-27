package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.layout.VerticalLayout;

import static meteordevelopment.pulsar.utils.Utils.combine;

/** Vertical list widget which uses {@link VerticalLayout}. */
public class WVerticalList extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "container", "vertical-list");

    public WVerticalList() {
        layout = VerticalLayout.INSTANCE;
    }

    @Override
    public String[] names() {
        return NAMES;
    }
}
