package com.example.englishapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

import java.util.Objects;
import java.util.ResourceBundle;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SideBarController implements Initializable {

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
      Logger.getLogger(SideBarController.class.getName()).log(Level.SEVERE, null, e);
    }
    EnViDicController.vocabModelObservableList = vocabModelObservableList;
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
        FXMLLoader.load(Objects.requireNonNull(getClass().getResource("EnViDicPane.fxml")));
    contentArea.getChildren().removeAll();
    contentArea.getChildren().setAll(newPane);
  }

  public void clickedTranslateAPI(MouseEvent event) throws IOException {
    Parent newPane =
        FXMLLoader.load(Objects.requireNonNull(getClass().getResource("TranslateAPI.fxml")));
    contentArea.getChildren().removeAll();
    contentArea.getChildren().setAll(newPane);
  }
}
