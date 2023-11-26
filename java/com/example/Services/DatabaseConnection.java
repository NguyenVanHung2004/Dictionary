package com.example.Services;

import com.example.Models.VocabModel;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {

  public Connection databaseLink;



  private static DatabaseConnection databaseConnection_instance = null ;

  private DatabaseConnection(){
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
  }

  public static synchronized DatabaseConnection getInstance(){
    if( databaseConnection_instance == null)
        databaseConnection_instance = new DatabaseConnection();
    return databaseConnection_instance;
  }

  public Connection getDatabaseConnection() {
    return databaseLink;
  }

  public static void insertToDatabase(String databaseName , VocabModel newVocabModel) {
    try{
      DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
      Connection connection = databaseConnection.getDatabaseConnection();
      String sqlQuery = "INSERT INTO " + databaseName  + "(word,definition) VALUES (? ,? ) ";
      PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
      preparedStatement.setString(1, newVocabModel.getWord());
      preparedStatement.setString(2, newVocabModel.getDefinition());
      System.out.println(sqlQuery);
      preparedStatement.execute();
      preparedStatement.close();
    }catch (SQLException ignored){
      System.out.println("Cannot insert new word to database");
    }
  }
  public static void updateToDatabase(String databaseName , VocabModel newVocabModel , String oldWord)   {
    try{
      DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
      Connection connection = databaseConnection.getDatabaseConnection();
      String sqlQuery = "UPDATE " +  databaseName + " SET word = ? , definition=?  WHERE word = ? ";
      PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
      preparedStatement.setString(1, newVocabModel.getWord());
      preparedStatement.setString(2, newVocabModel.getDefinition());
      preparedStatement.setString(3, oldWord);
      preparedStatement.execute();
      preparedStatement.close();
    }catch (SQLException ignored){
      System.out.println("Cannot update new definition to database");
    }
  }

  public static void deleteInDatabase(String databaseName , String wordToDelete) throws SQLException {
    try{
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Connection connection = databaseConnection.getDatabaseConnection();
    String query = "DELETE FROM "  + databaseName +  " WHERE word = ? ; ";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
      System.out.println(query);
    preparedStatement.setString(1, wordToDelete);
    preparedStatement.execute();
    preparedStatement.close();
    }catch (SQLException sqlException){
      System.out.println("Cannot delete word in database" + databaseName);

    }
  }



}
