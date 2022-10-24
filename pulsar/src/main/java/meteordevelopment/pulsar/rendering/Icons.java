package meteordevelopment.pulsar.rendering;

import meteordevelopment.pulsar.theme.Theme;
import org.lwjgl.system.MemoryUtil;

import java.util.HashMap;
import java.util.Map;

public class Icons {
    private Theme theme;

    private final TextureAtlas atlas = new TextureAtlas();
    private final Map<String, Map<Integer, TextureRegion>> icons = new HashMap<>();

    public void setTheme(Theme theme) {
        this.theme = theme;

        atlas.clear();
        icons.clear();
    }

    public TextureRegion get(String path, int size) {
        Map<Integer, TextureRegion> icons = this.icons.computeIfAbsent(path, s -> new HashMap<>());

        TextureRegion region = icons.get(size);
        if (region != null) return region;

        long surface = Svg.loadFromMemory(theme.readFile(path), MemoryUtil.NULL, size, size, size * 3);
        region = atlas.add(Svg.getData(surface, size, size), size, size);
        Svg.destroy(surface);

        icons.put(size, region);
        return region;
    }

    public Texture bind() {
        return atlas.bind();
    }
}
