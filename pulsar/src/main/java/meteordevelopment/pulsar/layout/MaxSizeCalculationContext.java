package meteordevelopment.pulsar.layout;

import meteordevelopment.pts.properties.Properties;
import meteordevelopment.pulsar.utils.IntStack;
import meteordevelopment.pulsar.widgets.Widget;

public class MaxSizeCalculationContext {
    private final IntStack yStack = new IntStack(10);
    private final IntStack maxHeightStack = new IntStack(10);

    private boolean adjusted;

    public MaxSizeCalculationContext() {
        yStack.push(Integer.MIN_VALUE);
        maxHeightStack.push(Integer.MAX_VALUE);
    }

    public void pushMaxSize(Widget widget) {
        int maxHeight = maxHeightStack.peek();
        double widgetMaxHeight = widget.get(Properties.MAX_HEIGHT);

        boolean hasMaxHeight = maxHeight != Integer.MAX_VALUE;
        boolean widgetHasMaxHeight = widgetMaxHeight != Double.MAX_VALUE;

        int y = yStack.peek();
        yStack.push(Math.max(
                yStack.peek(),
                widget.y
        ));

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
        yStack.pop();
        maxHeightStack.pop();
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
