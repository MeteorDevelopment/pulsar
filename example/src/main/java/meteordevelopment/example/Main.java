package meteordevelopment.example;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Theme;
import meteordevelopment.pulsar.theme.parser.Parser;
import meteordevelopment.pulsar.widgets.*;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11C.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Window window = new Window();
        Renderer renderer = new Renderer();

        Theme theme = Parser.parse(new InputStreamReader(Main.class.getResourceAsStream("/test2.pts")));
        //Theme theme = Parser.parse(new FileReader("test.pts"));
        renderer.theme = theme;
        renderer.window = window.handle;

        WContainer widget = new WWindow("Test Window").minWidth(300);
        widget.add(new WText("Hello"));
        widget.add(new WText("COPE?!?!?!?!").id("right"));

        WContainer a = widget.add(new WHorizontalList()).widget;
        a.add(new WText("Good:"));
        a.add(new WCheckbox(true));

        WContainer b = widget.add(new WHorizontalList()).widget;
        b.add(new WText("Text:"));
        b.add(new WTextBox("Cope?").minWidth(200)).expandX();

        widget.add(new WButton("Click me")).expandX();

        widget.computeStyle(theme);
        widget.calculateSize();

        widget.x = window.getWidth() / 2.0 - widget.width / 2.0;
        widget.y = window.getHeight() / 2.0 - widget.height / 2.0;
        widget.calculateWidgetPositions();

        window.mousePressed = integer -> widget.mousePressed(integer, window.lastMouseX, window.lastMouseY, false);
        window.mouseMoved = widget::mouseMoved;
        window.mouseReleased = integer -> widget.mouseReleased(integer, window.lastMouseX, window.lastMouseY);
        window.keyPressed = widget::keyPressed;
        window.keyRepeated = widget::keyRepeated;
        window.charTyped = widget::charTyped;

        double lastTime = glfwGetTime();

        while (!window.shouldClose()) {
            double time = glfwGetTime();
            double delta = time - lastTime;
            lastTime = time;

            window.pollEvents();

            glClearColor(0.9f, 0.9f, 0.9f, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            widget.computeStyle(theme);
            renderer.begin(window.getWidth(), window.getHeight());
            widget.render(renderer, 0, 0, delta);
            renderer.end();

            window.swapBuffers();
        }
    }
}
