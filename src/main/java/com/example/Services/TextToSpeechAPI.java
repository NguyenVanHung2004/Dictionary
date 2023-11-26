package com.example.Services;

import javafx.scene.control.Alert;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TextToSpeechAPI extends ApiConnection {
  private static TextToSpeechAPI instance = null;
  protected TextToSpeechAPI() {
  }

  public static synchronized TextToSpeechAPI getInstance() {
    if (instance == null)
      instance = new TextToSpeechAPI();

    return instance;
  }

  @Override
  public void prepareQuery(String query , String language) {
    query = URLEncoder.encode(query , StandardCharsets.UTF_8);
    String webUrl = "https://api.voicerss.org/?key=c841bc4b9efd47f2a46f5b673be3984b";
    finalQuery =
            webUrl +  "&hl="
                    + language
                    + "&c="
                    + "WAV"
                    + "&src="
                    + query;

  }

  public AudioInputStream getAudioInputStream() throws IOException {
    getConnection();
    try {
      InputStream is = connection.getInputStream();
      InputStream bufferedIn = new BufferedInputStream(is);
        return AudioSystem.getAudioInputStream(bufferedIn);
    } catch (Exception e) {
      System.out.println("Text-to-speech conversion and playback failed: " + e.getMessage());

    }
    return null;
  }

  public void Speak(){
    try {
      AudioInputStream ais = this.getAudioInputStream();
      AudioFormat format = ais.getFormat();
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
      SourceDataLine source = (SourceDataLine) AudioSystem.getLine(info);
      source.open(format);
      source.start();
      int read;
      byte[] buffer = new byte[1024];
      while ((read = ais.read(buffer, 0, buffer.length)) != -1) {
        source.write(buffer, 0, read);
      }
      source.close();
      ais.close();
      System.out.println("Text-to-speech conversion and playback completed.");
    } catch (Exception e) {
      System.out.println("Text-to-speech conversion and playback failed: " + e.getMessage());
    }
  }
}
