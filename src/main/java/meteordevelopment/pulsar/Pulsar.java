package meteordevelopment.pulsar;

import org.lwjgl.opengl.GL15C;
import org.lwjgl.opengl.GL30C;

import java.util.function.IntConsumer;

public class Pulsar {
    public static IntConsumer BIND_VERTEX_ARRAY = GL30C::glBindVertexArray;
    public static IntConsumer BIND_ARRAY_BUFFER = value -> GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, value);
    public static IntConsumer BIND_ELEMENT_ARRAY_BUFFER = value -> GL15C.glBindBuffer(GL15C.GL_ELEMENT_ARRAY_BUFFER, value);
}
