package meteordevelopment.pulsar.utils;

public record Vec2(double x, double y) {
    public Vec2(double v) {
        this(v, v);
    }

    public int intX() {
        return (int) Math.ceil(x);
    }

    public int intY() {
        return (int) Math.ceil(y);
    }
}
