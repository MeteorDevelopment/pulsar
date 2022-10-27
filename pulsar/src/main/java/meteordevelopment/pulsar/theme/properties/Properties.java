package meteordevelopment.pulsar.theme.properties;

import meteordevelopment.pulsar.utils.*;

import java.util.HashMap;
import java.util.Map;

import static meteordevelopment.pulsar.theme.properties.PropertyTypes.*;

public class Properties {
    private static final Map<String, Property<?>> PROPERTIES = new HashMap<>();

    public static final Property<Color4> COLOR = add(new Property<>("color", COLOR4_TYPE));
    public static final Property<Color4> BACKGROUND_COLOR = add(new Property<>("background-color", COLOR4_TYPE));

    public static final Property<Double> OUTLINE_SIZE = add(new Property<>("outline-size", UNIT_TYPE));
    public static final Property<Color4> OUTLINE_COLOR = add(new Property<>("outline-color", COLOR4_TYPE));

    public static final Property<Vec2> SPACING = add(new Property<>("spacing", VEC2_TYPE));
    public static final Property<Vec4> PADDING = add(new Property<>("padding", VEC4_TYPE));
    public static final Property<Vec4> RADIUS = add(new Property<>("radius", VEC4_TYPE));
    public static final Property<Vec2> SIZE = add(new Property<>("size", VEC2_TYPE));
    public static final Property<Vec2> MINIMUM_SIZE = add(new Property<>("minimum-size", VEC2_TYPE));

    public static final Property<AlignX> ALIGN_X = add(new Property<>("align-x", ALIGN_X_TYPE));
    public static final Property<AlignY> ALIGN_Y = add(new Property<>("align-y", ALIGN_Y_TYPE));

    public static final Property<Double> FONT_SIZE = add(new Property<>("font-size", UNIT_TYPE));
    public static final Property<Color4> TEXT_SHADOW = add(new Property<>("text-shadow", COLOR4_TYPE));
    public static final Property<Vec2> TEXT_SHADOW_OFFSET = add(new Property<>("text-shadow-offset", VEC2_TYPE, new Vec2(1, -1)));

    public static final Property<Double> MAX_WIDTH = add(new Property<>("max-width", UNIT_TYPE));
    public static final Property<Double> MAX_HEIGHT = add(new Property<>("max-height", UNIT_TYPE));

    public static final Property<ListDirection> LIST_DIRECTION = add(new Property<>("list-direction", LIST_DIRECTION_TYPE));

    public static final Property<String> ICON = add(new Property<>("icon", STRING_TYPE));
    public static final Property<String> ICON_PATH = add(new Property<>("icon-path", STRING_TYPE));

    private static <T> Property<T> add(Property<T> property) {
        PROPERTIES.put(property.name(), property);
        return property;
    }

    public static Property<?> get(String name) {
        return PROPERTIES.get(name);
    }
}
