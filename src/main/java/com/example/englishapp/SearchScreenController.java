package com.example.englishapp;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

import java.util.Objects;
import java.util.ResourceBundle;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchScreenController implements Initializable {

  @FXML StackPane contentArea;

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
        vocabModelObservableList.add(myWord);
      }
    } catch (SQLException e) {
      Logger.getLogger(SearchScreenController.class.getName()).log(Level.SEVERE, null, e);
    }
    VietnameseController.vocabModelObservableList = vocabModelObservableList;
  }

  @FXML
  public void clickedSideBarMyWord(MouseEvent event) throws IOException {
    Parent newPane =
        FXMLLoader.load(Objects.requireNonNull(getClass().getResource("myVocabulary.fxml")));
    contentArea.getChildren().removeAll();
    contentArea.getChildren().setAll(newPane);
  }

  public void clickedSideBarVietnamese(MouseEvent event) throws IOException {
    Parent newPane =
        FXMLLoader.load(Objects.requireNonNull(getClass().getResource("VietnamesePanel.fxml")));
    contentArea.getChildren().removeAll();
    contentArea.getChildren().setAll(newPane);
  }
}
