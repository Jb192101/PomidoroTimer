package org.jedi_bachelor.model;

import javax.sound.sampled.*;
import java.util.Objects;

public class SoundPlayer {
    private static Clip currentClip;

    public static void stopSound() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }

    public static void playSound(String filePath) {
        try {
            stopSound();

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    Objects.requireNonNull(SoundPlayer.class.getResourceAsStream(filePath)));

            currentClip = AudioSystem.getClip();
            currentClip.open(audioStream);
            currentClip.start();

        } catch (Exception e) {
            System.err.println("Ошибка mp3: " + e.getMessage());
        }
    }
}