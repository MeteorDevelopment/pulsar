package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.utils.CharFilters;
import meteordevelopment.pulsar.utils.Utils;

import java.util.Locale;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WDoubleEdit extends WHorizontalList {
    protected static final String[] NAMES = combine(WHorizontalList.NAMES, "double-edit");

    public Runnable action, actionOnUnfocused;
    public int decimalPlaces = 3;

    protected double value, min, max;

    protected WTextBox textBoxW;
    protected WSlider sliderW;

    public WDoubleEdit(double value, Double min, Double max, double sliderMin, double sliderMax) {
        this.value = value;
        this.min = min != null ? min : Double.MIN_VALUE;
        this.max = max != null ? max : Double.MAX_VALUE;

        init(true, sliderMin, sliderMax);
    }

    public WDoubleEdit(double value, Double min, Double max) {
        this.value = value;
        this.min = min != null ? min : Double.MIN_VALUE;
        this.max = max != null ? max : Double.MAX_VALUE;

        init(false, 0, 0);
    }

    private void init(boolean slider, double sliderMin, double sliderMax) {
        textBoxW = add(new WTextBox(format())).widget();
        textBoxW.tag("double-edit");
        textBoxW.filter = CharFilters.DOUBLE;

        textBoxW.actionOnUnfocused = () -> {
            double v = 0;

            try {
                v = Double.parseDouble(textBoxW.get());
            }
            catch (NumberFormatException ignored) {}

            double prev = value;
            set(v);

            if (prev != value) runActions();
        };

        if (slider) {
            if (sliderMin < min) sliderMin = min;
            if (sliderMax > max) sliderMax = max;

            sliderW = add(new WSlider(value, sliderMin, sliderMax)).expandX().widget();
            sliderW.tag("double-edit");

            sliderW.action = () -> {
                double prev = value;
                value = Utils.clamp(sliderW.get(), min, max);

                textBoxW.set(format());

                if (prev != value && action != null) action.run();
            };

            sliderW.actionOnRelease = () -> {
                if (actionOnUnfocused != null) actionOnUnfocused.run();
            };
        }
        else {
            WButton minus = add(new WButton("-")).widget();
            minus.tag("double-edit");
            minus.action = () -> buttonAdd(-1);
            minus.checkIcon();

            WButton plus = add(new WButton("+")).widget();
            plus.tag("double-edit");
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
        double prev = value;
        set(value + delta);

        if (prev != value) runActions();
    }

    public double get() {
        return value;
    }

    public void set(double value) {
        this.value = Utils.clamp(value, min, max);

        textBoxW.set(format());
        if (sliderW != null) sliderW.set(value);
    }

    protected String format() {
        return String.format(Locale.US, "%." + decimalPlaces + "f", value);
    }
}
