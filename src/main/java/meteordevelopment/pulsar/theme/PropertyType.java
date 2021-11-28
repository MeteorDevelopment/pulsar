package meteordevelopment.pulsar.theme;

import meteordevelopment.pulsar.utils.Vec2;
import meteordevelopment.pulsar.utils.Vec4;

public enum PropertyType {
    Number(0.0),
    Vec2(new Vec2(0.0)),
    Vec4(new Vec4(0.0)),
    Color(null),
    Color4(null),
    Enum(null);

    public final Object value;

    PropertyType(Object value) {
        this.value = value;
    }
}
