package com.example.englishapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TranslateAPIController {
    @FXML
    TextArea inputSentence;
    @FXML
    TextArea outputSentence;
    @FXML
    Button translateButton;
    private final String apiUrl = "https://api.mymemory.translated.net/get?q=";
    @FXML
    private void translateButtonOnClick(){
        String input = inputSentence.getText();
        // outputSentence.setText(input);
        ApiConnection apiConnection = new ApiConnection( apiUrl);
        JSONObject jsonObject = apiConnection.getJSONObject(input+ "&langpair=en|vi");
        JSONObject responseData =  (JSONObject) jsonObject.get("responseData");
        String myDef = responseData.get("translatedText").toString();

        System.out.println(myDef);
        outputSentence.setText(myDef);
    }
}
