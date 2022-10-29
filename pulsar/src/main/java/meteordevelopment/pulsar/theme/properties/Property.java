package meteordevelopment.pulsar.theme.properties;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record Property<T>(String name, PropertyType<T> type, T defaultValue) {
    public Property(String name, PropertyType<T> type) {
        this(name, type, null);
    }
}
