package com.example.Controllers;

import com.example.Models.VocabModel;
import com.example.Services.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

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

  static ObservableList<VocabModel> myVocabObservableList = FXCollections.observableArrayList();
  public static String selectedWord;
  public static String selectedDefinition;
  StringProperty stringProperty = new SimpleStringProperty(selectedWord);

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    deleteButton.disableProperty().bind(Bindings.isNull(stringProperty));
    updateButton.disableProperty().bind(Bindings.isNull(stringProperty));
    ttsImageView.visibleProperty().bind(Bindings.isEmpty(stringProperty).not());
    myVocabObservableList = DatabaseConnection.getAllVocabModelFromDatabase("mydictionary");
    wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
    definitionColumn.setCellValueFactory(new PropertyValueFactory<>("definition"));
    FilteredList<VocabModel> filteredList = new FilteredList<>(myVocabObservableList, b -> true);
    keyWordField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              filteredList.setPredicate(
                  vocabModel -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    return vocabModel.getWord().startsWith(newValue);
                  });
            });

    SortedList<VocabModel> sortedList = new SortedList<>(filteredList);

    myDicTableView.setItems(sortedList);

    definitionColumn.setCellFactory(
        tc -> {
          TableCell<VocabModel, String> cell = new TableCell<>();
          Text text = new Text();
          cell.setGraphic(text);
          cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
          text.wrappingWidthProperty().bind(cell.widthProperty());
          text.textProperty().bind(cell.itemProperty());
          return cell;
        });
  }

  @FXML
  private void addButtonClicked() throws IOException {
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
            dialogController.addToDatabase("mydictionary");
          } catch (WordAlreadyExistsException | EmptyInPutException e) {
            openErrorDialog(e.getMessage());
          }
          jfxDialog.close();
          refresh();
        });
    jfxDialog.show();
  }

  @FXML
  private void updateButtonClicked() throws IOException {

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(
        Objects.requireNonNull(getClass().getResource("/com/example/view/dialog.fxml")));
    Pane dialog = loader.load();
    CustomDialog dialogController = loader.getController();
    dialogController.setLabel("UPDATE");
    dialogController.setWordTextField(selectedWord);
    dialogController.setDefinitionTextArea(selectedDefinition);
    JFXDialog jfxDialog = new JFXDialog(root, dialog, JFXDialog.DialogTransition.TOP);
    dialogController.okButton.setOnAction(
        event -> {
          try {
            dialogController.updateToDatabase("mydictionary", selectedWord);
          } catch (EmptyInPutException e) {
            openErrorDialog(e.getMessage());
          }
          jfxDialog.close();
          refresh();
        });
    jfxDialog.show();
  }

  @FXML
  private void deleteButtonClicked() throws SQLException {
    DatabaseConnection.deleteInDatabase("mydictionary", selectedWord);
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
          protected Void call() {
            try {
              AudioPlayer audioPlayer = AudioPlayer.getInstance();
              audioPlayer.prepareQuery(selectedWord, "en-us");
              audioPlayer.speak();
            } catch (NoInternetException e) {
              Platform.runLater(
                  () -> {
                    openErrorDialog(e.getMessage());
                  });
            } catch (Exception e) {
              throw new RuntimeException();
            }

            return null;
          }
        };
    new Thread(task).start();
  }

  public void refresh() {
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            myVocabObservableList = DatabaseConnection.getAllVocabModelFromDatabase("mydictionary");

            Platform.runLater(() -> myDicTableView.setItems(myVocabObservableList));

            return null;
          }
        };

    new Thread(task).start();
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
