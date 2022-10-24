package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.layout.HorizontalLayout;

import static meteordevelopment.pulsar.utils.Utils.combine;

/** Horizontal list widget which uses {@link HorizontalLayout}. */
public class WHorizontalList extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "container", "horizontal-list");

    public WHorizontalList() {
        layout = new HorizontalLayout();
    }

    @Override
    public String[] names() {
        return NAMES;
    }
}
