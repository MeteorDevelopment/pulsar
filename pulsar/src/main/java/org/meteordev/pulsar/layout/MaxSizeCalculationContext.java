package org.meteordev.pulsar.layout;

import org.meteordev.pts.properties.Properties;
import org.meteordev.pulsar.utils.IntStack;
import org.meteordev.pulsar.widgets.Widget;

public class MaxSizeCalculationContext {
    private final IntStack xStack = new IntStack(10);
    private final IntStack maxWidthStack = new IntStack(10);

    private final IntStack yStack = new IntStack(10);
    private final IntStack maxHeightStack = new IntStack(10);

    private boolean adjusted;

    public MaxSizeCalculationContext() {
        xStack.push(Integer.MIN_VALUE);
        maxWidthStack.push(Integer.MAX_VALUE);

        yStack.push(Integer.MIN_VALUE);
        maxHeightStack.push(Integer.MAX_VALUE);
    }

    public void pushMaxSize(Widget widget) {
        // Max width
        int maxWidth = maxWidthStack.peek();
        double widgetMaxWidth = widget.get(Properties.MAX_WIDTH);

        boolean hasMaxWidth = maxWidth != Integer.MAX_VALUE;
        boolean widgetHasMaxWidth = widgetMaxWidth != Double.MAX_VALUE;

        int x = xStack.peek();
        xStack.push(Math.max(xStack.peek(), widget.x));

        if (!hasMaxWidth) {
            maxWidthStack.push((int) widgetMaxWidth);
        }
        else {
            int newMaxWidth = maxWidth - (xStack.peek() - x) - widget.get(Properties.PADDING).right(); // I am not sure if the right padding here is correct
            if (widgetHasMaxWidth) newMaxWidth = Math.min(newMaxWidth, (int) widgetMaxWidth);
            maxWidthStack.push(newMaxWidth);
        }

        // Max height
        int maxHeight = maxHeightStack.peek();
        double widgetMaxHeight = widget.get(Properties.MAX_HEIGHT);

        boolean hasMaxHeight = maxHeight != Integer.MAX_VALUE;
        boolean widgetHasMaxHeight = widgetMaxHeight != Double.MAX_VALUE;

        int y = yStack.peek();
        yStack.push(Math.max(yStack.peek(), widget.y));

        if (!hasMaxHeight) {
            maxHeightStack.push((int) widgetMaxHeight);
        }
        else {
            int newMaxHeight = maxHeight - (yStack.peek() - y) - widget.get(Properties.PADDING).bottom(); // I am not sure if the bottom padding here is correct
            if (widgetHasMaxHeight) newMaxHeight = Math.min(newMaxHeight, (int) widgetMaxHeight);
            maxHeightStack.push(newMaxHeight);
        }
    }

    public void popMaxSize() {
        xStack.pop();
        maxWidthStack.pop();

        yStack.pop();
        maxHeightStack.pop();
    }

    public int peekMaxWidth() {
        return maxWidthStack.peek();
    }

    public int peekMaxHeight() {
        return maxHeightStack.peek();
    }

    public void setAdjusted() {
        adjusted = true;
    }

    public boolean wasAdjusted() {
        return adjusted;
    }
}
