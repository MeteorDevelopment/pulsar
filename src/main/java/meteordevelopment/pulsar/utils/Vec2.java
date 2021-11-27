package meteordevelopment.pulsar.utils;

public record Vec2(double x, double y) {
    public Vec2(double v) {
        this(v, v);
    }
}
