package meteordevelopment.example;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Theme;
import meteordevelopment.pulsar.theme.fileresolvers.ResourceFileResolver;
import meteordevelopment.pulsar.theme.parser.Parser;
import meteordevelopment.pulsar.widgets.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11C.*;

public class Main {
    public static void main(String[] args) {
        Window window = new Window();
        Renderer renderer = new Renderer();

        Theme theme = Parser.parse(new ResourceFileResolver("/"), "test2.pts");
        //Theme theme = Parser.parse(new NormalFileResolver("/"), "test.pts");
        renderer.setTheme(theme);
        renderer.window = window.handle;

        WRoot root = new WRoot.FullScreen();
        root.setWindowSize(window.getWidth(), window.getHeight());

        WWindow w = root.add(new WWindow("Test Window")).widget();

        w.add(new WText("Hello"));
        w.add(new WText("COPE?!?!?!!?").tag("right"));

        WTable t = w.add(new WTable()).expandX().widget();

        t.add(new WText("Good:"));
        t.add(new WCheckbox(true));
        t.row();

        t.add(new WText("Text:"));
        t.add(new WTextBox("Cope?")).expandX();
        t.row();

        t.add(new WText("Number:"));
        t.add(new WSlider(4, 0, 10)).expandX();

        w.add(new WButton("Click me")).expandX();

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
