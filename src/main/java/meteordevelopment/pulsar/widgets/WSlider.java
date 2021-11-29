package meteordevelopment.pulsar.widgets;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.utils.Utils;

import static meteordevelopment.pulsar.utils.Utils.combine;

public class WSlider extends WContainer {
    protected static final String[] NAMES = combine(WContainer.NAMES, "slider");

    public Runnable action;
    public Runnable actionOnRelease;

    protected double value;
    protected double min, max;

    protected boolean dragging;
    protected double valueAtDragStart;

    protected Widget left, right, handle;

    public WSlider(double value, double min, double max) {
        this.value = Utils.clamp(value, min, max);
        this.min = min;
        this.max = max;

        left = add(new Widget().id("slider-left")).expandX().widget;
        right = add(new Widget().id("slider-right")).expandX().widget;
        handle = add(new Widget().id("slider-handle")).widget;
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    protected boolean onMousePressed(int button, double mouseX, double mouseY, boolean used) {
        if (hovered && !used) {
            valueAtDragStart = value;
            double handleSize = handle.width;

            double valueWidth = mouseX - (x + handleSize / 2);
            set((valueWidth / (width - handleSize)) * (max - min) + min);
            if (action != null) action.run();

            dragging = true;
            return true;
        }

        return false;
    }

    @Override
    protected void onMouseMoved(double mouseX, double mouseY, double deltaMouseX, double deltaMouseY) {
        double s = handle.width;
        double s2 = s / 2;

        boolean mouseOverX = mouseX >= this.x + s2 && mouseX <= this.x + s2 + width - s;
        hovered = mouseOverX && mouseY >= this.y && mouseY <= this.y + height;

        if (dragging) {
            if (mouseOverX) {
                double valueWidth = mouseX - (this.x + s / 2);
                valueWidth = Utils.clamp(valueWidth, 0, width - s);

                set((valueWidth / (width - s)) * (max - min) + min);
                if (action != null) action.run();
            }
            else {
                if (value > min && mouseX < this.x + s2) {
                    value = min;
                    if (action != null) action.run();
                }
                else if (value < max && mouseX > this.x + s2 + width - s) {
                    value = max;
                    if (action != null) action.run();
                }
            }
        }
    }

    @Override
    protected boolean onMouseReleased(int button, double mouseX, double mouseY) {
        if (dragging) {
            if (value != valueAtDragStart && actionOnRelease != null) {
                actionOnRelease.run();
            }

            dragging = false;
            return true;
        }

        return false;
    }

    @Override
    protected boolean onMouseScrolled(double amount) {
        if (hovered) {
            set(value + 0.25 * amount);

            if (action != null) action.run();
            return true;
        }

        return false;
    }

    public void set(double value) {
        this.value = Utils.clamp(value, min, max);
    }

    public double get() {
        return value;
    }

    protected double valueWidth() {
        double valuePercentage = (value - min) / (max - min);
        return valuePercentage * (width - handle.width);
    }

    @Override
    public void render(Renderer renderer, double mouseX, double mouseY, double delta) {
        double v = valueWidth();
        handle.x = x + v;
        v += handle.width / 2;
        left.width = v;
        right.x = x + v;
        right.width = width - v;

        super.render(renderer, mouseX, mouseY, delta);
    }
}
