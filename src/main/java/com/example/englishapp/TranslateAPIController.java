package com.example.englishapp;

import com.example.englishapp.ApiConnection;
import com.example.englishapp.TextToSpeechAPI;
import com.example.englishapp.TranslateAPI;
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

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    inputSentence.setWrapText(true);
    outputSentence.setWrapText(true);
    translateButton.setDisable(true);

    System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
    try {
      File countriesTxt =
          new File(
              "G:\\Java\\OOP_BTL\\EnglishApp\\src\\main\\resources\\com\\example\\englishapp\\countries.txt");
      Scanner sc = new Scanner(countriesTxt);
      while (sc.hasNext()) {
        mapCountries.put(sc.nextLine(), sc.nextLine());
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    countriesObservableList.addAll(mapCountries.values());
    choiceBoxTranslateFrom.setItems(countriesObservableList);
    choiceBoxTranslateTo.setItems(countriesObservableList);
    choiceBoxTranslateTo.setValue(mapCountries.get("vi-VN"));
    choiceBoxTranslateFrom.setValue(mapCountries.get("en-GB"));
  }

  @FXML
  private void translateButtonOnClick() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            translateButton.setDisable(true);
            String input = inputSentence.getText();
            TranslateAPI translateAPIConnection = new TranslateAPI();
            translateAPIConnection.prepareQuery(input);
            String myDef =  translateAPIConnection.getOutPutString();
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
              TextToSpeechAPI textToSpeech = new TextToSpeechAPI();
              textToSpeech.prepareQuery(text);
              textToSpeech.Speak();
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
    textToSpeech(outputSentence.getText(), "vi-vn");
  }

  @FXML
  void speechInput() throws IOException, InterruptedException, LineUnavailableException {
    textToSpeech(inputSentence.getText(), "en-us");
  }

  @FXML
  void onKeyReleased() {
    if (!inputSentence.getText().isEmpty()) translateButton.setDisable(false);
    else translateButton.setDisable(true);
  }
}
