package com.example.Controllers;

import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController {
    @FXML
    Button quizzButton ;
    @FXML
    StackPane root;

    static JFXDialog jfxDialog;
    @FXML
    void clickedQuizzButton() throws IOException {
        Pane quizzModel = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/com/example/view/quizz_model_pane.fxml")));
        jfxDialog=   new JFXDialog(root, quizzModel, JFXDialog.DialogTransition.LEFT );
        jfxDialog.show();
  }
    public static void closeDialog(){
        jfxDialog.close();
    }

}
