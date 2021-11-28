package meteordevelopment.pulsar.utils;

@SuppressWarnings("SuspiciousNameCombination")
public record Vec4(double x, double y, double z, double w) {
    public Vec4(double v) {
        this(v, v, v, v);
    }

    public double top() {
        return x;
    }
    public double right() {
        return y;
    }
    public double bottom() {
        return z;
    }
    public double left() {
        return w;
    }

    public double horizontal() {
        return left() + right();
    }
    public double vertical() {
        return bottom() + top();
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
