package meteordevelopment.pulsar.utils;

public class ColorFactory {
    public interface IColorFactory {
        IColor create(int r, int g, int b, int a);
    }

    public static IColorFactory factory = ColorImpl::new;

    public static IColor create(int r, int g, int b, int a) {
        return factory.create(
                Utils.clamp(r, 0, 255),
                Utils.clamp(g, 0, 255),
                Utils.clamp(b, 0, 255),
                Utils.clamp(a, 0, 255)
        );
    }
}
