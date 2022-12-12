package org.meteordev.pulsar.widgets;

import org.meteordev.pulsar.input.MouseButtonEvent;
import org.meteordev.pulsar.input.MouseMovedEvent;
import org.meteordev.pulsar.input.MouseScrolledEvent;
import org.meteordev.pulsar.rendering.Renderer;
import org.meteordev.pulsar.utils.Utils;

import static org.meteordev.pulsar.utils.Utils.combine;

public class WSlider extends Widget {
    protected static final String[] NAMES = combine(Widget.NAMES, "slider");

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

        left = add(new Widget().tag("slider-left")).expandX().widget();
        right = add(new Widget().tag("slider-right")).expandX().widget();
        handle = add(new WHandle()).widget();
    }

    @Override
    public String[] names() {
        return NAMES;
    }

    @Override
    protected void onMousePressed(MouseButtonEvent event) {
        if (!event.used && isHovered()) {
            valueAtDragStart = value;
            double handleSize = handle.width;

            double valueWidth = event.x - (x + handleSize / 2);
            set((valueWidth / (width - handleSize)) * (max - min) + min);
            if (action != null) action.run();

            dragging = true;
            handle.invalidStyle();

            event.use();
        }
    }

    @Override
    protected void onMouseMoved(MouseMovedEvent event) {
        double s = handle.width;
        double s2 = s / 2;

        boolean mouseOverX = event.x >= this.x + s2 && event.x <= this.x + s2 + width - s;

        if (dragging) {
            if (mouseOverX) {
                double valueWidth = event.x - (this.x + s / 2);
                valueWidth = Utils.clamp(valueWidth, 0, width - s);

                set((valueWidth / (width - s)) * (max - min) + min);
                if (action != null) action.run();
            }
            else {
                if (value > min && event.x < this.x + s2) {
                    value = min;
                    if (action != null) action.run();
                }
                else if (value < max && event.x > this.x + s2 + width - s) {
                    value = max;
                    if (action != null) action.run();
                }
            }
        }
    }

    @Override
    protected void onMouseReleased(MouseButtonEvent event) {
        if (dragging) {
            if (value != valueAtDragStart && actionOnRelease != null) {
                actionOnRelease.run();
            }

            dragging = false;
            handle.invalidStyle();
        }
    }

    @Override
    protected void onMouseScrolled(MouseScrolledEvent event) {
        if (!event.used && isHovered()) {
            set(value + 0.25 * event.value);

            if (action != null) action.run();
            event.use();
        }
    }

    public void set(double value) {
        this.value = Utils.clamp(value, min, max);
    }

    public double get() {
        return value;
    }

    protected int valueWidth() {
        double valuePercentage = (value - min) / (max - min);
        return (int) Math.ceil(valuePercentage * (width - handle.width));
    }

    @Override
    public void render(Renderer renderer, double delta) {
        int v = valueWidth();
        handle.x = x + v;
        v += handle.width / 2;
        left.width = v;
        right.x = x + v;
        right.width = width - v;

        super.render(renderer, delta);
    }

    protected class WHandle extends Widget {
        protected static final String[] NAMES = combine(Widget.NAMES, "slider-handle");

        @Override
        public String[] names() {
            return NAMES;
        }

        @Override
        public boolean isPressed() {
            return dragging;
        }
    }
}
