package meteordevelopment.pulsar.theme;

import java.util.List;

public record Selector(String[] names, List<String> tags, boolean isHovered, boolean isPressed) implements IStylable {
    @Override
    public String[] names() {
        return names;
    }

    @Override
    public List<String> tags() {
        return tags == null ? List.of() : tags;
    }

    @Override
    public boolean isHovered() {
        return isHovered;
    }

    @Override
    public boolean isPressed() {
        return isPressed;
    }
}
