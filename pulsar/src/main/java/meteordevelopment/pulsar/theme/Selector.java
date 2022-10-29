package meteordevelopment.pulsar.theme;

import com.github.bsideup.jabel.Desugar;
import meteordevelopment.pulsar.utils.Lists;

import java.util.List;

@Desugar
public record Selector(String[] names, List<String> tags, boolean isHovered, boolean isPressed) implements IStylable {
    @Override
    public String[] names() {
        return names;
    }

    @Override
    public List<String> tags() {
        return tags == null ? Lists.of() : tags;
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
