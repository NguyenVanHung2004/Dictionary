package com.example.Services;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.UnknownHostException;

public abstract class ApiConnection {
  protected HttpURLConnection connection;
  protected URL url;
  protected String finalQuery;
  public ApiConnection() {

  }

  protected abstract void prepareQuery(String query);

  public void getConnection() throws IOException {
      System.out.println(finalQuery);
      url = new URL(finalQuery);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
    }
  }

