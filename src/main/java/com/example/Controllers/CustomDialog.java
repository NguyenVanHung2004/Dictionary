package com.example.Controllers;

import com.example.Models.VocabModel;
import com.example.Services.DatabaseConnection;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;


public class CustomDialog implements Initializable {
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

  public void setDefinitionTextArea(String definitionText) {
    this.definitionTextArea.setText( definitionText);
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

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    definitionTextArea.setWrapText(true);
  }
}
