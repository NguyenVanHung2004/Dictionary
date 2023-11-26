package com.example.Services;

import javafx.scene.control.Alert;

import javax.sound.sampled.*;
import java.io.IOException;

public class AudioPlayer extends TextToSpeechAPI {

    private static AudioPlayer audioPlayerInstance = null;
    private SourceDataLine currentSource;

    public static synchronized AudioPlayer getInstance() {
        if (audioPlayerInstance == null)
            audioPlayerInstance = new AudioPlayer();

        return audioPlayerInstance;
    }
    private AudioPlayer(){

    }

    public void stop() {
        if (currentSource != null && currentSource.isRunning()) {
            currentSource.stop();
            currentSource.close();
            currentSource = null;
        }
    }

    public void speak() {
        try {
            stop(); // Dừng âm thanh hiện tại (nếu có)

            AudioInputStream ais = this.getAudioInputStream();
            AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            currentSource = (SourceDataLine) AudioSystem.getLine(info);
            currentSource.open(format);
            currentSource.start();

            int read;
            byte[] buffer = new byte[1024];
            while ((read = ais.read(buffer, 0, buffer.length)) != -1) {
                currentSource.write(buffer, 0, read);
            }

            currentSource.close();
            ais.close();
            System.out.println("Text-to-speech conversion and playback completed.");
        } catch (Exception e) {

            System.out.println("Text-to-speech conversion and playback failed : " + e.getMessage());



        }
    }

}
