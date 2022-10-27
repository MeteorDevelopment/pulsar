package meteordevelopment.pulsar.utils;

public final class Vec2 {
    public double x, y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

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
