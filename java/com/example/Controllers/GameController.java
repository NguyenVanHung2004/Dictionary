package com.example.Controllers;

import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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

    static JFXDialog quizz;

    @FXML
    void clickedQuizzButton() throws IOException {
        Pane quizzPane = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/com/example/view/quizz_model_pane.fxml")));
        quizz =   new JFXDialog(root, quizzPane, JFXDialog.DialogTransition.LEFT );
        quizz.show();
  }
    @FXML
    void gameTetrisClicked() throws IOException {
        Pane tetris =
                FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/view/game_tetris.fxml")));
        JFXDialog gameTetris =   new JFXDialog(root, tetris, JFXDialog.DialogTransition.LEFT );
        gameTetris.requestFocus();
        gameTetris.show();
    }
    @FXML
    void gameSnakeClicked() throws IOException{
        Pane gameSnakePane =
                FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/view/game_snake.fxml")));
        JFXDialog gameSnake =  new JFXDialog(root, gameSnakePane, JFXDialog.DialogTransition.LEFT );
        gameSnake.requestFocus();
        gameSnake.show();
    }
    public static void closeDialog(){
        quizz.close();
    }

}
