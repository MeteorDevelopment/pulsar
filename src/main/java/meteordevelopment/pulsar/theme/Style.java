package meteordevelopment.pulsar.theme;

import java.util.HashMap;
import java.util.Map;

public class Style {
    private final Map<Property<?>, Object> properties = new HashMap<>();

    public <T> void set(Property<T> property, T value) {
        properties.put(property, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Property<T> property) {
        T value = (T) properties.get(property);
        if (value == null) value = property.defaultValue();
        return value != null ? value : (T) property.type().value;
    }

    void merge(Style style) {
        for (Property<?> property : style.properties.keySet()) {
            properties.put(property, style.properties.get(property));
        }
    }
}
