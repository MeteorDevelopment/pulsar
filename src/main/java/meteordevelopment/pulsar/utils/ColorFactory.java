package meteordevelopment.pulsar.utils;

public class ColorFactory {
    public interface IColorFactory {
        IColor create(int r, int g, int b, int a);
    }

    public static IColorFactory factory = ColorImpl::new;

    public static IColor create(int r, int g, int b, int a) {
        return factory.create(r, g, b, a);
    }
}
