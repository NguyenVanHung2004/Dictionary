package com.example.englishapp;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.sql.*;

import java.util.ResourceBundle;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchScreenController implements Initializable {

  @FXML ListView<String> searchList;
  @FXML WebView webView;
  @FXML private TextField keyWordField;

  ObservableList<String> vocabModelObservableList = FXCollections.observableArrayList();

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    DatabaseConnection connectNow = new DatabaseConnection();
    Connection connectDB = connectNow.getDatabaseConnection();
    String query = "SELECT target,definition FROM dictionary;";
    try {
      Statement statement = connectDB.createStatement();
      ResultSet queryOutput = statement.executeQuery(query);
      while (queryOutput.next()) {
        String myWord = queryOutput.getString("target");
        String myDefinition = queryOutput.getString("definition");
        vocabModelObservableList.add(myWord);
      }
      searchList.setItems(vocabModelObservableList);

      FilteredList<String> filteredData = new FilteredList<>(vocabModelObservableList, b -> true);
      keyWordField
          .textProperty()
          .addListener(
              (observable, oldValue, newValue) -> {
                filteredData.setPredicate(
                    vocabModel -> {
                      if (newValue.isEmpty() || newValue.isBlank()) return true;
                      String searchKeyWord = newValue.toLowerCase();
                      if (vocabModel.contains(searchKeyWord)) return true;
                      return false;
                    });
              });
      SortedList<String> sortedData = new SortedList<>(filteredData);
      searchList.setItems(sortedData);
      searchList.getSelectionModel().selectedItemProperty().addListener(this::clickedColumn);
    } catch (SQLException e) {
      Logger.getLogger(SearchScreenController.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  public void clickedColumn(
      ObservableValue<? extends String> observableValue, String old, String newValue) {

    String selectedWord = searchList.getSelectionModel().getSelectedItems().toString();
    String searchWord = selectedWord.substring(1, selectedWord.length() - 1);
    DatabaseConnection connectNow = new DatabaseConnection();
    Connection connectDB = connectNow.getDatabaseConnection();
    String query = "SELECT definition FROM dictionary " + "WHERE target = '" + searchWord + "';";
    System.out.println(query);
    try {
      Statement statement = connectDB.createStatement();
      ResultSet queryOutput = statement.executeQuery(query);
      while (queryOutput.next()) {
        String myDefinition = queryOutput.getString("definition");

        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(myDefinition);
      }

    } catch (SQLException e) {
      Logger.getLogger(SearchScreenController.class.getName()).log(Level.SEVERE, null, e);
    }
  }
}
