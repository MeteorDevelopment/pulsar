package meteordevelopment.pulsar.theme.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PropertyType<T> {
    public final String name;
    public final Supplier<T> accessorDefaultValue;
    public final T defaultValue;

    private final PropertyConstructor<T>[] constructors;
    private final PropertyAccessor<T>[] accessors;
    private final Decomposer<T> decomposer;

    private PropertyType(String name, Supplier<T> accessorDefaultValue, T defaultValue, PropertyConstructor<T>[] constructors, PropertyAccessor<T>[] accessors, Decomposer<T> decomposer) {
        this.name = name;
        this.accessorDefaultValue = accessorDefaultValue;
        this.defaultValue = defaultValue;
        this.constructors = constructors;
        this.accessors = accessors;
        this.decomposer = decomposer;
    }

    public PropertyConstructor<T> getConstructor(List<ValueType> types) {
        for (PropertyConstructor<T> constructor : constructors) {
            if (constructor.matches(types)) return constructor;
        }

        return null;
    }

    public PropertyAccessor<T> getAccessor(String name, List<ValueType> types) {
        for (PropertyAccessor<T> accessor : accessors) {
            if (accessor.matches(name, types)) return accessor;
        }

        return null;
    }

    public void decompose(T value, List<ValueType> types, List<Object> values) {
        decomposer.decompose(value, types, values);
    }

    public static class Builder<T> {
        private final String name;
        private final Supplier<T> accessorDefaultValue;
        private T defaultValue = null;
        private final List<PropertyConstructor<T>> constructors = new ArrayList<>();
        private final List<PropertyAccessor<T>> accessors = new ArrayList<>();
        private Decomposer<T> decomposer;

        public Builder(String name, Supplier<T> accessorDefaultValue) {
            this.name = name;
            this.accessorDefaultValue = accessorDefaultValue;
        }

        public Builder<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder<T> constructor(PropertyConstructor.Factory<T> factory, ValueType... types) {
            constructors.add(new PropertyConstructor<>(types, factory));
            return this;
        }

        public Builder<T> accessor(String name, PropertyAccessor.Setter<T> setter, ValueType... types) {
            accessors.add(new PropertyAccessor<>(name, types, setter));
            return this;
        }

        public Builder<T> decomposer(Decomposer<T> decomposer) {
            this.decomposer = decomposer;
            return this;
        }

        @SuppressWarnings("unchecked")
        public PropertyType<T> build() {
            return new PropertyType<>(
                    name,
                    accessorDefaultValue,
                    defaultValue,
                    constructors.toArray(new PropertyConstructor[0]),
                    accessors.toArray(new PropertyAccessor[0]),
                    decomposer
            );
        }
    }

    public interface Decomposer<T> {
        void decompose(T value, List<ValueType> types, List<Object> values);
    }
}
