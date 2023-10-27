package com.example.englishapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class DialogController implements Initializable {
  @FXML TextField wordTextField;
  @FXML TextArea definitonTextArea;
  @FXML Button okButton;
  @FXML Label titleLabel;
  @FXML
  Pane root;
  String myWord;
  String myDefinition;
  DatabaseConnection databaseConnection = null;
  Connection connection = null;
  public static String type;
  public static String databaseName;

  @Override
  public void initialize(URL url, ResourceBundle resource)  {

    databaseConnection = new DatabaseConnection();
    connection = databaseConnection.getDatabaseConnection();
    titleLabel.setText(type);
    if (type.equals("Update")) {
      if( databaseName.equals("mydictionary")){
        wordTextField.setText(MyVocabularyController.selectedWord);
        definitonTextArea.setText(MyVocabularyController.selectedDefinition);
      }else {
        wordTextField.setText(EnViDicController.selectedWord);

      }
    }
  }

  public void buttonClicked() throws SQLException {
    myWord = wordTextField.getText();
    myDefinition = definitonTextArea.getText();

    if (myWord.isEmpty() || myDefinition.isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setHeaderText(null);
      alert.setContentText("Please fill all the data");
      alert.showAndWait();
    }
    if (type.equals("Update")) {
      update();
    } else if (type.equals("Add")) {
      Add();
    }
    close();
  }
  private void close(){
    Stage stage = (Stage) (root.getScene().getWindow());

    try {
      Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SideBar.fxml")));
      Scene scene = new Scene(root);
      scene.setFill(Color.TRANSPARENT);
      stage.setScene(scene);
    } catch(IOException ioe) {
      ioe.printStackTrace();
    }
  }

  private void Add() throws SQLException {
    String sqlQuery;
    if (databaseName.equals("dictionary") ){
       sqlQuery = "INSERT INTO dictionary (target,definition) VALUES (? ,? ) ";
      myDefinition = "<I><Q>" + myDefinition + "</Q></I>";   // web view format
    }
    else
       sqlQuery = "INSERT INTO mydictionary(english,vietnamese) VALUES (? ,? ) ";
    PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
    preparedStatement.setString(1, myWord);
    preparedStatement.setString(2, myDefinition);

    preparedStatement.execute();
    preparedStatement.close();

  }

  private void update() throws SQLException {
    String sqlQuery;
    if (databaseName.equals("mydictionary") ){
      sqlQuery = "UPDATE mydictionary SET english = ? , vietnamese=?  WHERE english = ? ";
    }else {
      sqlQuery = "UPDATE dictionary SET target = ? , definition =?  WHERE target = ? ";
    }
    PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
    preparedStatement.setString(1, myWord);
    preparedStatement.setString(2, myDefinition);
    preparedStatement.setString(3, myWord);
    preparedStatement.execute();
    preparedStatement.close();
  }
}
