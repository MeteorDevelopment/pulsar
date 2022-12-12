package org.meteordev.example;

import org.meteordev.pts.properties.Properties;
import org.meteordev.pts.utils.Overflow;
import org.meteordev.pulsar.rendering.Renderer;
import org.meteordev.pulsar.theme.Theme;
import org.meteordev.pulsar.theme.fileresolvers.ResourceFileResolver;
import org.meteordev.pulsar.theme.parser.Parser;
import org.meteordev.pts.utils.AlignX;
import org.meteordev.pulsar.widgets.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11C.*;

public class Main {
    private static WWindow createMainWindow() {
        WWindow w = new WWindow("Test Window");

        w.add(new WText("Hello"));
        w.add(new WText("COPE?!?!?!!?")).right();
        w.add(new WHorizontalSeparator("Something"));

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

        t.add(new WHorizontalSeparator("Numbers"));
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

        w.add(new WHorizontalSeparator());

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

    private static WWindow createLongWindow() {
        WWindow w = new WWindow("Long");
        w.bodySet(Properties.MAX_HEIGHT, 200.0);
        w.bodySet(Properties.OVERFLOW_Y, Overflow.Scroll);

        for (int i = 0; i < 20; i++) {
            w.add(new WText("Item: " + i));
        }

        return w;
    }

    public static void main(String[] args) {
        Window window = new Window("GUI Example", 1280, 720);
        Renderer renderer = new Renderer();

        Theme theme = Parser.parse(new ResourceFileResolver("/white-red"), "theme.pts");
        renderer.setTheme(theme);
        renderer.window = window.handle;

        WRoot root = new WRoot(false);
        root.setWindowSize(window.getWidth(), window.getHeight());

        WWindowManager windows = root.add(new WWindowManager()).expandX().widget();

        windows.add(createMainWindow());
        windows.add(createLoginWindow());
        windows.add(createLongWindow());

        window.onResized = root::setWindowSize;
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
