package com.example.Controllers;

import com.example.Services.AudioPlayer;
import com.example.Services.DatabaseConnection;
import com.example.Services.TextToSpeechAPI;
import com.example.Models.VocabModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import com.jfoenix.controls.JFXDialog;

import javax.sound.sampled.*;
import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.sql.Connection;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyDictionaryController implements Initializable {
  @FXML TextField keyWordField;
  @FXML TableView<VocabModel> myDicTableView;
  @FXML TableColumn<VocabModel, String> wordColumn;
  @FXML TableColumn<VocabModel, String> definitionColumn;
  @FXML Button updateButton;
  @FXML Button deleteButton;
  @FXML Button addButton;
  @FXML ImageView ttsImageView;
  @FXML StackPane root;

  private Pane dialog;
  static ObservableList<VocabModel> myVocabObservableList = FXCollections.observableArrayList();
  public static String selectedWord;
  public static String selectedDefinition;
  StringProperty stringProperty = new SimpleStringProperty(selectedWord);
  DatabaseConnection databaseConnection = null;
  Connection connection = null;

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    databaseConnection = DatabaseConnection.getInstance();
    connection = databaseConnection.getDatabaseConnection();
    deleteButton.disableProperty().bind(Bindings.isNull(stringProperty));
    updateButton.disableProperty().bind(Bindings.isNull(stringProperty));
    ttsImageView.visibleProperty().bind(Bindings.isEmpty(stringProperty).not());
    String query = "SELECT * FROM mydictionary;";
    try {
      Statement statement = connection.createStatement();
      ResultSet queryOutput = statement.executeQuery(query);
      while (queryOutput.next()) {
        String myWord = queryOutput.getString("word");
        String myDefinition = queryOutput.getString("definition");
        myVocabObservableList.add(new VocabModel(myWord, myDefinition));
      }
      wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
      definitionColumn.setCellValueFactory(new PropertyValueFactory<>("definition"));
      myDicTableView.setItems(myVocabObservableList);
    } catch (SQLException e) {
      Logger.getLogger(SideBarController.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  @FXML
  private void addButtonClicked() throws IOException {

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Objects.requireNonNull(getClass().getResource("/com/example/view/dialog.fxml")));
    Pane dialog = loader.load();
    CustomDialog dialogController = loader.getController();
    dialogController.setLabel("ADD");
    JFXDialog jfxDialog = new JFXDialog( root, dialog, JFXDialog.DialogTransition.TOP);
    dialogController.okButton.setOnAction(event -> {
      dialogController.addToDatabase("mydictionary");
      jfxDialog.close();
      refresh();
    });
    jfxDialog.show();
  }

  @FXML
  private  void updateButtonClicked() throws IOException {

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Objects.requireNonNull(getClass().getResource("/com/example/view/dialog.fxml")));
    Pane dialog = loader.load();
    CustomDialog dialogController = loader.getController();
    dialogController.setLabel("UPDATE");
    dialogController.setWordTextField(selectedWord);
    dialogController.setDefinitionTextArea(selectedDefinition);
    JFXDialog jfxDialog = new JFXDialog( root, dialog, JFXDialog.DialogTransition.TOP);
    dialogController.okButton.setOnAction(event -> {
      dialogController.updateToDatabase("mydictionary" , selectedWord);
      jfxDialog.close();
    });
    jfxDialog.show();
  }

  @FXML
  private void deleteButtonClicked() throws IOException, SQLException {

    DatabaseConnection.deleteInDatabase("mydictionary" , selectedWord);
    selectedWord = null;
    stringProperty.set(null);
    refresh();
  }

  @FXML
  public void clickedTableView() {
    selectedWord = wordColumn.getCellData(myDicTableView.getSelectionModel().getSelectedIndex());
    stringProperty.set(selectedWord);
    selectedDefinition =
        definitionColumn.getCellData(myDicTableView.getSelectionModel().getSelectedIndex());
    System.out.println(selectedWord);
  }

  public void speech() {
    Task<Void> task =
            new Task<>() {
              @Override
              protected Void call() throws Exception {
                try {
                  AudioPlayer audioPlayer = AudioPlayer.getInstance();
                  audioPlayer.prepareQuery(selectedWord , "en-us");
                  audioPlayer.speak();
                } catch (Exception e) {

                  // Hiển thị thông báo lỗi
                  System.out.println(
                          "Text-to-speech conversion and playback failed:   " + e.getMessage());

                }
                return null;
              }
            };
    new Thread(task).start();
  }

  public void refresh() {
    databaseConnection = DatabaseConnection.getInstance();
    connection = databaseConnection.getDatabaseConnection();
    myVocabObservableList.clear();
    String query = "SELECT word,definition FROM mydictionary;";
    try {
      Statement statement = connection.createStatement();
      ResultSet queryOutput = statement.executeQuery(query);
      while (queryOutput.next()) {
        String myWord = queryOutput.getString("word");
        String myDefinition = queryOutput.getString("definition");
        myVocabObservableList.add(new VocabModel(myWord, myDefinition));
      }
      wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
      definitionColumn.setCellValueFactory(new PropertyValueFactory<>("definition"));
      myDicTableView.setItems(myVocabObservableList);
    } catch (SQLException e) {
      Logger.getLogger(SideBarController.class.getName()).log(Level.SEVERE, null, e);
    }
  }
}
