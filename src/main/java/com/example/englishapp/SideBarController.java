package com.example.englishapp;

import com.jfoenix.controls.JFXDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

import java.util.Objects;
import java.util.ResourceBundle;
import animatefx.animation.Bounce;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SideBarController implements Initializable {

  @FXML StackPane contentArea;
  private Pane myWordPane ;
  private Pane EnViDicPane ;
  private Pane transApiPane;
  private Pane animationPane;
  private Pane quizzPane;
  private WebView webViewPane;
  private Scene scene;
  ObservableList<String> vocabModelObservableList = FXCollections.observableArrayList();

  @Override
  public void initialize(URL url, ResourceBundle resource) {

    try {
      animationPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("loading_animation.fxml")));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    new JFXDialog(contentArea, animationPane, JFXDialog.DialogTransition.LEFT).show();

    Task<Void> task =
            new Task<>() {
              @Override
              protected Void call() throws Exception {

                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.getDatabaseConnection();

                String query = "SELECT target,definition FROM dictionary;";
                try {
                  Statement statement = connectDB.createStatement();
                  ResultSet queryOutput = statement.executeQuery(query);
                  while (queryOutput.next()) {
                    String myWord = queryOutput.getString("target");
                    vocabModelObservableList.add(myWord);
                  }
                } catch (SQLException e) {
                  Logger.getLogger(SideBarController.class.getName()).log(Level.SEVERE, null, e);
                }
                EnViDicController.vocabModelObservableList = vocabModelObservableList;

                return null;
              }
              @Override
              protected void succeeded() {
                super.succeeded();
                try {
                  myWordPane =
                          FXMLLoader.load(
                                  Objects.requireNonNull(getClass().getResource("my_vocabulary.fxml")));
                  new JFXDialog(contentArea, myWordPane, JFXDialog.DialogTransition.LEFT).show();
                  EnViDicPane =
                          FXMLLoader.load(Objects.requireNonNull(getClass().getResource("en_vi_dic.fxml")));
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              }
            };
    new Thread(task).start();
    try {
      animationPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("loading_animation.fxml")));
      new JFXDialog(contentArea, animationPane, JFXDialog.DialogTransition.LEFT).show();
      myWordPane =
             FXMLLoader.load(Objects.requireNonNull(getClass().getResource("my_vocabulary.fxml")));
      EnViDicPane =
            FXMLLoader.load(Objects.requireNonNull(getClass().getResource("en_vi_dic.fxml")));
      transApiPane =
            FXMLLoader.load(Objects.requireNonNull(getClass().getResource("TranslateAPI.fxml")));
      webViewPane =
              FXMLLoader.load(Objects.requireNonNull(getClass().getResource("WebView.fxml")));
      quizzPane =
              FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game_pane.fxml")));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @FXML
  public void clickedSideBarMyWord(MouseEvent event) throws IOException {

    new  JFXDialog(contentArea, myWordPane, JFXDialog.DialogTransition.LEFT).show();


  }
  @FXML
  public void clickedSideBarVietnamese(MouseEvent event) throws IOException {
    new JFXDialog(contentArea, EnViDicPane , JFXDialog.DialogTransition.LEFT).show();
  }
  @FXML
  public void clickedTranslateAPI(MouseEvent event) throws IOException {
    new JFXDialog(contentArea, transApiPane, JFXDialog.DialogTransition.LEFT).show();

  }
  public void clickedSideBarEnglish(MouseEvent event) {
    contentArea.getChildren().removeAll();
    contentArea.getChildren().setAll(webViewPane);
  }
  public void clickedSideBarLogOut(MouseEvent event) {

    Stage stage = (Stage) contentArea.getScene().getWindow();
    stage.close();
  }
  public void clickedQuizz(MouseEvent event) {
    new JFXDialog(contentArea, quizzPane, JFXDialog.DialogTransition.LEFT).show();
  }
}
