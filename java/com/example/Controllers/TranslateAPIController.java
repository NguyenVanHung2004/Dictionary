package com.example.Controllers;

import com.example.Services.ApiConnection;
import com.example.Services.AudioPlayer;
import com.example.Services.TextToSpeechAPI;
import com.example.Services.TranslateAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.HttpURLConnection;

import org.json.simple.JSONObject;

import java.net.URL;

import java.util.*;

import javax.sound.sampled.*;

public class TranslateAPIController implements Initializable {
  @FXML TextArea inputSentence;
  @FXML TextArea outputSentence;
  @FXML Button translateButton;
  @FXML ChoiceBox<String> choiceBoxTranslateTo;
  @FXML ChoiceBox<String> choiceBoxTranslateFrom;

  private final ObservableList<String> countriesObservableList =
      FXCollections.observableArrayList();
  private final Map<String, String> mapCountries = new LinkedHashMap<>();
  ApiConnection apiConnection;
  AudioPlayer audioPlayer = AudioPlayer.getInstance();

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    inputSentence.setWrapText(true);
    outputSentence.setWrapText(true);

    translateButton.setDisable(true);
    outputSentence.setEditable(false);
    inputSentence.textProperty().addListener((observable, oldValue, newValue)->{
      if( oldValue  != null && newValue.length() < oldValue.length()){
        outputSentence.clear();
        audioPlayer.stop();
      }
    });
    System.setProperty(
        "freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
   /* try {
      File countriesTxt =
          new File(
              "G:\\Java\\OOP_BTL\\EnglishApp\\src\\main\\resources\\com\\example\\text\\countries.txt");
      Scanner sc = new Scanner(countriesTxt);
      while (sc.hasNext()) {
        mapCountries.put(sc.nextLine(), sc.nextLine());
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }*/

    mapCountries.put("English","en-us");
    mapCountries.put( "Vietnamese","vi-vn");

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
          protected Void call() throws Exception {
            translateButton.setDisable(true);
            String input = inputSentence.getText();
            TranslateAPI translateAPIConnection = TranslateAPI.getInstance();
            String inputLang =  mapCountries.get(  choiceBoxTranslateFrom.getValue() ).substring(0,2);
            String ouputLang =  mapCountries.get(  choiceBoxTranslateTo.getValue() ).substring(0,2);
            translateAPIConnection.prepareQuery(input, inputLang +"|" + ouputLang);
            String myDef = translateAPIConnection.getOutPutString();
            System.out.println(myDef);
            outputSentence.setText(myDef);
            return null;
          }

          protected void succeeded() {
            translateButton.setDisable(false);
          }
        };
    new Thread(task).start();
  }

  public ChoiceBox<String> getChoiceBoxTranslateFrom() {
    return choiceBoxTranslateFrom;
  }

  public ChoiceBox<String> getChoiceBoxTranslateTo() {
    return choiceBoxTranslateTo;
  }

  void textToSpeech(String text, String language)
      throws IOException, InterruptedException, LineUnavailableException {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            try {
              audioPlayer.prepareQuery(text ,language );
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

  @FXML
  void speechOutput() throws IOException, InterruptedException, LineUnavailableException {
    textToSpeech(outputSentence.getText(), mapCountries.get(  choiceBoxTranslateTo.getValue()));
  }

  @FXML
  void speechInput() throws IOException, InterruptedException, LineUnavailableException {
    textToSpeech(inputSentence.getText(), mapCountries.get( choiceBoxTranslateFrom.getValue()));
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
