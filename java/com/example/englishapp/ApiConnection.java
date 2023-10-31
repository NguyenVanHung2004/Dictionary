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

public abstract class ApiConnection {
  protected  String urlString;
  protected HttpURLConnection connection;
  protected URL url;
  protected String webUrl;
  protected String query;
  protected String finalQuery;
  public ApiConnection() {

  }

  protected abstract void prepareQuery(String query);

  public void getConnection(){
    try {

      System.out.println(finalQuery);
      url = new URL(finalQuery);

      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}
