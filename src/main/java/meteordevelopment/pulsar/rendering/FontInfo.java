package meteordevelopment.pulsar.rendering;

import meteordevelopment.pulsar.utils.Utils;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryUtil;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class FontInfo {
    public final STBTTFontinfo fontInfo;
    public final ByteBuffer buffer;

    public FontInfo(InputStream in) {
        byte[] data = Utils.read(in);
        buffer = MemoryUtil.memAlloc(data.length).put(data);
        buffer.rewind();

        fontInfo = STBTTFontinfo.create();
        STBTruetype.stbtt_InitFont(fontInfo, buffer);
    }

    public void dispose() {
        MemoryUtil.memFree(buffer);
    }
}
