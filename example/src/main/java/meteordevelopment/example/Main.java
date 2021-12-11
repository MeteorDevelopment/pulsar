package meteordevelopment.example;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Theme;
import meteordevelopment.pulsar.theme.fileresolvers.ResourceFileResolver;
import meteordevelopment.pulsar.theme.parser.Parser;
import meteordevelopment.pulsar.utils.AlignX;
import meteordevelopment.pulsar.widgets.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11C.*;

public class Main {
    private static WWindow createMainWindow() {
        WWindow w = new WWindow("Test Window");

        w.add(new WText("Hello"));
        w.add(new WText("COPE?!?!?!!?").tag("right"));

        WTable t = w.add(new WTable()).expandX().widget();

        t.add(new WText("Something:"));
        t.add(new WDropdown<>(AlignX.Center));
        t.row();

        t.add(new WText("Good:"));
        t.add(new WCheckbox(true));
        t.row();

        t.add(new WText("Text:"));
        t.add(new WTextBox("Cope?")).expandX();
        t.row();

        t.add(new WText("Integer 1:"));
        t.add(new WIntEdit(2, -8, 8, -8, 8));
        t.row();

        t.add(new WText("Integer 2:"));
        t.add(new WIntEdit(2, null, null));
        t.row();

        t.add(new WText("Double 1:"));
        t.add(new WDoubleEdit(2, -8d, 8d, -8, 8));
        t.row();

        t.add(new WText("Double 2:"));
        t.add(new WDoubleEdit(2, null, null));
        t.row();

        w.add(new WButton("Click me")).expandX();

        return w;
    }

    private static WWindow createLoginWindow() {
        WWindow w = new WWindow("Login");
        WTable t = w.add(new WTable()).expandX().widget();

        t.add(new WText("Username:"));
        t.add(new WTextBox("", "Username")).expandX();
        t.row();

        t.add(new WText("Password:"));
        t.add(new WTextBox("", "Password", '*')).expandX();

        w.add(new WButton("Login")).expandX();

        return w;
    }

    public static void main(String[] args) {
        Window window = new Window();
        Renderer renderer = new Renderer();

        Theme theme = Parser.parse(new ResourceFileResolver("/"), "test2.pts");
        //Theme theme = Parser.parse(new NormalFileResolver("/"), "test.pts");
        renderer.setTheme(theme);
        renderer.window = window.handle;

        WRoot root = new WRoot.FullScreen();
        root.setWindowSize(window.getWidth(), window.getHeight());

        root.add(createMainWindow());
        root.add(createLoginWindow());

        window.onResized = () -> root.setWindowSize(window.getWidth(), window.getHeight());
        window.onEvent = root::dispatch;

        double lastTime = glfwGetTime();

        while (!window.shouldClose()) {
            double time = glfwGetTime();
            double delta = time - lastTime;
            lastTime = time;

            window.pollEvents();

            glClearColor(0.9f, 0.9f, 0.9f, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            root.render(renderer, delta);

            window.swapBuffers();
        }
    }
}
