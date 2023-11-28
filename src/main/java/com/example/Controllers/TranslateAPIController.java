package com.example.Controllers;

import com.example.Services.AudioPlayer;
import com.example.Services.NoInternetException;
import com.example.Services.TranslateAPI;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import javax.sound.sampled.LineUnavailableException;

public class TranslateAPIController implements Initializable {
  @FXML ImageView ttsInputImageView;
  @FXML ImageView ttsOutputImageView;
  @FXML StackPane root;
  @FXML TextArea inputSentence;
  @FXML TextArea outputSentence;
  @FXML Button translateButton;
  @FXML ChoiceBox<String> choiceBoxTranslateTo;
  @FXML ChoiceBox<String> choiceBoxTranslateFrom;
  StringProperty stringInputProperty = new SimpleStringProperty(null);
  StringProperty stringOutPutProperty = new SimpleStringProperty(null);

  private final ObservableList<String> countriesObservableList =
      FXCollections.observableArrayList();
  private final Map<String, String> mapCountries = new LinkedHashMap<>();
  AudioPlayer audioPlayer = AudioPlayer.getInstance();

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    inputSentence.setWrapText(true);
    outputSentence.setWrapText(true);
    translateButton.setDisable(true);
    outputSentence.setEditable(false);
    ttsInputImageView.visibleProperty().bind(Bindings.isEmpty(stringInputProperty).not());
    ttsOutputImageView.visibleProperty().bind(Bindings.isEmpty(stringOutPutProperty).not());
    inputSentence
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              stringInputProperty.set(newValue);
              if (oldValue != null && newValue.length() < oldValue.length()) {
                outputSentence.clear();
                stringOutPutProperty.set("");
                audioPlayer.stop();
              }
            });
    System.setProperty(
        "freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
    mapCountries.put("English", "en-us");
    mapCountries.put("Vietnamese", "vi-vn");
    countriesObservableList.addAll(mapCountries.keySet());
    choiceBoxTranslateFrom.setItems(countriesObservableList);
    choiceBoxTranslateTo.setItems(countriesObservableList);
    choiceBoxTranslateFrom.setValue("English");
    choiceBoxTranslateTo.setValue("Vietnamese");
  }

  @FXML
  private void translateButtonOnClick() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            translateButton.setDisable(true);
            String input = inputSentence.getText();
            TranslateAPI translateAPIConnection = TranslateAPI.getInstance();
            String inputLang = mapCountries.get(choiceBoxTranslateFrom.getValue()).substring(0, 2);
            String outputLang = mapCountries.get(choiceBoxTranslateTo.getValue()).substring(0, 2);
            translateAPIConnection.prepareQuery(input, inputLang + "|" + outputLang);
              String myDef = null;
              try {
                  myDef = translateAPIConnection.getOutPutString();
              } catch (NoInternetException e) {
                  Platform.runLater(
                          () -> {
                              openErrorDialog();
                          });
              }
              System.out.println(myDef);
            outputSentence.setText(myDef);
            stringOutPutProperty.set(myDef);
            return null;
          }

          protected void succeeded() {
            translateButton.setDisable(false);
          }
        };
    new Thread(task).start();
  }

  void textToSpeech(String text, String language) {
    {
      Task<Void> task =
          new Task<>() {
            @Override
            protected Void call() {
              try {
                audioPlayer.prepareQuery(text, language);
                audioPlayer.speak();
              } catch (NoInternetException e) {
                Platform.runLater(
                    () -> {
                        openErrorDialog();
                    });
              } catch (IOException | LineUnavailableException e) {
                  throw new RuntimeException(e);
              }
              return null;
            }
          };
      new Thread(task).start();
    }
  }

  @FXML
  void speechOutput() {
    textToSpeech(outputSentence.getText(), mapCountries.get(choiceBoxTranslateTo.getValue()));
  }

  @FXML
  void speechInput() {
    textToSpeech(inputSentence.getText(), mapCountries.get(choiceBoxTranslateFrom.getValue()));
  }
  void openErrorDialog(){
      JFXDialogLayout content = new JFXDialogLayout();
      content.setHeading(new Text("Error"));
      content.setBody(
              new Text(
                      "No Internet Connection.\nPlease check your internet connection and try again."));

      JFXButton okButton = new JFXButton("Okay");
      JFXDialog dialog =
              new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
      okButton.setOnAction(actionEvent -> dialog.close());
      content.setActions(okButton);
      dialog.show();
  }

  @FXML
  void onKeyReleased() {
    if (!inputSentence.getText().isEmpty()) {
      translateButton.setDisable(false);
    } else {
      translateButton.setDisable(true);
      outputSentence.clear();
    }
  }
}
