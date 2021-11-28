package meteordevelopment.pulsar.theme;

import meteordevelopment.pulsar.utils.*;

import java.util.HashMap;
import java.util.Map;

public class Properties {
    private static final Map<String, Property<?>> PROPERTIES = new HashMap<>();

    public static final Property<Color4> COLOR = add(new Property<>("color", PropertyType.Color4));
    public static final Property<Color4> BACKGROUND_COLOR = add(new Property<>("background-color", PropertyType.Color4));

    public static final Property<Double> OUTLINE_SIZE = add(new Property<>("outline-size", PropertyType.Number));
    public static final Property<Color4> OUTLINE_COLOR = add(new Property<>("outline-color", PropertyType.Color4));

    public static final Property<Vec2> SPACING = add(new Property<>("spacing", PropertyType.Vec2));
    public static final Property<Vec4> PADDING = add(new Property<>("padding", PropertyType.Vec4));
    public static final Property<Vec4> RADIUS = add(new Property<>("radius", PropertyType.Vec4));
    public static final Property<Vec2> SIZE = add(new Property<>("size", PropertyType.Vec2));

    public static final Property<AlignX> ALIGN_X = add(new Property<>("align-x", PropertyType.Enum, AlignX.Left));
    public static final Property<AlignY> ALIGN_Y = add(new Property<>("align-y", PropertyType.Enum, AlignY.Center));

    public static final Property<Double> FONT_SIZE = add(new Property<>("font-size", PropertyType.Number));
    public static final Property<Color4> TEXT_SHADOW = add(new Property<>("text-shadow", PropertyType.Color4));
    public static final Property<Vec2> TEXT_SHADOW_OFFSET = add(new Property<>("text-shadow-offset", PropertyType.Vec2, new Vec2(1, -1)));

    public static final Property<ListDirection> LIST_DIRECTION = add(new Property<>("list-direction", PropertyType.Enum, ListDirection.Normal));

    private static <T> Property<T> add(Property<T> property) {
        PROPERTIES.put(property.name(), property);
        return property;
    }

    public static Property<?> get(String name) {
        return PROPERTIES.get(name);
    }
}
