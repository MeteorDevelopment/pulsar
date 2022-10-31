package meteordevelopment.pts.utils;

public class ColorFactory {
    public interface IColorFactory {
        IColor create(int r, int g, int b, int a);
    }

    public static IColorFactory factory = ColorImpl::new;

    public static IColor create(int r, int g, int b, int a) {
        return factory.create(
                clamp(r),
                clamp(g),
                clamp(b),
                clamp(a)
        );
    }

    private static int clamp(int value) {
        if (value < 0) return 0;
        return Math.min(value, 255);
    }
}
