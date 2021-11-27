package meteordevelopment.example;

import org.lwjgl.opengl.GL;

import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Window {
    public interface MouseCallback {
        void run(double x, double y, double deltaX, double deltaY);
    }

    private final long handle;
    private int width, height;
    private double lastMouseX, lastMouseY;

    public MouseCallback mouseMoved;
    public Consumer<Integer> mousePressed, mouseReleased;

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

        glfwSetCursorPosCallback(handle, (window, xpos, ypos) -> {
            if (mouseMoved != null) mouseMoved.run(xpos, height - ypos, xpos - lastMouseX, height - ypos - lastMouseY);

            lastMouseX = xpos;
            lastMouseY = height - ypos;
        });

        glfwSetMouseButtonCallback(handle, (window, button, action, mods) -> {
            if (action == GLFW_RELEASE) {
                if (mouseReleased != null) mouseReleased.accept(button);
            }
            else {
                if (mousePressed != null) mousePressed.accept(button);
            }
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
