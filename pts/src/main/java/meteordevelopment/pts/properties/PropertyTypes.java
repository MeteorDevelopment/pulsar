package meteordevelopment.pts.properties;

import meteordevelopment.pts.utils.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PropertyTypes {
    private static final Map<String, PropertyType<?>> TYPES = new HashMap<>();

    public static final PropertyType<Double> UNIT_TYPE = add(new PropertyType.Builder<>("Unit", () -> 0.0)
            .defaultValue(0.0)
            .constructor(values -> (double) values[0], ValueType.Unit)
            .decomposer((value, types, values) -> {
                types.add(ValueType.Unit);
                values.add(value);
            })
            .build());

    public static final PropertyType<AlignX> ALIGN_X_TYPE = enumType("AlignX", AlignX.Left);
    public static final PropertyType<AlignY> ALIGN_Y_TYPE = enumType("AlignY", AlignY.Center);
    public static final PropertyType<ListDirection> LIST_DIRECTION_TYPE = enumType("ListDirection", ListDirection.Normal);
    public static final PropertyType<Overflow> OVERFLOW_TYPE = enumType("Overflow", Overflow.Visible);

    public static final PropertyType<IColor> COLOR_TYPE = add(new PropertyType.Builder<>("Color", () -> ColorFactory.create(255, 255, 255, 255))
            .constructor(values -> (IColor) values[0], ValueType.Color)
            .decomposer((value, types, values) -> {
                types.add(ValueType.Color);
                values.add(value);
            })
            .build());

    public static final PropertyType<String> STRING_TYPE = add(new PropertyType.Builder<>("String", () -> "")
            .constructor(values -> (String) values[0], ValueType.String)
            .decomposer((value, types, values) -> {
                types.add(ValueType.String);
                values.add(value);
            })
            .build());

    public static final PropertyType<Vec2> VEC2_TYPE = add(new PropertyType.Builder<>("Vec2", () -> new Vec2(0.0))
            .defaultValue(new Vec2(0.0))
            .constructor(values -> new Vec2((double) values[0]), ValueType.Unit)
            .constructor(values -> new Vec2((double) values[0], (double) values[1]), ValueType.Unit, ValueType.Unit)
            .accessor("x", (target, values) -> target.x = (double) values[0], ValueType.Unit)
            .accessor("y", (target, values) -> target.y = (double) values[0], ValueType.Unit)
            .decomposer((value, types, values) -> {
                types.add(ValueType.Unit);
                values.add(value.x);

                types.add(ValueType.Unit);
                values.add(value.y);
            })
            .build());

    public static final PropertyType<Vec4> VEC4_TYPE = add(new PropertyType.Builder<>("Vec4", () -> new Vec4(0.0))
            .defaultValue(new Vec4(0.0))
            .constructor(values -> new Vec4((double) values[0]), ValueType.Unit)
            .constructor(values -> new Vec4((double) values[0], (double) values[1], (double) values[2], (double) values[3]), ValueType.Unit, ValueType.Unit, ValueType.Unit, ValueType.Unit)
            .accessor("x", (target, values) -> target.x = (double) values[0], ValueType.Unit)
            .accessor("y", (target, values) -> target.y = (double) values[0], ValueType.Unit)
            .accessor("z", (target, values) -> target.z = (double) values[0], ValueType.Unit)
            .accessor("w", (target, values) -> target.w = (double) values[0], ValueType.Unit)
            .accessor("top", (target, values) -> target.top((double) values[0]), ValueType.Unit)
            .accessor("right", (target, values) -> target.right((double) values[0]), ValueType.Unit)
            .accessor("bottom", (target, values) -> target.bottom((double) values[0]), ValueType.Unit)
            .accessor("left", (target, values) -> target.left((double) values[0]), ValueType.Unit)
            .accessor("horizontal", (target, values) -> target.horizontal((double) values[0]), ValueType.Unit)
            .accessor("horizontal", (target, values) -> {
                target.left((double) values[0]);
                target.right((double) values[1]);
            }, ValueType.Unit, ValueType.Unit)
            .accessor("vertical", (target, values) -> target.vertical((double) values[0]), ValueType.Unit)
            .accessor("vertical", (target, values) -> {
                target.top((double) values[0]);
                target.bottom((double) values[1]);
            }, ValueType.Unit, ValueType.Unit)
            .accessor("top-left", (target, values) -> target.topLeft((double) values[0]), ValueType.Unit)
            .accessor("top-right", (target, values) -> target.topRight((double) values[0]), ValueType.Unit)
            .accessor("bottom-right", (target, values) -> target.bottomRight((double) values[0]), ValueType.Unit)
            .accessor("bottom-left", (target, values) -> target.bottomLeft((double) values[0]), ValueType.Unit)
            .decomposer((value, types, values) -> {
                types.add(ValueType.Unit);
                values.add(value.x);

                types.add(ValueType.Unit);
                values.add(value.y);

                types.add(ValueType.Unit);
                values.add(value.z);

                types.add(ValueType.Unit);
                values.add(value.w);
            })
            .build());

    public static final PropertyType<Color4> COLOR4_TYPE = add(new PropertyType.Builder<>("Color4", () -> new Color4(ColorFactory.create(255, 255, 255, 255)))
            .constructor(values -> new Color4((IColor) values[0]), ValueType.Color)
            .constructor(values -> new Color4((IColor) values[0], (IColor) values[1], (IColor) values[2], (IColor) values[3]), ValueType.Color, ValueType.Color, ValueType.Color, ValueType.Color)
            .accessor("top-left", (target, values) -> target.topLeft = (IColor) values[0], ValueType.Color)
            .accessor("top-right", (target, values) -> target.topRight = (IColor) values[0], ValueType.Color)
            .accessor("bottom-right", (target, values) -> target.bottomLeft = (IColor) values[0], ValueType.Color)
            .accessor("bottom-left", (target, values) -> target.bottomLeft = (IColor) values[0], ValueType.Color)
            .decomposer((value, types, values) -> {
                types.add(ValueType.Color);
                values.add(value.topLeft);

                types.add(ValueType.Color);
                values.add(value.topRight);

                types.add(ValueType.Color);
                values.add(value.bottomRight);

                types.add(ValueType.Color);
                values.add(value.bottomLeft);
            })
            .build());

    private static <T> PropertyType<T> add(PropertyType<T> type) {
        TYPES.put(type.name, type);
        return type;
    }

    public static PropertyType<?> get(String name) {
        return TYPES.get(name);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> PropertyType<T> enumType(String name, final T defaultValue) {
        return add(new PropertyType.Builder<>(name, () -> defaultValue)
                .defaultValue(defaultValue)
                .constructor(values -> {
                    try {
                        String text = (String) values[0];

                        Method valuesMethod = defaultValue.getClass().getDeclaredMethod("values");
                        Enum<T>[] enums = (Enum<T>[]) valuesMethod.invoke(null);

                        for (Enum<T> value2 : enums) {
                            if (value2.name().equalsIgnoreCase(text)) {
                                return (T) value2;
                            }
                        }

                        return null;
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }, ValueType.Identifier)
                .decomposer((value1, types, values) -> {
                    types.add(ValueType.Identifier);
                    values.add(value1.name());
                })
                .build());
    }
}
