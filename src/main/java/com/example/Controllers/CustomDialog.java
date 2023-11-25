package com.example.Controllers;

import com.example.Models.VocabModel;
import com.example.Services.DatabaseConnection;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomDialog  {
  @FXML TextField wordTextField;
  @FXML TextArea definitionTextArea;
  @FXML Button okButton;
  @FXML Label titleLabel;
  @FXML Pane root;
  String label;
  String myWord;
  String myDefinition;

  public void setLabel(String label) {
    this.label = label;
    titleLabel.setText(label);
  }

  public void setWordTextField(String wordTextField) {
    this.wordTextField.setText(wordTextField);
  }

  public void setDefinitionTextArea(String definitionTextArea) {
    this.definitionTextArea.setText( definitionTextArea);
  }


  public void addToDatabase(String databaseName) {
    myWord = wordTextField.getText();
    myDefinition = definitionTextArea.getText();

    if (myWord.isEmpty() || myDefinition.isEmpty()) {
      alertEmpty();
    }else {
      DatabaseConnection.insertToDatabase(databaseName, new VocabModel(myWord,myDefinition));
    }
  }
  public void updateToDatabase(String databaseName, String oldWord) {
    myWord = wordTextField.getText();
    myDefinition = definitionTextArea.getText();
    if (myWord.isEmpty() || myDefinition.isEmpty()) {
      alertEmpty();
    }else {
      DatabaseConnection.updateToDatabase(   databaseName , new VocabModel(myWord,myDefinition)  , oldWord);
    }
  }

  private void alertEmpty(){
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setHeaderText(null);
    alert.setContentText("Please fill all the data");
    alert.showAndWait();
  }
  /*
  private void close(){
    Stage stage = (Stage) (root.getScene().getWindow());
    try {
      Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/view/SideBar.fxml")));
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
       sqlQuery = "INSERT INTO dictionary (word,definition) VALUES (? ,? ) ";
      myDefinition = "<I><Q>" + myDefinition + "</Q></I>";   // web view format
    }
    else
       sqlQuery = "INSERT INTO mydictionary(word,definition) VALUES (? ,? ) ";
    PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
    preparedStatement.setString(1, myWord);
    preparedStatement.setString(2, myDefinition);
    preparedStatement.execute();
    preparedStatement.close();
  }

  private void update() throws SQLException {
    String sqlQuery;
    if (databaseName.equals("mydictionary") ){
      sqlQuery = "UPDATE mydictionary SET word = ? , definition=?  WHERE word = ? ";
    }else {
      sqlQuery = "UPDATE dictionary SET word = ? , definition =?  WHERE word = ? ";
    }
    PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
    preparedStatement.setString(1, myWord);
    preparedStatement.setString(2, myDefinition);
    preparedStatement.setString(3, myWord);
    preparedStatement.execute();
    preparedStatement.close();
  }


   */
}
