package com.example.Controllers;


import com.example.Services.DatabaseConnection;
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
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

import java.util.Objects;
import java.util.ResourceBundle;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SideBarController implements Initializable {

  @FXML StackPane contentArea;
  private Pane myWordPane ;
  private Pane EnViDicPane ;
  private Pane transApiPane;
  private Pane animationPane;

  private WebView webViewPane;
  private JFXDialog jfxDialogMyWord;
  private JFXDialog jfxDialogEnViDic;
  private JFXDialog jfxDialogTransAPI;
  private JFXDialog jfxDialogVietnamese;
  private JFXDialog jfxDialogGame;

  ObservableList<String> vocabModelObservableList = FXCollections.observableArrayList();

  @Override
  public void initialize(URL url, ResourceBundle resource) {

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
                                  Objects.requireNonNull(getClass().getResource("/com/example/view/my_vocabulary.fxml")));
                  new JFXDialog(contentArea, myWordPane, JFXDialog.DialogTransition.LEFT).show();
                  EnViDicPane =
                          FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/view/en_vi_dic.fxml")));
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              }
            };
    new Thread(task).start();
    try {
      animationPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/view/loading_animation.fxml")));
      new JFXDialog(contentArea, animationPane, JFXDialog.DialogTransition.LEFT).show();
      myWordPane =
             FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/view/my_vocabulary.fxml")));
      EnViDicPane =
            FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/view/en_vi_dic.fxml")));
      transApiPane =
            FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/view/TranslateAPI.fxml")));
      webViewPane =
              FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/view/WebView.fxml")));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @FXML
  public void clickedSideBarMyWord(MouseEvent event) throws IOException {
    closeAll();
    jfxDialogMyWord =
    new  JFXDialog(contentArea, myWordPane, JFXDialog.DialogTransition.LEFT);
            jfxDialogMyWord.show();

  }

  @FXML
  public void clickedSideBarVietnamese(MouseEvent event) throws IOException {
    closeAll();
    jfxDialogVietnamese =  new JFXDialog(contentArea, EnViDicPane , JFXDialog.DialogTransition.LEFT);
    jfxDialogVietnamese.show();
  }
  @FXML
  public void clickedTranslateAPI(MouseEvent event) throws IOException {
    closeAll();
    jfxDialogTransAPI = new JFXDialog(contentArea, transApiPane, JFXDialog.DialogTransition.LEFT);
    jfxDialogTransAPI.show();

  }
  public void clickedSideBarEnglish(MouseEvent event) {
    closeAll();
    contentArea.getChildren().removeAll();
    contentArea.getChildren().setAll(webViewPane);
  }
  public void clickedSideBarLogOut(MouseEvent event) {
    closeAll();
    Stage stage = (Stage) contentArea.getScene().getWindow();
    stage.close();
  }
  public  void clickedGame(MouseEvent event) throws IOException {
    closeAll();
    Pane game2Pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/view/game_pane.fxml")));
    contentArea.getChildren().removeAll();
    game2Pane.requestFocus();
    contentArea.getChildren().setAll(game2Pane);
    //jfxDialogGame = new  JFXDialog(contentArea, game2Pane, JFXDialog.DialogTransition.LEFT);
  //  jfxDialogGame.show();
  }
  public void closeAll(){
    if (jfxDialogEnViDic != null)
      closeJFXDialog(jfxDialogEnViDic);
    if (jfxDialogTransAPI != null)
      closeJFXDialog(jfxDialogTransAPI);
    if (jfxDialogVietnamese != null)
      closeJFXDialog(jfxDialogVietnamese);
    if (jfxDialogGame != null)
      closeJFXDialog(jfxDialogGame);
    if (jfxDialogMyWord != null)
      closeJFXDialog(jfxDialogMyWord);
  }
  public void closeJFXDialog( JFXDialog jfxDialog){
    jfxDialog.close();
  }
}
