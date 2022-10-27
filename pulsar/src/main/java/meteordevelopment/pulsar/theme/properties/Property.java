package meteordevelopment.pulsar.theme.properties;

public record Property<T>(String name, PropertyType<T> type, T defaultValue) {
    public Property(String name, PropertyType<T> type) {
        this(name, type, null);
    }
}
