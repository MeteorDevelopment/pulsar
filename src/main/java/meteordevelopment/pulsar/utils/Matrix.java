package meteordevelopment.pulsar.utils;

import java.nio.FloatBuffer;

import static org.lwjgl.system.MemoryUtil.memSet;

// Code adapted from JOML
public class Matrix {
    public static void identity(FloatBuffer buffer) {
        memSet(buffer, 0);

        buffer.put(0, 1);
        buffer.put(5, 1);
        buffer.put(10, 1);
        buffer.put(15, 1);
    }

    public static FloatBuffer ortho(FloatBuffer buffer, float left, float right, float bottom, float top, float zNear, float zFar) {
        identity(buffer);

        buffer.put(0, 2.0f / (right - left));
        buffer.put(5, 2.0f / (top - bottom));
        buffer.put(10, zNear - zFar);
        buffer.put(12, (right + left) / (left - right));
        buffer.put(13, (top + bottom) / (bottom - top));
        buffer.put(14, (zFar + zNear) / (zNear - zFar));

        return buffer;
    }
}
