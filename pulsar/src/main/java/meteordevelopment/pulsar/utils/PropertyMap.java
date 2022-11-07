package meteordevelopment.pulsar.utils;

import meteordevelopment.pts.properties.Properties;
import meteordevelopment.pts.properties.Property;

import java.util.*;

public class PropertyMap implements Map<Property<?>, Object> {
    private Property<?>[] keys = new Property[10];
    private Object[] values = new Object[10];

    private int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < size; i++) {
            if (keys[i] == key) return true;
        }

        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 0; i < size; i++) {
            if (values[i] == value) return true;
        }

        return false;
    }

    @Override
    public Object get(Object key) {
        for (int i = 0; i < size; i++) {
            if (keys[i] == key) return values[i];
        }

        return null;
    }

    @Override
    public Object put(Property<?> key, Object value) {
        for (int i = 0; i < size; i++) {
            if (keys[i] == key) {
                Object previousValue = values[i];
                values[i] = value;
                return previousValue;
            }
        }

        if (size >= values.length) grow();
        values[size++] = value;
        keys[size - 1] = key;

        return null;
    }

    @Override
    public Object remove(Object key) {
        for (int i = 0; i < size; i++) {
            if (keys[i] != key) {
                Object value = values[i];
                size--;

                if (i < size) {
                    keys[i] = keys[size];
                    values[i] = values[size];
                }
                else {
                    keys[i] = null;
                    values[i] = null;
                }

                return value;
            }
        }

        return null;
    }

    @Override
    public void putAll(Map<? extends Property<?>, ?> m) {
        for (Property<?> property : m.keySet()) put(property, m.get(property));
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            keys[i] = null;
            values[i] = null;
        }

        size = 0;
    }

    @Override
    public Set<Property<?>> keySet() {
        return new AbstractSet<>() {
            @Override
            public Iterator<Property<?>> iterator() {
                return new Iterator<>() {
                    private int i;

                    @Override
                    public boolean hasNext() {
                        return i < size;
                    }

                    @Override
                    public Property<?> next() {
                        return keys[i++];
                    }
                };
            }

            @Override
            public int size() {
                return size;
            }
        };
    }

    @Override
    public Collection<Object> values() {
        return new AbstractSet<>() {
            @Override
            public Iterator<Object> iterator() {
                return new Iterator<>() {
                    private int i;

                    @Override
                    public boolean hasNext() {
                        return i < size;
                    }

                    @Override
                    public Object next() {
                        return values[i++];
                    }
                };
            }

            @Override
            public int size() {
                return size;
            }
        };
    }

    @Override
    public Set<Entry<Property<?>, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }

    private void grow() {
        int capacity = Utils.clamp((int) (values.length * 1.75), values.length + 1, Properties.getAll().size());

        Property<?>[] newProperties = new Property[capacity];
        System.arraycopy(keys, 0, newProperties, 0, size);
        keys = newProperties;

        Object[] newValues = new Object[capacity];
        System.arraycopy(values, 0, newValues, 0, size);
        values = newValues;
    }
}
