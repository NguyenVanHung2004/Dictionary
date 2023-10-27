package com.example.englishapp;

import com.jfoenix.controls.JFXDialog;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.sound.sampled.*;
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

public class EnViDicController implements Initializable {

  @FXML private TextField keyWordField;
  @FXML ListView<String> searchList;
  @FXML WebView webView;
  @FXML Button addButton;
  @FXML
  StackPane root;

  public static ObservableList<String> vocabModelObservableList =
      FXCollections.observableArrayList();
  public static String selectedWord;
  private Pane dialog;

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
      Logger.getLogger(SideBarController.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  public void addButtonClicked() throws IOException {
    DialogController.type = "Add";
    DialogController.databaseName = "dictionary";
    dialog = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dialog.fxml")));
    new JFXDialog( root, dialog, JFXDialog.DialogTransition.TOP).show();

  }
  public void updateButtonClicked() throws IOException {
    DialogController.type = "Update";
    DialogController.databaseName = "dictionary";
    dialog = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dialog.fxml")));
    new JFXDialog( root, dialog, JFXDialog.DialogTransition.TOP).show();
  }
  public void deleteButtonClicked(){

  }
  public void speech(){
      Task<Void> task =
              new Task<>() {
                @Override
                protected Void call() throws Exception {
                  try {
                    String key = "c841bc4b9efd47f2a46f5b673be3984b";
                    String outputformat = "WAV";
                    String query = selectedWord.replace(" ", "%20");
                    String textToSpeechUrl =
                            "https://api.voicerss.org/?key="
                                    + key
                                    + "&hl="
                                    + "en-gb"
                                    + "&c="
                                    + outputformat
                                    + "&src="
                                    + query;
                    ApiConnection apiConnection = new ApiConnection(textToSpeechUrl);
                    AudioInputStream ais = apiConnection.getAudioInputStream();
                    AudioFormat format = ais.getFormat();
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                    SourceDataLine source = (SourceDataLine) AudioSystem.getLine(info);
                    source.open(format);
                    source.start();
                    int read;
                    byte[] buffer = new byte[1024];
                    while ((read = ais.read(buffer, 0, buffer.length)) != -1) {
                      source.write(buffer, 0, read);
                    }
                    source.close();
                    ais.close();
                    System.out.println("Text-to-speech conversion and playback completed.");
                  } catch (Exception e) {
                    // Hiển thị thông báo lỗi
                    System.out.println(
                            "Text-to-speech conversion and playback failed: " + e.getMessage());
                  }
                  return null;
                }
              };
      new Thread(task).start();
  }

}
