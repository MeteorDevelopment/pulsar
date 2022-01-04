package meteordevelopment.pulsar.rendering;

import meteordevelopment.pulsar.utils.Utils;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20C.*;

public class Shader {
    private static float[] ARRAY = new float[4 * 4];

    private final int id;
    private final Map<String, Integer> uniformLocations = new HashMap<>();

    public Shader(String vertexPath, String fragmentPath) {
        int vert = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vert, Utils.readResourceString(vertexPath)); // TODO
        glCompileShader(vert);

        int frag = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(frag, Utils.readResourceString(fragmentPath)); // TODO
        glCompileShader(frag);

        id = glCreateProgram();

        glAttachShader(id, vert);
        glAttachShader(id, frag);
        glLinkProgram(id);

        glDetachShader(id, vert);
        glDeleteShader(vert);
        glDetachShader(id, frag);
        glDeleteShader(frag);
    }

    public void bind() {
        glUseProgram(id);
    }

    private int getLocation(String name) {
        if (uniformLocations.containsKey(name)) return uniformLocations.get(name);

        int location = glGetUniformLocation(id, name);
        uniformLocations.put(name, location);
        return location;
    }

    public void set(String name, int v) {
        glUniform1i(getLocation(name), v);
    }

    public void set(String name, double x, double y) {
        glUniform2f(getLocation(name), (float) x, (float) y);
    }

    public void set(String name, Matrix4f m) {
        glUniformMatrix4fv(getLocation(name), false, m.get(ARRAY));
    }

    public void set(String name, Texture texture) {
        glUniform1i(getLocation(name), texture.getSlot());
    }
}
