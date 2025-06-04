package com.minesweeper;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    private static final int NUM_SOUNDS = 5;
    private static Clip[] clips;
    private static final String FILE_PATH_PREFIX = "";
    private static boolean soundEnabled = true; 

    static {
        clips = new Clip[NUM_SOUNDS];
        for (int i = 0; i < NUM_SOUNDS; i++) {
            loadSound(i + 1, "/resources/sound/sound" + (i + 1) + ".wav");
        }
    }

    private static void loadSound(int index, String resourcePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                Sound.class.getResourceAsStream(resourcePath)
            );
            clips[index - 1] = AudioSystem.getClip();
            clips[index - 1].open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
            System.err.println("Failed to load sound: " + resourcePath);
            e.printStackTrace();
        }
    }


    public static void playSound(int index) {
        if (soundEnabled && index >= 1 && index <= NUM_SOUNDS && clips[index - 1] != null) {
            if (clips[index - 1].isRunning()) {
                clips[index - 1].stop();
            }
            clips[index - 1].setFramePosition(0);
            clips[index - 1].start();
        }
    }

    public static void toggleSound() {
        setSoundEnabled(!getSoundEnabled());  
        System.out.println("Mute/Unmute");
    }

    public static void stopSound(int index) {
        if (index >= 1 && index <= NUM_SOUNDS && clips[index - 1] != null) {
            if (clips[index - 1].isRunning()) {
                clips[index - 1].stop();  
            }
        }
    }
    public static void startSound(int index) {
        if (index >= 1 && index <= NUM_SOUNDS && clips[index - 1] != null) {
            clips[index - 1].setFramePosition(0); 
            clips[index - 1].start(); 
        }
    }
    public static boolean getSoundEnabled(){
        return soundEnabled;
    }
    public static void setSoundEnabled(boolean n){
        soundEnabled = n;
    }
}