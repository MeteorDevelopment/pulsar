package org.meteordev.example.musicplayer;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jl.decoder.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageResize;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

import static org.lwjgl.openal.AL10.*;

public class Song {
    private final Path path;
    private final Mp3File mp3;

    private ByteBuffer image;
    private int imageWidth, imageHeight;

    public Song(Path path) {
        try {
            this.path = path;
            this.mp3 = new Mp3File(path);
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            throw new RuntimeException(e);
        }

        if (mp3.getId3v2Tag().getAlbumImageMimeType() != null) {
            byte[] bytes = mp3.getId3v2Tag().getAlbumImage();
            ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length).put(bytes).rewind();

            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.ints(0);
                IntBuffer height = stack.ints(0);
                IntBuffer channels = stack.ints(0);

                ByteBuffer original = STBImage.stbi_load_from_memory(buffer, width, height, channels, 4);
                int originalImageWidth = width.get(0);
                int originalImageHeight = height.get(0);

                imageWidth = 50;
                imageHeight = 50;
                image = MemoryUtil.memAlloc(imageWidth * imageHeight * 4);

                STBImageResize.stbir_resize_uint8(original, originalImageWidth, originalImageHeight, 0, image, imageWidth, imageHeight, 0, 4);

                STBImage.stbi_image_free(original);
            }

            MemoryUtil.memFree(buffer);
        }
    }

    public String getTitle() {
        return mp3.getId3v2Tag().getTitle().trim();
    }

    public String getArtist() {
        return mp3.getId3v2Tag().getArtist().trim();
    }

    public String getSearchString() {
        return getArtist() + " - " + getTitle();
    }

    public int getSampleRate() {
        return mp3.getSampleRate();
    }

    public ByteBuffer getImage() {
        return image;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getSeconds() {
        return (int) mp3.getLengthInSeconds();
    }

    public int loadBuffer() {
        Bitstream bitstream;

        try {
            bitstream = new Bitstream(new FileInputStream(path.toFile()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        MP3Decoder decoder = new MP3Decoder();

        ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
        OutputBuffer outputBuffer = new OutputBuffer(2, false);
        decoder.setOutputBuffer(outputBuffer);

        while (true) {
            Header header;
            try {
                header = bitstream.readFrame();
            } catch (BitstreamException e) {
                throw new RuntimeException(e);
            }
            if (header == null) break;

            try {
                decoder.decodeFrame(header, bitstream);
            } catch (DecoderException ignored) {
                // bruh
            }

            bitstream.closeFrame();
            output.write(outputBuffer.getBuffer(), 0, outputBuffer.reset());
        }

        try {
            bitstream.close();
        } catch (BitstreamException e) {
            throw new RuntimeException(e);
        }
        ByteBuffer data = MemoryUtil.memAlloc(output.size()).put(output.toByteArray()).rewind();

        int buffer = alGenBuffers();
        alBufferData(buffer, AL_FORMAT_STEREO16, data, getSampleRate());

        MemoryUtil.memFree(data);
        return buffer;
    }
}
