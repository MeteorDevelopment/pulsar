package org.meteordev.pulsar.widgets;

import org.meteordev.pulsar.utils.CharFilters;
import org.meteordev.pulsar.utils.Utils;

import static org.meteordev.pulsar.utils.Utils.combine;

public class WIntEdit extends WHorizontalList {
    protected static final String[] NAMES = combine(WHorizontalList.NAMES, "int-edit");

    public Runnable action, actionOnUnfocused;

    protected int value, min, max;

    protected WTextBox textBoxW;
    protected WSlider sliderW;

    public WIntEdit(int value, Integer min, Integer max, int sliderMin, int sliderMax) {
        this.value = value;
        this.min = min != null ? min : Integer.MIN_VALUE;
        this.max = max != null ? max : Integer.MAX_VALUE;

        init(true, sliderMin, sliderMax);
    }

    public WIntEdit(int value, Integer min, Integer max) {
        this.value = value;
        this.min = min != null ? min : Integer.MIN_VALUE;
        this.max = max != null ? max : Integer.MAX_VALUE;

        init(false, 0, 0);
    }

    private void init(boolean slider, int sliderMin, int sliderMax) {
        textBoxW = add(new WTextBox(Integer.toString(value))).widget();
        textBoxW.tag("int-edit");
        textBoxW.filter = CharFilters.INTEGER;

        textBoxW.actionOnUnfocused = () -> {
            int v = 0;

            try {
                v = Integer.parseInt(textBoxW.get());
            }
            catch (NumberFormatException ignored) {}

            int prev = value;
            set(v);

            if (prev != value) runActions();
        };

        if (slider) {
            if (sliderMin < min) sliderMin = min;
            if (sliderMax > max) sliderMax = max;

            sliderW = add(new WSlider(value, sliderMin, sliderMax)).expandX().widget();
            sliderW.tag("int-edit");

            sliderW.action = () -> {
                int prev = value;
                value = Utils.clamp((int) Math.round(sliderW.get()), min, max);

                textBoxW.set(Integer.toString(value));

                if (prev != value && action != null) action.run();
            };

            sliderW.actionOnRelease = () -> {
                if (actionOnUnfocused != null) actionOnUnfocused.run();
            };
        }
        else {
            WButton minus = add(new WButton("-")).widget();
            minus.tag("int-edit");
            minus.action = () -> buttonAdd(-1);
            minus.checkIcon();

            WButton plus = add(new WButton("+")).widget();
            plus.tag("int-edit");
            plus.action = () -> buttonAdd(1);
            plus.checkIcon();
        }
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    private void runActions() {
        if (action != null) action.run();
        if (actionOnUnfocused != null) actionOnUnfocused.run();
    }

    private void buttonAdd(int delta) {
        int prev = value;
        set(value + delta);

        if (prev != value) runActions();
    }

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = Utils.clamp(value, min, max);

        textBoxW.set(Integer.toString(this.value));
        if (sliderW != null) sliderW.set(value);
    }
}
