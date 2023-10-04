package com.example.englishapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class addVocabDialogController implements Initializable {
  @FXML TextField wordTextField;
  @FXML TextArea definitonTextArea;
  @FXML Button okButton;
  @FXML Label titleLabel;
  String myWord;
  String myDefinition;
  DatabaseConnection databaseConnection = null;
  Connection connection = null;
  public static String type;

  @Override
  public void initialize(URL url, ResourceBundle resource) {

    databaseConnection = new DatabaseConnection();
    connection = databaseConnection.getDatabaseConnection();
    titleLabel.setText(type);
    if (type.equals("Update")) {
      wordTextField.setText(MyVocabularyController.selectedWord);
      definitonTextArea.setText(MyVocabularyController.selectedDefinition);
    }else{
      wordTextField.setText(VietnameseController.selectedWord);
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
    Stage stage = (Stage) okButton.getScene().getWindow();
    stage.close();

  }

  private void Add() throws SQLException {

    String sqlQuery = "INSERT INTO mydictionary(english,vietnamese) VALUES (? ,? ) ";
    PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
    preparedStatement.setString(1, myWord);
    preparedStatement.setString(2, myDefinition);

    preparedStatement.execute();
    preparedStatement.close();

  }

  private void update() throws SQLException {
    String sqlQuery = "UPDATE mydictionary SET english = ? , vietnamese=?  WHERE english = ? ";
    PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
    preparedStatement.setString(1, myWord);
    preparedStatement.setString(2, myDefinition);
    preparedStatement.setString(3, myWord);
    preparedStatement.execute();
    preparedStatement.close();
  }
}
