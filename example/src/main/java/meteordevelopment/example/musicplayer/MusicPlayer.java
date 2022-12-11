package meteordevelopment.example.musicplayer;

import meteordevelopment.example.Window;
import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Theme;
import meteordevelopment.pulsar.theme.fileresolvers.ResourceFileResolver;
import meteordevelopment.pulsar.theme.parser.Parser;
import meteordevelopment.pulsar.widgets.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11C.*;

public class MusicPlayer {
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }

        return false;
    }

    private static void fillSongs(Widget list, String searchText) {
        for (Song song : Player.SONGS) {
            if (!containsIgnoreCase(song.getSearchString(), searchText)) continue;

            WHorizontalList songW = list.add(new WHorizontalList()).tag("song").expandX().widget();

            songW.add(new WImage(song.getImage(), song.getImageWidth(), song.getImageHeight()));

            WVerticalList textW = songW.add(new WVerticalList()).expandX().widget();
            textW.add(new WText(song.getTitle())).tag("title");
            textW.add(new WText(song.getArtist())).tag("author");

            WButton play = songW.add(new WButton(null)).tag("play").widget();
            play.checkIcon();
            play.action = () -> Player.play(song);
        }
    }

    private static WVerticalList songs;

    private static Widget create() {
        WVerticalList list = new WVerticalList();
        list.tag("main");

        WVerticalList top = list.add(new WVerticalList()).tag("songs").expandX().widget();
        WTextBox searchBar = top.add(new WTextBox("", "Search")).expandX().widget();
        top.add(new WHorizontalSeparator()).expandX();

        songs = list.add(new WVerticalList()).tag("songs").expandX().widget();

        searchBar.setFocused(true);
        searchBar.action = () -> {
            songs.clear();
            fillSongs(songs, searchBar.get());
        };

        fillSongs(songs, searchBar.get());

        return list;
    }

    public static void main(String[] args) {
        Player.load();

        Window window = new Window("Pulsar Player", 600, 400);
        Renderer renderer = new Renderer();

        Theme theme = Parser.parse(new ResourceFileResolver("/pulsar-player"), "theme.pts");
        renderer.setTheme(theme);
        renderer.window = window.handle;

        WRoot root = new WRoot(true);
        root.setWindowSize(window.getWidth(), window.getHeight());

        root.add(create()).expandX();

        window.onResized = root::setWindowSize;
        window.onEvent = root::dispatch;

        double lastTime = glfwGetTime();

        while (!window.shouldClose()) {
            double time = glfwGetTime();
            double delta = time - lastTime;
            lastTime = time;

            window.pollEvents();

            glClearColor(0.3f, 0.3f, 0.3f, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            root.render(renderer, delta);

            window.swapBuffers();
        }
    }
}
