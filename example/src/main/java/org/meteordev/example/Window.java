package org.meteordev.example;

import org.lwjgl.opengl.GL;
import org.meteordev.pulsar.input.*;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Window {
    public final long handle;
    private int width, height;
    public double lastMouseX, lastMouseY;

    private final MouseButtonEvent mouseButtonEvent = new MouseButtonEvent();
    private final MouseMovedEvent mouseMovedEvent = new MouseMovedEvent();
    private final MouseScrolledEvent mouseScrolledEvent = new MouseScrolledEvent();
    private final KeyEvent keyEvent = new KeyEvent();
    private final CharTypedEvent charTypedEvent = new CharTypedEvent();

    public BiConsumer<Integer, Integer> onResized;
    public Consumer<Event> onEvent;

    public Window(String title, int width, int height) {
        glfwInit();

        this.width = width;
        this.height = height;

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        handle = glfwCreateWindow(width, height, title, 0, 0);

        glfwMakeContextCurrent(handle);
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glfwSetWindowSizeCallback(handle, (window, width1, height1) -> {
            this.width = width1;
            this.height = height1;

            glViewport(0, 0, width1, height1);

            if (onResized != null) onResized.accept(this.width, this.height);
        });

        glfwSetMouseButtonCallback(handle, (window, button, action, mods) -> {
            if (onEvent != null) onEvent.accept(mouseButtonEvent.set(action == GLFW_RELEASE ? EventType.MouseReleased : EventType.MousePressed, lastMouseX, lastMouseY, button));
        });

        glfwSetCursorPosCallback(handle, (window, xpos, ypos) -> {
            if (onEvent != null) onEvent.accept(mouseMovedEvent.set(xpos, ypos, xpos - lastMouseX, ypos - lastMouseY));

            lastMouseX = xpos;
            lastMouseY = ypos;
        });

        glfwSetScrollCallback(handle, (window, xoffset, yoffset) -> {
            if (onEvent != null) onEvent.accept(mouseScrolledEvent.set(yoffset));
        });

        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if (onEvent != null) {
                if (action == GLFW_PRESS) onEvent.accept(keyEvent.set(EventType.KeyPressed, key, mods));
                else if (action == GLFW_REPEAT) onEvent.accept(keyEvent.set(EventType.KeyRepeated, key, mods));
            }
        });

        glfwSetCharCallback(handle, (window, codepoint) -> {
            if (onEvent != null) onEvent.accept(charTypedEvent.set((char) codepoint));
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
