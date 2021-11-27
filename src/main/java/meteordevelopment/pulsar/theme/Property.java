package meteordevelopment.pulsar.theme;

public record Property<T>(String name, PropertyType type, T defaultValue) {
    public Property(String name, PropertyType type) {
        this(name, type, null);
    }
}
