package org.meteordev.example;

import org.meteordev.pulsar.layout.TableLayout;
import org.meteordev.pulsar.rendering.Renderer;
import org.meteordev.pulsar.theme.Theme;
import org.meteordev.pulsar.theme.fileresolvers.ResourceFileResolver;
import org.meteordev.pulsar.theme.parser.Parser;
import org.meteordev.pulsar.widgets.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11C.*;

public class Module {
    public enum Mode {
        Default,
        Copium,
        Beta
    }

    private static WWindow createWindow() {
        WWindow w = new WWindow("Module");

        w.add(new WText("Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quam ab impedit ratione in voluptates autem suscipit veritatis labore?")).tag("description");

        // General
        WSection s = w.add(new WSection("General")).expandX().widget();
        TableLayout t = s.setLayout(new TableLayout());

        s.add(new WText("Mode"));
        s.add(new WDropdown<>(Mode.Default)).expandCellX();
        s.add(new WButton(null)).tag("reset");
        t.row();

        s.add(new WText("Toggle Perspective"));
        s.add(new WCheckbox(true)).expandCellX();
        s.add(new WButton(null)).tag("reset");
        t.row();

        s.add(new WText("Speed"));
        s.add(new WDoubleEdit(1, 0.0, null, 0, 5)).expandX();
        s.add(new WButton(null)).tag("reset");
        t.row();

        // Active
        w.add(new WHorizontalSeparator());
        WHorizontalList l = w.add(new WHorizontalList()).expandX().widget();
        l.add(new WText("Active:"));
        l.add(new WCheckbox(false));

        return w;
    }

    public static void main(String[] args) {
        Window window = new Window("GUI Example", 1280, 720);
        Renderer renderer = new Renderer();

        Theme theme = Parser.parse(new ResourceFileResolver("/meteor"), "theme.pts");
        renderer.setTheme(theme);
        renderer.window = window.handle;

        WRoot root = new WRoot(true);
        root.setWindowSize(window.getWidth(), window.getHeight());

        WWindowManager windows = root.add(new WWindowManager()).expandX().widget();

        windows.add(createWindow());

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
