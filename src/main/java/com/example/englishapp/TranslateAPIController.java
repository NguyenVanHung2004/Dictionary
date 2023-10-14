package com.example.englishapp;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import java.net.URL;
import java.util.*;
import javax.speech.Central;

import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

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
    System.setProperty("freetts.voices","com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
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
    String apiUrl = "https://api.mymemory.translated.net/get?q=";
    apiConnection = new ApiConnection(apiUrl);
    countriesObservableList.addAll(mapCountries.values());
    choiceBoxTranslateFrom.setItems(countriesObservableList);
    choiceBoxTranslateTo.setItems(countriesObservableList);
    choiceBoxTranslateTo.setValue(mapCountries.get("vi-VN"));
    choiceBoxTranslateFrom.setValue(mapCountries.get("en-GB"));
  }

  @FXML
  private void translateButtonOnClick() {
    Task<Void> task= new Task<>() {
      @Override
      protected Void call() throws Exception {
        String input = inputSentence.getText();
        String translateFrom = getChoiceBoxTranslateFrom().getValue();
        String translateTo = getChoiceBoxTranslateTo().getValue();
        JSONObject jsonObject =
                apiConnection.getJSONObject(input + "&langpair=" + translateFrom + "|" + translateTo);
        JSONObject responseData = (JSONObject) jsonObject.get("responseData");
        String myDef = responseData.get("translatedText").toString();
        System.out.println(myDef);
        outputSentence.setText(myDef);
        return null;
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
  void textToSpeech(String text )  {

    try {
      Central.registerEngineCentral(
              "com.sun.speech.freetts"
                      + ".jsapi.FreeTTSEngineCentral");
      Synthesizer synthesizer
              = Central.createSynthesizer(
              new SynthesizerModeDesc(Locale.US));

      synthesizer.allocate();

      synthesizer.resume();

      synthesizer.speakPlainText(
              text, null);
      synthesizer.waitEngineState(
              Synthesizer.QUEUE_EMPTY);

    }

    catch (Exception e) {
      e.printStackTrace();
    }
  }


  @FXML
  void speechOutput() {
    textToSpeech(outputSentence.getText() );
  }
  @FXML
  void speechInput() {
    textToSpeech(inputSentence.getText() );
  }


}
