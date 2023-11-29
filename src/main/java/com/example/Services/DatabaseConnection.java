package com.example.Services;

import com.example.Models.Trie;
import com.example.Models.VocabModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DatabaseConnection {

  public Connection databaseLink;

  private static DatabaseConnection databaseConnection_instance = null;

  private DatabaseConnection() {
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

  public static synchronized DatabaseConnection getInstance() {
    if (databaseConnection_instance == null) databaseConnection_instance = new DatabaseConnection();
    return databaseConnection_instance;
  }

  public Connection getDatabaseConnection() {
    return databaseLink;
  }

  public static void insertToDatabase(String databaseName, VocabModel newVocabModel) throws WordAlreadyExistsException {
    try {
      DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
      Connection connection = databaseConnection.getDatabaseConnection();

      String checkQuery = "SELECT * FROM " + databaseName + " WHERE word = ?";
      PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
      checkStatement.setString(1, newVocabModel.getWord());
      ResultSet resultSet = checkStatement.executeQuery();

      if (resultSet.next()) {
        // Nếu từ đã tồn tại, ném ra ngoại lệ
        throw new WordAlreadyExistsException(resultSet.getString("word"));
      }


      String sqlQuery = "INSERT INTO " + databaseName + "(word,definition) VALUES (? ,? ) ";
      PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
      preparedStatement.setString(1, newVocabModel.getWord());
      preparedStatement.setString(2, newVocabModel.getDefinition());
      System.out.println(sqlQuery);
      preparedStatement.execute();
      preparedStatement.close();
    } catch (SQLException ignored) {
      System.out.println("Cannot insert new word to database");
    }
  }

  public static void updateToDatabase(
      String databaseName, VocabModel newVocabModel, String oldWord) {
    try {
      DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
      Connection connection = databaseConnection.getDatabaseConnection();
      String sqlQuery = "UPDATE " + databaseName + " SET word = ? , definition=?  WHERE word = ? ";
      System.out.println(sqlQuery);
      PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
      preparedStatement.setString(1, newVocabModel.getWord());
      preparedStatement.setString(2, newVocabModel.getDefinition());
      preparedStatement.setString(3, oldWord);
      preparedStatement.execute();
      preparedStatement.close();
    } catch (SQLException ignored) {
      System.out.println("Cannot update new definition to database");
    }
  }

  public static void deleteInDatabase(String databaseName, String wordToDelete) {
    try {
      DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
      Connection connection = databaseConnection.getDatabaseConnection();
      String query = "DELETE FROM " + databaseName + " WHERE word = ? ; ";
      System.out.println(query);
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, wordToDelete);
      preparedStatement.execute();
      preparedStatement.close();
    } catch (SQLException sqlException) {
      System.out.println("Cannot delete word in database" + databaseName);
    }
  }
  public static ObservableList<VocabModel> getAllVocabModelFromDatabase(String databaseName) {
    ObservableList<VocabModel> vocabObservableList = FXCollections.observableArrayList();
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Connection connection = databaseConnection.getDatabaseConnection();
    String query = "SELECT * FROM  " + databaseName;
    try {
      Statement statement = connection.createStatement();
      ResultSet queryOutput = statement.executeQuery(query);
      while (queryOutput.next()) {
        String myWord = queryOutput.getString("word");
        String myDefinition = queryOutput.getString("definition");
        vocabObservableList.add(new VocabModel(myWord, myDefinition));
      }
    }catch(SQLException e){
       System.out.println("Cannot get all vocab from database" + databaseName);
    }
    return vocabObservableList;
  }
  public static void getAllWordFromDatabaseToTrie(String databaseName) {
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Connection connection = databaseConnection.getDatabaseConnection();
    String query = "SELECT * FROM  " + databaseName;
    System.out.println(query);
    try {
      Statement statement = connection.createStatement();
      ResultSet queryOutput = statement.executeQuery(query);
      while (queryOutput.next()) {
        String myWord = queryOutput.getString("word");
        Trie.insert(myWord);
      }
    }catch(SQLException e){
      System.out.println("Cannot get all word from database" + databaseName);
    }
  }
}
