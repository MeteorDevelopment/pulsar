package meteordevelopment.example;

import meteordevelopment.pulsar.rendering.Renderer;
import meteordevelopment.pulsar.theme.Theme;
import meteordevelopment.pulsar.theme.parser.Parser;
import meteordevelopment.pulsar.widgets.*;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL11C.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Window window = new Window();
        Renderer renderer = new Renderer();

        Theme theme = Parser.parse(new InputStreamReader(Main.class.getResourceAsStream("/test2.pts")));
        //Theme theme = Parser.parse(new FileReader("test.pts"));
        renderer.theme = theme;

        WContainer widget = new WWindow("Test Window").minWidth(240);
        widget.add(new WText("Hello"));
        widget.add(new WText("COPE?!?!?!?!").id("right"));

        WContainer a = widget.add(new WHorizontalList()).widget;
        a.add(new WText("Good:"));
        a.add(new WCheckbox(true));

        widget.add(new WButton("Click me")).expandX();

        widget.computeStyle(theme);
        widget.calculateSize();

        widget.x = window.getWidth() / 2.0 - widget.width / 2.0;
        widget.y = window.getHeight() / 2.0 - widget.height / 2.0;
        widget.calculateWidgetPositions();

        window.mouseMoved = widget::mouseMoved;
        window.mousePressed = widget::mousePressed;
        window.mouseReleased = widget::mouseReleased;

        while (!window.shouldClose()) {
            window.pollEvents();

            glClearColor(0.9f, 0.9f, 0.9f, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            widget.computeStyle(theme);
            renderer.begin();
            widget.render(renderer, 0, 0, 0);
            renderer.end(window.getWidth(), window.getHeight());

            window.swapBuffers();
        }
    }
}
