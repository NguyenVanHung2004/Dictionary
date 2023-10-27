package com.example.englishapp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ApiConnection {
  private final String urlString;
  private HttpURLConnection connection;
  private URL url;
  public ApiConnection(String urlString) {
    this.urlString = urlString;
  }

  public void getConnection(){
    try {

      System.out.println(urlString);
      url = new URL(urlString);

      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public JSONObject getJSONObject() {
    getConnection();
    try {
      // Check if connect is made
      int responseCode = connection.getResponseCode();

      if (responseCode != 200) {
        throw new RuntimeException("HttpResponseCode: " + responseCode);
      } else {

        StringBuilder informationString = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
          informationString.append(scanner.nextLine());
        }
        scanner.close();

        JSONParser parse = new JSONParser();

        return (JSONObject) parse.parse(String.valueOf(informationString));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  public AudioInputStream getAudioInputStream() throws IOException {
    getConnection();
    try {
    InputStream is = connection.getInputStream();
    InputStream bufferedIn = new BufferedInputStream(is);
    AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
    return ais;
  } catch (Exception e) {
        System.out.println(
            "Text-to-speech conversion and playback failed: " + e.getMessage());
  }
  return null;
}
}
