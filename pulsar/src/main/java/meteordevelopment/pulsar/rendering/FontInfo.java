package meteordevelopment.pulsar.rendering;

import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class FontInfo {
    public final STBTTFontinfo fontInfo;
    public final ByteBuffer buffer;

    public FontInfo(ByteBuffer buffer) {
        fontInfo = STBTTFontinfo.create();
        STBTruetype.stbtt_InitFont(fontInfo, buffer);

        this.buffer = buffer;
    }

    public void dispose() {
        MemoryUtil.memFree(buffer);
    }
}
