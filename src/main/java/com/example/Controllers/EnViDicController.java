package com.example.Controllers;

import com.example.Models.Trie;
import com.example.Services.*;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnViDicController implements Initializable {
  @FXML ImageView ttsImageView;
  @FXML private TextField keyWordField;
  @FXML ListView<String> searchList;
  @FXML WebView webView;
  @FXML Button addButton;
  @FXML Button updateButton;
  @FXML Button deleteButton;
  @FXML StackPane root;

  private String selectedWord = null;
  StringProperty stringProperty = new SimpleStringProperty(null);
  private String selectedDefinition;
  WebEngine webEngine;
  ObservableList<String> itemsOfListView = FXCollections.observableArrayList();

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    deleteButton.disableProperty().bind(Bindings.isNull(stringProperty));
    updateButton.disableProperty().bind(Bindings.isNull(stringProperty));
    ttsImageView.visibleProperty().bind(Bindings.isEmpty(stringProperty).not());
    searchList.setItems(itemsOfListView);
    keyWordField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              ArrayList<String> words = Trie.getWordsWithPrefix(newValue);
              itemsOfListView.clear();
              itemsOfListView.addAll(words);
            });

    searchList.getSelectionModel().selectedItemProperty().addListener(this::clickedColumn);
  }

  public void clickedColumn(
      ObservableValue<? extends String> observableValue, String old, String newValue) {

    selectedWord = searchList.getSelectionModel().getSelectedItems().toString();
    selectedWord = selectedWord.substring(1, selectedWord.length() - 1);
    stringProperty.set(selectedWord);

    DatabaseConnection connectNow = DatabaseConnection.getInstance();
    Connection connectDB = connectNow.getDatabaseConnection();

    String query = "SELECT definition FROM dictionary WHERE word = ?;";
    try {
      PreparedStatement preparedStatement = connectDB.prepareStatement(query);
      preparedStatement.setString(1, selectedWord);
      System.out.println(query);
      ResultSet queryOutput = preparedStatement.executeQuery();
      while (queryOutput.next()) {
        selectedDefinition = queryOutput.getString("definition");
        webEngine = webView.getEngine();
        webEngine.loadContent(selectedDefinition.replace("\n", "<br>"));
      }

    } catch (SQLException e) {
      Logger.getLogger(SideBarController.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  public void addButtonClicked() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(
        Objects.requireNonNull(getClass().getResource("/com/example/view/dialog.fxml")));
    Pane dialog = loader.load();
    CustomDialog dialogController = loader.getController();
    dialogController.setLabel("ADD");
    JFXDialog jfxDialog = new JFXDialog(root, dialog, JFXDialog.DialogTransition.TOP);
    dialogController.okButton.setOnAction(
        event -> {
          try {
            dialogController.addToDatabase("dictionary");
          } catch (WordAlreadyExistsException | EmptyInPutException e) {
            openErrorDialog(e.getMessage());
          }
          jfxDialog.close();
          refresh();
        });

    jfxDialog.show();
  }

  public void updateButtonClicked() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(
        Objects.requireNonNull(getClass().getResource("/com/example/view/dialog.fxml")));
    Pane dialog = loader.load();
    CustomDialog dialogController = loader.getController();
    dialogController.setLabel("UPDATE");
    dialogController.setWordTextField(selectedWord);
    dialogController.setDefinitionTextArea(
        selectedDefinition.replace("<br />", "\n").replace("<I><Q>", "").replace("</Q></I>", ""));
    JFXDialog jfxDialog = new JFXDialog(root, dialog, JFXDialog.DialogTransition.TOP);
    dialogController.okButton.setOnAction(
        event -> {
          try {
            dialogController.updateToDatabase("dictionary", selectedWord);
          } catch (EmptyInPutException e) {
            openErrorDialog(e.getMessage());
          }
          jfxDialog.close();
          refresh();
        });
    jfxDialog.show();
  }

  public void deleteButtonClicked() {
    DatabaseConnection.deleteInDatabase("dictionary", selectedWord);
    Trie.delete(selectedWord);
    refresh();
    selectedWord = null;
    stringProperty.set(null);
  }

  public void speech() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            try {
              AudioPlayer audioPlayer = AudioPlayer.getInstance();
              audioPlayer.prepareQuery(selectedWord, "en-us");
              audioPlayer.speak();
            } catch (NoInternetException e) {
              Platform.runLater(() -> openErrorDialog(e.getMessage()));
            } catch (Exception e) {
              throw new RuntimeException();
            }
            return null;
          }
        };
    new Thread(task).start();
  }

  public void refresh() {
    Pane animationPane;
    try {
      animationPane =
          FXMLLoader.load(
              Objects.requireNonNull(
                  getClass().getResource("/com/example/view/loading_animation.fxml")));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    JFXDialog jfxDialogAnimation =
        new JFXDialog(root, animationPane, JFXDialog.DialogTransition.LEFT);
    jfxDialogAnimation.show();

    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            DatabaseConnection.getAllWordFromDatabaseToTrie("dictionary");
            return null;
          }

          @Override
          protected void succeeded() {
            super.succeeded();
            jfxDialogAnimation.close();
          }
        };
    new Thread(task).start();
    selectedWord = null;
    keyWordField.clear();
  }

  void openErrorDialog(String text) {
    JFXDialogLayout content = new JFXDialogLayout();
    content.setHeading(new Text("Error"));
    content.setBody(new Text(text));
    JFXButton okButton = new JFXButton("Okay");
    JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
    okButton.setOnAction(actionEvent -> dialog.close());
    content.setActions(okButton);
    dialog.show();
  }
}
