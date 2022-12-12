package org.meteordev.example;

import com.udojava.evalex.Expression;
import org.meteordev.pulsar.rendering.Renderer;
import org.meteordev.pulsar.theme.Theme;
import org.meteordev.pulsar.theme.fileresolvers.ResourceFileResolver;
import org.meteordev.pulsar.theme.parser.Parser;
import org.meteordev.pulsar.widgets.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11C.*;

public class Calculator {
    private static WText text;

    private static void button(WTable table, String number, String tag) {
        WButton button = table.add(new WButton(number)).expandX().widget();
        button.action = () -> text.setText(text.getText() + number);

        if (tag != null) button.tag(tag);
    }

    private static void button(WTable table, String number) {
        button(table, number, null);
    }

    private static Widget create() {
        WVerticalList list = new WVerticalList();

        text = list.add(new WText("")).widget();

        WTable table = list.add(new WTable()).expandX().widget();

        WButton bA = table.add(new WButton("A")).expandX().tag("secondary").widget();
        WButton bC = table.add(new WButton("C")).expandX().tag("secondary").widget();
        bC.action = () -> text.setText("");
        WButton bRemove = table.add(new WButton("<-")).expandX().tag("secondary").widget();
        bRemove.action = () -> {
            if (text.getText().length() > 0) text.setText(text.getText().substring(0, text.getText().length() - 1));
        };
        button(table, "/", "secondary");

        table.row();

        button(table, "7");
        button(table, "8");
        button(table, "9");
        button(table, "*", "secondary");

        table.row();

        button(table, "4");
        button(table, "5");
        button(table, "6");
        button(table, "-", "secondary");

        table.row();

        button(table, "1");
        button(table, "2");
        button(table, "3");
        button(table, "+", "secondary");

        table.row();

        button(table, "-");
        button(table, "0");
        button(table, ".");
        WButton bEquals = table.add(new WButton("=")).expandX().tag("equals").widget();
        bEquals.action = () -> {
            Expression expression = new Expression(text.getText());
            text.setText(expression.eval().toString());
        };

        return list;
    }

    public static void main(String[] args) {
        Window window = new Window("Calculator", 321, 331);
        Renderer renderer = new Renderer();

        Theme theme = Parser.parse(new ResourceFileResolver("/calculator"), "theme.pts");
        renderer.setTheme(theme);
        renderer.window = window.handle;

        WRoot root = new WRoot(false);
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
