package com.example.Controllers;

import com.example.Services.AudioPlayer;
import com.example.Services.DatabaseConnection;

import com.example.Services.TextToSpeechAPI;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class EnViDicController implements Initializable {
@FXML
  public TextArea definitonTextArea;
@FXML
  public TextField wordTextField;
  @FXML
  public JFXButton acceptButton;
  @FXML
  public JFXButton declineButton;
  Pane customDialog;
  @FXML private TextField keyWordField;
  @FXML ListView<String> searchList;
  @FXML WebView webView;
  @FXML Button addButton;
  @FXML Button updateButton;
  @FXML Button deleteButton;
  @FXML StackPane root;

  public static ObservableList<String> vocabModelObservableList =
      FXCollections.observableArrayList();
  private  String selectedWord;
  private String selectedDefinition;



  @Override
  public void initialize(URL url, ResourceBundle resource) {
    searchList.setItems(vocabModelObservableList);
    updateButton.setDisable(true);
    deleteButton.setDisable(true);
    FilteredList<String> filteredData = new FilteredList<>(vocabModelObservableList, b -> true);
    keyWordField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              filteredData.setPredicate(
                  vocabModel -> {
                    if (newValue.isEmpty() || newValue.isBlank()) return true;
                    String searchKeyWord = newValue.toLowerCase();
                      return vocabModel.startsWith(searchKeyWord);
                  });
            });

    SortedList<String> sortedData = new SortedList<>(filteredData);
    searchList.setItems(sortedData);
    searchList.getSelectionModel().selectedItemProperty().addListener(this::clickedColumn);


  }

  public void clickedColumn(
      ObservableValue<? extends String> observableValue, String old, String newValue) {
    updateButton.setDisable(false);
    deleteButton.setDisable(false);
    selectedWord = searchList.getSelectionModel().getSelectedItems().toString();
    selectedWord = selectedWord.substring(1, selectedWord.length() - 1);

    DatabaseConnection connectNow = DatabaseConnection.getInstance();
    Connection connectDB = connectNow.getDatabaseConnection();

    String query = "SELECT definition FROM dictionary WHERE word = ?;";
    try {
      PreparedStatement preparedStatement = connectDB.prepareStatement(query);
      preparedStatement.setString(1 , selectedWord);
      System.out.println(query);
      ResultSet queryOutput = preparedStatement.executeQuery();
      while (queryOutput.next()) {
        selectedDefinition = queryOutput.getString("definition");
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(selectedDefinition.replace("\n", "<br>"));
      }

    } catch (SQLException e) {
      Logger.getLogger(SideBarController.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  public void addButtonClicked() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Objects.requireNonNull(getClass().getResource("/com/example/view/dialog.fxml")));
    Pane dialog = loader.load();
    CustomDialog dialogController = loader.getController();
    dialogController.setLabel("ADD");
    JFXDialog jfxDialog = new JFXDialog( root, dialog, JFXDialog.DialogTransition.TOP);
    dialogController.okButton.setOnAction(event -> {
      dialogController.addToDatabase("dictionary");
      jfxDialog.close();
    });
    jfxDialog.show();
  }
  public void updateButtonClicked() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Objects.requireNonNull(getClass().getResource("/com/example/view/dialog.fxml")));
    Pane dialog = loader.load();
    CustomDialog dialogController = loader.getController();
    dialogController.setLabel("UPDATE");
    dialogController.setWordTextField(selectedWord);
    dialogController.setDefinitionTextArea(selectedDefinition);
    JFXDialog jfxDialog = new JFXDialog( root, dialog, JFXDialog.DialogTransition.TOP);
    dialogController.okButton.setOnAction(event -> {
      dialogController.updateToDatabase("dictionary" , selectedWord);
      jfxDialog.close();
    });
    jfxDialog.show();
  }

  public void deleteButtonClicked() throws SQLException {
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Connection connection = databaseConnection.getDatabaseConnection();
    String query = "DELETE FROM dictionary WHERE word = ? ; " ;
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setString(1, selectedWord);
    preparedStatement.execute();
    preparedStatement.close();
  }

  public void speech(){
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            try {
              AudioPlayer audioPlayer = AudioPlayer.getInstance();
              audioPlayer.prepareQuery(selectedWord);
              audioPlayer.speak();
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
