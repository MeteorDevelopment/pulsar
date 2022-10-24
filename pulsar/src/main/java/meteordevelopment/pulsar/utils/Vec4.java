package meteordevelopment.pulsar.utils;

@SuppressWarnings("SuspiciousNameCombination")
public record Vec4(double x, double y, double z, double w) {
    public Vec4(double v) {
        this(v, v, v, v);
    }

    public int top() {
        return (int) Math.ceil(x);
    }
    public int right() {
        return (int) Math.ceil(y);
    }
    public int bottom() {
        return (int) Math.ceil(z);
    }
    public int left() {
        return (int) Math.ceil(w);
    }

    public int horizontal() {
        return (int) Math.ceil(left() + right());
    }
    public int vertical() {
        return (int) Math.ceil(bottom() + top());
    }

    public double topLeft() {
        return x;
    }
    public double topRight() {
        return y;
    }
    public double bottomRight() {
        return z;
    }
    public double bottomLeft() {
        return w;
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0 && w == 0;
    }
}
