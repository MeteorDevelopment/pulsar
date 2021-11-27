package meteordevelopment.pulsar.utils;

public record Vec4(double x, double y, double z, double w) {
    public Vec4(double v) {
        this(v, v, v, v);
    }

    public double horizontal() {
        return y + w;
    }

    public double vertical() {
        return x + z;
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0 && w == 0;
    }
}
