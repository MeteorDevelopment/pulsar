package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.layout.VerticalLayout;

import static meteordevelopment.pulsar.utils.Utils.combine;

/** Vertical list container widget which uses {@link VerticalLayout}. */
public class WVerticalList extends WContainer {
    protected static final String[] SELF_NON_SCROLL_MODE_NAMES = combine(WContainer.SELF_NON_SCROLL_MODE_NAMES, "vertical-list");
    protected static final String[] CONTENTS_NON_SCROLL_MODE_NAMES = combine(WContainer.CONTENTS_NON_SCROLL_MODE_NAMES, "vertical-list");

    public WVerticalList() {
        layout = VerticalLayout.INSTANCE;
    }

    @Override
    protected String[] getSelfNonScrollModeNames() {
        return SELF_NON_SCROLL_MODE_NAMES;
    }

    @Override
    protected String[] getContentsScrollModeNames() {
        return CONTENTS_NON_SCROLL_MODE_NAMES;
    }
}
