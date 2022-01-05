package meteordevelopment.pulsar.rendering;

import meteordevelopment.pulsar.Pulsar;
import meteordevelopment.pulsar.utils.IColor;
import meteordevelopment.pulsar.utils.Vec4;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.*;

public class Mesh {
    public enum Attrib {
        UByte(1),
        Float(1),
        Vec2(2),
        Vec4(4),
        Color(4);

        public final int count;

        Attrib(int size) {
            this.count = size;
        }

        public int size() {
            return switch (this) {
                case UByte, Color -> count;
                default -> count * 4;
            };
        }

        public int type() {
            return switch (this) {
                case UByte, Color -> GL_UNSIGNED_BYTE;
                default -> GL_FLOAT;
            };
        }

        public boolean normalize() {
            return this == Color;
        }
    }

    private final int primitiveVerticesSize;

    private int verticesCapacity;
    private long vertices, verticesI;

    private int indicesCapacity;
    private long indices;

    private final int vao, vbo, ibo;

    private boolean building;
    private int vertexI, indicesCount;

    private double alpha = 1;

    public Mesh(Attrib... attributes) {
        int stride = 0;
        for (Attrib attrib : attributes) stride += attrib.size();

        this.primitiveVerticesSize = stride * 3;

        verticesCapacity = primitiveVerticesSize * 256 * 4;
        vertices = nmemAllocChecked(verticesCapacity);

        indicesCapacity = 3 * 512 * 4;
        indices = nmemAllocChecked(indicesCapacity);

        vao = glGenVertexArrays();
        Pulsar.BIND_VERTEX_ARRAY.accept(vao);

        vbo = glGenBuffers();
        Pulsar.BIND_ARRAY_BUFFER.accept(vbo);

        ibo = glGenBuffers();
        Pulsar.BIND_ELEMENT_ARRAY_BUFFER.accept(ibo);

        int offset = 0;
        for (int i = 0; i < attributes.length; i++) {
            Attrib attrib = attributes[i];

            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i, attrib.count, attrib.type(), attrib.normalize(), stride, offset);

            offset += attrib.size();
        }

        Pulsar.BIND_VERTEX_ARRAY.accept(0);
        Pulsar.BIND_ARRAY_BUFFER.accept(0);
        Pulsar.BIND_ELEMENT_ARRAY_BUFFER.accept(0);
    }

    public void dispose() {
        nmemFree(vertices);
        nmemFree(indices);
    }

    public void begin() {
        if (building) throw new IllegalStateException("Mesh.end() called while already building.");

        verticesI = vertices;
        vertexI = 0;
        indicesCount = 0;

        building = true;
    }

    public void alpha(double alpha) {
        this.alpha = alpha;
    }

    public Mesh uByte(int v) {
        memPutByte(verticesI, (byte) v);

        verticesI++;
        return this;
    }

    public Mesh float_(double v) {
        memPutFloat(verticesI, (float) v);

        verticesI += 4;
        return this;
    }

    public Mesh vec2(double x, double y) {
        memPutFloat(verticesI, (float) x);
        memPutFloat(verticesI + 4, (float) y);

        verticesI += 8;
        return this;
    }

    public Mesh vec4(Vec4 v) {
        memPutFloat(verticesI, (float) v.topRight());
        memPutFloat(verticesI + 4, (float) v.bottomRight());
        memPutFloat(verticesI + 8, (float) v.topLeft());
        memPutFloat(verticesI + 12, (float) v.bottomLeft());

        verticesI += 16;
        return this;
    }

    public Mesh color(IColor c) {
        memPutByte(verticesI, (byte) c.getR());
        memPutByte(verticesI + 1, (byte) c.getG());
        memPutByte(verticesI + 2, (byte) c.getB());
        memPutByte(verticesI + 3, (byte) (c.getA() * alpha));

        verticesI += 4;
        return this;
    }

    public int next() {
        return vertexI++;
    }

    public void line(int i1, int i2) {
        long p = indices + indicesCount * 4L;

        memPutInt(p, i1);
        memPutInt(p + 4, i2);

        indicesCount += 2;
        growIfNeeded();
    }

    public void triangle(int i1, int i2, int i3) {
        long p = indices + indicesCount * 4L;

        memPutInt(p, i1);
        memPutInt(p + 4, i2);
        memPutInt(p + 8, i3);

        indicesCount += 3;
        growIfNeeded();
    }

    public void quad(int i1, int i2, int i3, int i4) {
        long p = indices + indicesCount * 4L;

        memPutInt(p, i1);
        memPutInt(p + 4, i2);
        memPutInt(p + 8, i3);

        memPutInt(p + 12, i3);
        memPutInt(p + 16, i4);
        memPutInt(p + 20, i1);

        indicesCount += 6;
        growIfNeeded();
    }

    public void growIfNeeded() {
        // Vertices
        if ((vertexI + 1) * primitiveVerticesSize >= verticesCapacity) {
            verticesCapacity = verticesCapacity * 2;
            if (verticesCapacity % primitiveVerticesSize != 0) verticesCapacity += verticesCapacity % primitiveVerticesSize;

            long newVertices = nmemAllocChecked(verticesCapacity);
            memCopy(vertices, newVertices, verticesI - vertices);

            verticesI = newVertices + (verticesI - vertices);
            vertices = newVertices;
        }

        // Indices
        if (indicesCount * 4 >= indicesCapacity) {
            indicesCapacity = indicesCapacity * 2;
            if (indicesCapacity % 3 != 0) indicesCapacity += indicesCapacity % (3 * 4);

            long newIndices = nmemAllocChecked(indicesCapacity);
            memCopy(indices, newIndices, indicesCount * 4L);

            indices = newIndices;
        }
    }

    public void end() {
        if (!building) throw new IllegalStateException("Mesh.end() called while not building.");

        if (indicesCount > 0) {
            Pulsar.BIND_ARRAY_BUFFER.accept(vbo);
            nglBufferData(GL_ARRAY_BUFFER, verticesI - vertices, vertices, GL_DYNAMIC_DRAW);
            Pulsar.BIND_ARRAY_BUFFER.accept(0);

            Pulsar.BIND_ELEMENT_ARRAY_BUFFER.accept(ibo);
            nglBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesCount * 4L, indices, GL_DYNAMIC_DRAW);
            Pulsar.BIND_ELEMENT_ARRAY_BUFFER.accept(0);
        }

        building = false;
    }

    public void render(boolean lines) {
        if (building) end();

        if (indicesCount > 0) {
            Pulsar.BIND_VERTEX_ARRAY.accept(vao);
            glDrawElements(lines ? GL_LINES : GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, 0);
            Pulsar.BIND_VERTEX_ARRAY.accept(0);
        }
    }
    public void render() {
        render(false);
    }
}
