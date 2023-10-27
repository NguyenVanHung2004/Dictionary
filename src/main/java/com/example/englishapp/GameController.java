package com.example.englishapp;

import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class GameController {
    @FXML
    Button quizzButton ;
    @FXML
    StackPane root;
    private Pane quizzModel;
@FXML
    void clickedQuizzButton() throws IOException {
        quizzModel = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("quizz_model_pane.fxml")));
        JFXDialog jfxDialog =   new JFXDialog(root, quizzModel , JFXDialog.DialogTransition.LEFT );
        jfxDialog.show();

  }
}
