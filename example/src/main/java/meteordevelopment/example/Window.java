package meteordevelopment.example;

import org.lwjgl.opengl.GL;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Window {
    public interface MouseCallback {
        void run(double x, double y, double deltaX, double deltaY);
    }

    public final long handle;
    private int width, height;
    public double lastMouseX, lastMouseY;

    public MouseCallback mouseMoved;
    public Consumer<Integer> mousePressed, mouseReleased;
    public BiConsumer<Integer, Integer> keyPressed, keyRepeated;
    public Consumer<Character> charTyped;

    public Window() {
        glfwInit();

        width = 1280;
        height = 720;

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        handle = glfwCreateWindow(width, height, "GUI Example", 0, 0);

        glfwMakeContextCurrent(handle);
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glfwSetWindowSizeCallback(handle, (window, width1, height1) -> {
            width = width1;
            height = height1;

            glViewport(0, 0, width1, height1);
        });

        glfwSetMouseButtonCallback(handle, (window, button, action, mods) -> {
            if (action == GLFW_RELEASE) {
                if (mouseReleased != null) mouseReleased.accept(button);
            }
            else {
                if (mousePressed != null) mousePressed.accept(button);
            }
        });

        glfwSetCursorPosCallback(handle, (window, xpos, ypos) -> {
            if (mouseMoved != null) mouseMoved.run(xpos, height - ypos, xpos - lastMouseX, height - ypos - lastMouseY);

            lastMouseX = xpos;
            lastMouseY = height - ypos;
        });

        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if (keyPressed != null && action == GLFW_PRESS) keyPressed.accept(key, mods);
            else if (keyRepeated != null && action == GLFW_REPEAT) keyRepeated.accept(key, mods);
        });

        glfwSetCharCallback(handle, (window, codepoint) -> {
            if (charTyped != null) charTyped.accept((char) codepoint);
        });

        glfwSwapInterval(1);
        glfwShowWindow(handle);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public void swapBuffers() {
        glfwSwapBuffers(handle);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
