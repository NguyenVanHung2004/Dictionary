package com.example.Services;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TextToSpeechAPI extends ApiConnection {

  protected TextToSpeechAPI() {}

  @Override
  public void prepareQuery(String query, String language) {
    query = URLEncoder.encode(query, StandardCharsets.UTF_8);
    String webUrl = "https://api.voicerss.org/?key=c841bc4b9efd47f2a46f5b673be3984b";
    finalQuery = webUrl + "&hl=" + language + "&c=" + "WAV" + "&src=" + query;
  }

  public AudioInputStream getAudioInputStream() throws IOException, NoInternetException {
    getConnection();
    InputStream is = connection.getInputStream();
    InputStream bufferedIn = new BufferedInputStream(is);
    try {
      return AudioSystem.getAudioInputStream(bufferedIn);
    } catch (UnsupportedAudioFileException e) {
      throw new RuntimeException(e);
    }
  }
}
