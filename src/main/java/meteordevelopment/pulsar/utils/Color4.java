package meteordevelopment.pulsar.utils;

public record Color4(IColor topLeft, IColor topRight, IColor bottomRight, IColor bottomLeft) {
    public Color4(IColor color) {
        this(color, color, color, color);
    }
}
