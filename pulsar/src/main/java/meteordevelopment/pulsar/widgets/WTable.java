package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.layout.TableLayout;

import static meteordevelopment.pulsar.utils.Utils.combine;

/** Table container widget which uses {@link TableLayout}. */
public class WTable extends WContainer {
    protected static final String[] SELF_NON_SCROLL_MODE_NAMES = combine(WContainer.SELF_NON_SCROLL_MODE_NAMES, "table");
    protected static final String[] CONTENTS_NON_SCROLL_MODE_NAMES = combine(WContainer.CONTENTS_NON_SCROLL_MODE_NAMES, "table");

    public WTable() {
        layout = new TableLayout();
    }

    @Override
    protected String[] getSelfNonScrollModeNames() {
        return SELF_NON_SCROLL_MODE_NAMES;
    }

    @Override
    protected String[] getContentsScrollModeNames() {
        return CONTENTS_NON_SCROLL_MODE_NAMES;
    }

    public void row() {
        ((TableLayout) layout).row();
    }

    public int rowI() {
        return ((TableLayout) layout).rowI;
    }

    public void removeRow(int i) {
        ((TableLayout) layout).removeRow(this, i);
    }
}
