package com.example.Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;


import java.net.URL;
import java.util.ResourceBundle;

public class WebViewController implements Initializable {

    @FXML
    WebView webView ;
    @FXML
    TextField urlTextField;
    @FXML
    ImageView backButton;

    WebEngine webEngine;
    WebHistory history;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = webView.getEngine();
        webEngine.load("https://dictionary.cambridge.org/");
        urlTextField.setText("https://dictionary.cambridge.org");
        history =  webEngine.getHistory();
        backButton.visibleProperty().bind(history.currentIndexProperty().greaterThan(0));
    }
    @FXML
    public void refreshPage(){
        webEngine.reload();
    }
    public void back(){
        history = webEngine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        if ( history.getCurrentIndex() > 1){
        history.go(-1);
        urlTextField.setText(entries.get(history.getCurrentIndex()).getUrl());
      }
    }
    public void home(){
        webEngine.load("https://dictionary.cambridge.org/");
    }
}
