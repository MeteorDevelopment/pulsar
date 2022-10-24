package meteordevelopment.pulsar.theme.properties;

import java.util.List;

public record PropertyConstructor<T>(ValueType[] types, Factory<T> factory) {
    public boolean matches(List<ValueType> types) {
        if (types.size() != this.types.length) return false;

        for (int i = 0; i < types.size(); i++) {
            if (types.get(i) != this.types[i]) return false;
        }

        return true;
    }

    public T create(List<Object> values) {
        return factory.create(values.toArray());
    }

    public interface Factory<T> {
        T create(Object[] values);
    }
}
