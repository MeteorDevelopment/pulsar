package meteordevelopment.pts.properties;

import com.github.bsideup.jabel.Desugar;

import java.util.List;

@Desugar
public record PropertyAccessor<T>(String name, ValueType[] types, Setter<T> setter) {
    public boolean matches(String name, List<ValueType> types) {
        if (!this.name.equals(name)) return false;
        if (types.size() != this.types.length) return false;

        for (int i = 0; i < types.size(); i++) {
            if (types.get(i) != this.types[i]) return false;
        }

        return true;
    }

    public void set(T target, List<Object> values) {
        setter.set(target, values.toArray());
    }

    public interface Setter<T> {
        void set(T target, Object[] values);
    }
}
