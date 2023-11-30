package com.example.Controllers;

import com.example.Services.NoInternetException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;


import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class WebViewController implements Initializable {

    @FXML
    WebView webView ;
    @FXML
    TextField urlTextField;
    @FXML
    ImageView backButton;
    @FXML
    StackPane root;

    WebEngine webEngine;
    WebHistory history;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = webView.getEngine();
        try {
            checkInternetConnection();
        } catch (NoInternetException e) {
            openErrorDialog("No Internet connection");
        }
        webEngine.load("https://dictionary.cambridge.org/");
        urlTextField.setText("https://dictionary.cambridge.org");
        urlTextField.setEditable(false);
        history =  webEngine.getHistory();
        backButton.visibleProperty().bind(history.currentIndexProperty().greaterThan(0));
    }
    @FXML
    public  void refreshPage(){
        try {
            checkInternetConnection();
        } catch (NoInternetException e) {
            openErrorDialog("No Internet connection");
        }
        webEngine.reload();
    }
    public void back(){
        try {
            checkInternetConnection();
        } catch (NoInternetException e) {
            openErrorDialog("No Internet connection");
        }
        history = webEngine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        if ( history.getCurrentIndex() > 1){
        history.go(-1);
        urlTextField.setText(entries.get(history.getCurrentIndex()).getUrl());

      }
    }
    public void home(){
        try {
            checkInternetConnection();
        } catch (NoInternetException e) {
            openErrorDialog("No Internet connection");
        }
        webEngine.load("https://dictionary.cambridge.org/");

    }

    public static void checkInternetConnection() throws NoInternetException {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            if (!address.isReachable(2000)) {
                throw new NoInternetException("No internet connection");
            }
        } catch (Exception e) {
            throw new NoInternetException("Cannot check Internet connection: " + e.getMessage());
        }
    }
    void openErrorDialog(String text) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(text));
        content.setBody(new Text(text));
        JFXButton okButton = new JFXButton("Okay");
        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
        okButton.setOnAction(actionEvent ->
                {
                dialog.close();
                MainMenuController.webView.close();
                }
        );
        content.setActions(okButton);
        dialog.show();
    }
    @FXML
    void mousePressed(){
        try {
            checkInternetConnection();
        } catch (NoInternetException e) {
           openErrorDialog("No Internet");
        }
    }
}
