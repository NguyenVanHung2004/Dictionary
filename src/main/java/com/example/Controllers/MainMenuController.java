package com.example.Controllers;

import com.example.Services.AudioPlayer;
import com.example.Services.DatabaseConnection;
import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainMenuController implements Initializable {

  @FXML StackPane contentArea;
  private Pane myWordPane;
  private Pane EnViDicPane;
  private Pane transApiPane;
   private Pane webViewPane;
  private JFXDialog jfxDialogMyWord;
  private JFXDialog jfxDialogEnViDic;
  private JFXDialog webView;
  private JFXDialog jfxDialogTransAPI;
  private JFXDialog jfxDialogVietnamese;
  private JFXDialog jfxDialogGame;
  static JFXDialog jfxDialogAnimation;

  @Override
  public void initialize(URL url, ResourceBundle resource) {

    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call(){
            DatabaseConnection.getAllWordFromDatabaseToTrie("dictionary");
            return null;
          }
          @Override
          protected void succeeded() {
            super.succeeded();
            try {
              jfxDialogAnimation.close();
              myWordPane =
                  FXMLLoader.load(
                      Objects.requireNonNull(
                          getClass().getResource("/com/example/view/my_vocabulary.fxml")));
              new JFXDialog(contentArea, myWordPane, JFXDialog.DialogTransition.LEFT).show();
              EnViDicPane =
                  FXMLLoader.load(
                      Objects.requireNonNull(
                          getClass().getResource("/com/example/view/en_vi_dic.fxml")));
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
        };
    new Thread(task).start();
    try {
      Pane animationPane = FXMLLoader.load(
              Objects.requireNonNull(
                      getClass().getResource("/com/example/view/loading_animation.fxml")));
      jfxDialogAnimation =
          new JFXDialog(contentArea, animationPane, JFXDialog.DialogTransition.LEFT);
      jfxDialogAnimation.show();
      transApiPane =
          FXMLLoader.load(
              Objects.requireNonNull(
                  getClass().getResource("/com/example/view/TranslateAPI.fxml")));
      webViewPane =
          FXMLLoader. load(
              Objects.requireNonNull(getClass().getResource("/com/example/view/WebView.fxml")));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @FXML
  public void clickedSideBarMyWord() throws IOException {
    closeAll();
    MyDictionaryController.myVocabObservableList.clear();
    myWordPane =
        FXMLLoader.load(
            Objects.requireNonNull(getClass().getResource("/com/example/view/my_vocabulary.fxml")));
    new JFXDialog(contentArea, myWordPane, JFXDialog.DialogTransition.LEFT).show();
    jfxDialogMyWord = new JFXDialog(contentArea, myWordPane, JFXDialog.DialogTransition.LEFT);
    jfxDialogMyWord.show();
  }

  @FXML
  public void clickedSideBarVietnamese()   {
    closeAll();
    jfxDialogVietnamese = new JFXDialog(contentArea, EnViDicPane, JFXDialog.DialogTransition.LEFT);
    jfxDialogVietnamese.show();
  }

  @FXML
  public void clickedTranslateAPI()   {
    closeAll();
    jfxDialogTransAPI = new JFXDialog(contentArea, transApiPane, JFXDialog.DialogTransition.LEFT);
    jfxDialogTransAPI.show();
  }
  @FXML
  public void clickedSideBarEnglish() {
    closeAll();
    webView = new JFXDialog(contentArea, webViewPane, JFXDialog.DialogTransition.LEFT);
    webView.show();
  }

  public void clickedSideBarLogOut() {
    closeAll();
    Stage stage = (Stage) contentArea.getScene().getWindow();
    stage.close();
  }

  public void clickedGame() throws IOException {
    closeAll();
    Pane gamePane = FXMLLoader.load(
            Objects.requireNonNull(getClass().getResource("/com/example/view/game_pane.fxml")));
    gamePane.requestFocus();
    jfxDialogGame = new JFXDialog(contentArea, gamePane, JFXDialog.DialogTransition.LEFT);
    jfxDialogGame.show();
  }

  public void closeAll() {
    AudioPlayer.getInstance().stop();
    if (jfxDialogEnViDic != null) closeJFXDialog(jfxDialogEnViDic);
    if (jfxDialogTransAPI != null) closeJFXDialog(jfxDialogTransAPI);
    if (jfxDialogVietnamese != null) closeJFXDialog(jfxDialogVietnamese);
    if (jfxDialogGame != null) closeJFXDialog(jfxDialogGame);
    if (jfxDialogMyWord != null) closeJFXDialog(jfxDialogMyWord);
  }

  public void closeJFXDialog(JFXDialog jfxDialog) {
    jfxDialog.close();
  }
}
