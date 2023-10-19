package com.example.englishapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.sql.Connection;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyVocabularyController implements Initializable {
  @FXML TextField keyWordField;
  @FXML TableView<VocabModel> myDicTableView;
  @FXML TableColumn<VocabModel, String> wordColumn;
  @FXML TableColumn<VocabModel, String> definitionColumn;
  @FXML Button updateButton;
  @FXML Button deleteButton;
  @FXML Button addButton;
  @FXML StackPane root;

  private Pane dialog;
  ObservableList<VocabModel> myVocabObservableList = FXCollections.observableArrayList();
  public static String selectedWord;
  public static String selectedDefinition;
  DatabaseConnection databaseConnection = null;
  Connection connection = null;

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    databaseConnection = new DatabaseConnection();
    connection = databaseConnection.getDatabaseConnection();

    String query = "SELECT english,vietnamese FROM mydictionary;";
    try {
      Statement statement = connection.createStatement();
      ResultSet queryOutput = statement.executeQuery(query);
      while (queryOutput.next()) {
        String myWord = queryOutput.getString("english");
        String myDefinition = queryOutput.getString("vietnamese");
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

    addVocabDialogController.type = "Add";
    dialog = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("add_vocab_dialog.fxml")));
    new JFXDialog( root, dialog, JFXDialog.DialogTransition.TOP).show();

  }

  @FXML
  private void updateButtonClicked() throws IOException {
    addVocabDialogController.type = "Update";
    dialog = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("add_vocab_dialog.fxml")));
    new JFXDialog( root, dialog, JFXDialog.DialogTransition.TOP).show();
    refresh();
  }

  @FXML
  private void deleteButtonClicked() throws IOException, SQLException {
    String query = "DELETE FROM mydictionary WHERE english = ? AND vietnamese = ? " ;
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setString(1, selectedWord);
    preparedStatement.setString(2, selectedDefinition);
    preparedStatement.execute();
    preparedStatement.close();
    refresh();
  }

  @FXML
  public void clickedTableView() {
    selectedWord = wordColumn.getCellData(myDicTableView.getSelectionModel().getSelectedIndex());
    selectedDefinition =
        definitionColumn.getCellData(myDicTableView.getSelectionModel().getSelectedIndex());
    System.out.println(selectedWord);
  }

   public void refresh() {
    databaseConnection = new DatabaseConnection();
    connection = databaseConnection.getDatabaseConnection();
     myVocabObservableList.clear();
    String query = "SELECT english,vietnamese FROM mydictionary;";
    try {
      Statement statement = connection.createStatement();
      ResultSet queryOutput = statement.executeQuery(query);
      while (queryOutput.next()) {
        String myWord = queryOutput.getString("english");
        String myDefinition = queryOutput.getString("vietnamese");
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
