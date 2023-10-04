package com.example.englishapp;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class DatabaseConnection {

  public Connection databaseLink;

  public Connection getDatabaseConnection( ) {
    String databaseName = "dictonary";
    String databaseUser = "root";
    String databasePassWord = "Nguyenvanhung2004yb!";
    String url = "jdbc:mysql://localhost/" + databaseName;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      databaseLink = DriverManager.getConnection(url, databaseUser, databasePassWord);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return databaseLink;
  }
}
