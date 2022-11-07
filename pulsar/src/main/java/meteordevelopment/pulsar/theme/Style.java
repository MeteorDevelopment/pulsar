package meteordevelopment.pulsar.theme;

import meteordevelopment.pts.properties.Property;
import meteordevelopment.pulsar.utils.PropertyMap;

import java.util.List;
import java.util.Map;

public class Style {
    public enum State {
        Normal,
        Hovered,
        Pressed;

        public static State of(String state) {
            if (state == null) return Normal;
            if (state.equalsIgnoreCase("hovered")) return Hovered;
            if (state.equalsIgnoreCase("pressed")) return Pressed;
            return Normal;
        }
    }

    public String name;
    public List<String> tags;
    public State state = State.Normal;

    private final Map<Property<?>, Object> properties = new PropertyMap();

    public <T> void set(Property<T> property, T value) {
        properties.put(property, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRaw(Property<T> property) {
        return (T) properties.get(property);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Property<T> property) {
        T value = (T) properties.get(property);
        if (value == null) value = property.defaultValue();
        return value != null ? value : property.type().defaultValue;
    }

    public void merge(Style style) {
        properties.putAll(style.properties);
    }

    @Override
    public String toString() {
        return "Style{" +
                "name='" + name + '\'' +
                ", tags=" + tags +
                ", state='" + state + '\'' +
                '}';
    }
}
