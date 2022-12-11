package meteordevelopment.pts.utils;

public final class Color4 {
    public IColor topLeft, topRight, bottomRight, bottomLeft;

    public Color4(IColor topLeft, IColor topRight, IColor bottomRight, IColor bottomLeft) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
    }

    public Color4(IColor color) {
        this(color, color, color, color);
    }
}
