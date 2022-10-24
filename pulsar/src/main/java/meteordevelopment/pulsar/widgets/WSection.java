package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.input.Event;
import meteordevelopment.pulsar.layout.Layout;
import meteordevelopment.pulsar.layout.VerticalLayout;
import meteordevelopment.pulsar.utils.Utils;

public class WSection extends Widget {
    protected static final String[] NAMES = Utils.combine(Widget.NAMES, "section");

    protected final Widget header;
    protected final Widget body;

    protected boolean expanded;
    protected double animation;

    public WSection(String title, boolean expanded) {
        this.expanded = expanded;
        this.animation = expanded ? 1 : 0;
        this.layout = VerticalLayout.INSTANCE;

        header = super.add(new WHorizontalList()).expandX().widget();
        body = super.add(new WVerticalList()).expandX().widget();

        header.add(new WHorizontalSeparator(title)).expandX();
    }

    public WSection(String title) {
        this(title, true);
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    public <T extends Layout> T setLayout(T layout) {
        body.layout = layout;
        return layout;
    }

    @Override
    public <T extends Widget> Cell<T> add(T widget) {
        return body.add(widget);
    }

    @Override
    public boolean remove(Widget widget) {
        return body.remove(widget);
    }

    @Override
    public void clear() {
        body.clear();
    }

    @Override
    public void dispatch(Event event) {
        header.dispatch(event);
        if (expanded) body.dispatch(event);

        dispatchToSelf(event);
    }
}
