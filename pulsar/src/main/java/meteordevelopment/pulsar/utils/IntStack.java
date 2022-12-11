package meteordevelopment.pulsar.utils;

public class IntStack {
    private int[] array;
    private int size;

    public IntStack(int defaultCapacity) {
        array = new int[defaultCapacity];
    }

    public void push(int value) {
        if (size >= array.length) grow();
        array[size++] = value;
    }

    public int pop() {
        return array[--size];
    }

    public int peek() {
        return array[size - 1];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void grow() {
        int capacity = Math.max(array.length + 1, (int) (array.length * 1.75));

        int[] newArray = new int[capacity];
        System.arraycopy(array, 0, newArray, 0, size);

        array = newArray;
    }
}
