package meteordevelopment.pulsar.rendering;

import java.io.*;
import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memByteBufferSafe;

public class Svg {
    static {
        String os = System.getProperty("os.name").toLowerCase();
        String path = "/pulsar/natives/libplutojni.";

        if (os.contains("win")) path += "dll";
        else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) path += "so";
        else if (os.contains("mac") || os.contains("darwin")) path += "dylib";
        else throw new RuntimeException("Unsupported operating system '" + os + "'");

        try {
            File temp = File.createTempFile("libplutojni", null);

            InputStream in = Svg.class.getResourceAsStream(path);
            OutputStream out = new FileOutputStream(temp);

            byte[] bytes = new byte[1024];
            int count;
            while ((count = in.read(bytes)) > 0) out.write(bytes, 0, count);

            out.close();
            in.close();

            System.load(temp.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // plutovg_surface_t* plutosvg_load_from_memory(const char* data, int size, plutovg_font_t* font, int width, int height, double dpi);
    private static native long plutojni_load_from_memory(long buffer, int len, long font, int width, int height, double dpi);

    public static long loadFromMemory(ByteBuffer buffer, long font, int width, int height, double dpi) {
        return plutojni_load_from_memory(memAddress(buffer), buffer.remaining(), font, width, height, dpi);
    }

    // unsigned char* plutovg_surface_get_data(const plutovg_surface_t* surface);
    private static native long plutojni_surface_get_data(long surface);

    public static ByteBuffer getData(long surface, int width, int height) {
        long result = plutojni_surface_get_data(surface);
        return memByteBufferSafe(result, width * height * 4);
    }

    // void plutovg_surface_destroy(plutovg_surface_t* surface);
    private static native void plutojni_surface_destroy(long surface);

    public static void destroy(long surface) {
        plutojni_surface_destroy(surface);
    }
}
