package meteordevelopment.pulsar.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Utils {
    public static boolean IS_MAC = System.getProperty("os.name").contains("Mac");

    public static byte[] read(InputStream in) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];

        try {
            for (int count; (count = in.read(bytes)) != -1;) {
                result.write(bytes, 0, count);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result.toByteArray();
    }

    public static byte[] readResource(String path) {
        InputStream in = Utils.class.getResourceAsStream(path);
        if (in == null) return new byte[0];

        return read(in);
    }

    public static String readResourceString(String path) {
        return new String(readResource(path), StandardCharsets.UTF_8);
    }

    public static <T> T[] combine(T[] a, T... b) {
        T[] result = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) return min;
        return Math.min(value, max);
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) return min;
        return Math.min(value, max);
    }
}
