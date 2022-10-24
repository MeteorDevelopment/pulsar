package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.layout.TableLayout;

import static meteordevelopment.pulsar.utils.Utils.combine;

/** Table widget which uses {@link TableLayout}. */
public class WTable extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "container", "table");

    public WTable() {
        layout = new TableLayout();
    }

    @Override
    public String[] names() {
        return NAMES;
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
