package org.meteordev.pulsar.widgets;

import org.meteordev.pulsar.layout.HorizontalLayout;

import static org.meteordev.pulsar.utils.Utils.combine;

/** Horizontal list container widget which uses {@link HorizontalLayout}. */
public class WHorizontalList extends WContainer {
    protected static final String[] SELF_NON_SCROLL_MODE_NAMES = combine(WContainer.SELF_NON_SCROLL_MODE_NAMES, "horizontal-list");
    protected static final String[] CONTENTS_NON_SCROLL_MODE_NAMES = combine(WContainer.CONTENTS_NON_SCROLL_MODE_NAMES, "horizontal-list");

    public WHorizontalList() {
        layout = new HorizontalLayout();
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
