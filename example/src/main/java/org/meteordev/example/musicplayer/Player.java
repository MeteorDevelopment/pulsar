package org.meteordev.example.musicplayer;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.AL_SEC_OFFSET;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;

public class Player {
    public static final List<Song> SONGS = new ArrayList<>();

    private static Song SONG;
    private static int SOURCE;
    private static int BUFFER;

    public static void load() {
        long device = alcOpenDevice((ByteBuffer) null);
        ALCCapabilities alcCaps = ALC.createCapabilities(device);

        long context = ALC10.alcCreateContext(device, (IntBuffer) null);
        alcMakeContextCurrent(context);
        AL.createCapabilities(alcCaps);

        SOURCE = alGenSources();
        alSourcef(SOURCE, AL_GAIN, 1);

        try {
            Files.find(Path.of(System.getProperty("user.home"), "Music"), 10, (path, basicFileAttributes) -> basicFileAttributes.isRegularFile())
                    .filter(path -> path.getFileName().toString().endsWith(".mp3"))
                    .map(Song::new)
                    .sorted(Comparator.comparing(Song::getSearchString))
                    .limit(5)
                    .forEach(SONGS::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Loaded " + SONGS.size() + " songs");
    }

    public static void play(Song song) {
        if (alGetSourcei(SOURCE, AL_SOURCE_STATE) == AL_PLAYING) {
            alSourceStop(SOURCE);
            alDeleteBuffers(BUFFER);
        }

        SONG = song;
        System.out.format("Playing %s by %s%n", song.getTitle(), song.getArtist());

        BUFFER = song.loadBuffer();
        alSourcei(SOURCE, AL_BUFFER, BUFFER);
        alSourcePlay(SOURCE);
    }

    public static double getProgress() {
        if (SONG == null) return 0;

        int position = alGetSourcei(SOURCE, AL_SEC_OFFSET);
        return (double) position / SONG.getSeconds();
    }
}
