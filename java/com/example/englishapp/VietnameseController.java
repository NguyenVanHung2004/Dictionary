package com.example.englishapp;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VietnameseController implements Initializable {

  @FXML private TextField keyWordField;
  @FXML ListView<String> searchList;
  @FXML WebView webView;
  @FXML Button addButton;

  public static ObservableList<String> vocabModelObservableList =
      FXCollections.observableArrayList();
  public static String selectedWord;

  @Override
  public void initialize(URL url, ResourceBundle resource) {
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
  }

  public void clickedColumn(
      ObservableValue<? extends String> observableValue, String old, String newValue) {

    selectedWord = searchList.getSelectionModel().getSelectedItems().toString();
    selectedWord = selectedWord.substring(1, selectedWord.length() - 1);
    DatabaseConnection connectNow = new DatabaseConnection();
    Connection connectDB = connectNow.getDatabaseConnection();
    String query = "SELECT definition FROM dictionary " + "WHERE target = '" + selectedWord + "';";
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

  public void addButtonClicked() throws IOException {
    addVocabDialogController.type = "Add";
    Parent parent =
        FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addVocabDialog.fxml")));
    Scene scene = new Scene(parent);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();
  }

}
