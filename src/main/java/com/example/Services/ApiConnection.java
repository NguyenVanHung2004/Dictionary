package com.example.Services;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public abstract class ApiConnection {
  protected HttpURLConnection connection;
  protected URL url;
  protected String finalQuery;
  protected ApiConnection() {
  }

  protected abstract void prepareQuery(String query, String language);

  public void getConnection() throws NoInternetException, IOException {
      System.out.println(finalQuery);
      url = new URL(finalQuery);
      checkInternetConnection();

      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
    }

  public void checkInternetConnection() throws NoInternetException {
      try {
          InetAddress address = InetAddress.getByName("www.google.com");
          if (!address.isReachable(2000)) {
              throw new NoInternetException("No internet connection");
          }
      } catch (Exception e) {
          throw new NoInternetException("Cannot check Internet connection: " + e.getMessage());
    }
  }
}

