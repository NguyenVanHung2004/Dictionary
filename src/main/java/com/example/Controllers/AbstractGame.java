package com.example.Controllers;

import com.example.Models.VocabModel;
import com.example.Services.DatabaseConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public abstract class AbstractGame {
    @FXML
    StackPane stackPane;
    List<String>  wordExplainList = new ArrayList<>();
    List<String> wordList = new ArrayList<>();
    String currentWord;
    String currentDefinition;
    int score = 0;

    protected void loadVocabFromDatabase() {

        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connectDB = connectNow.getDatabaseConnection();
        String query = "SELECT * FROM enendic;";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            while (queryOutput.next()) {
                String myWord = queryOutput.getString("word");
                String myDefinition = queryOutput.getString("definition");
                wordList.add(myWord);
                wordExplainList.add(myDefinition);
            }
        } catch (SQLException e) {
            Logger.getLogger(SideBarController.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    protected Image loadLetterImage(char c) {
        String imagePath =
                "G:\\Java\\OOP_BTL\\EnglishApp\\src\\main\\resources\\com\\example\\image\\"
                        + Character.toLowerCase(c)
                        + ".jpg";

        return new Image(imagePath);
    }
    protected void gameOver() {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("GAME OVER"));
        content.setBody(
                new Text(
                        "Your score is "
                                + score
                                + "\nThe word is "
                                + currentWord.toUpperCase()
                                + " \nDo you want save it to my dictionary? "));
        JFXButton okButton = new JFXButton("OK");
        JFXButton cancelButton = new JFXButton("CANCEL");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        okButton.setOnAction(
                actionEvent -> {
                    DatabaseConnection.insertToDatabase(
                            "mydictionary", new VocabModel(currentWord, currentDefinition));
                    dialog.close();
                });
        cancelButton.setOnAction(
                actionEvent -> {
                    dialog.close();
                });
        content.setActions(okButton, cancelButton);
        dialog.show();
    }
    abstract void selectRandomWord();
    abstract void runLoop(GraphicsContext gc);
    abstract void drawBackground(GraphicsContext gc);
    abstract void loadNewLevel();
    abstract void checkAnswer();
    abstract void checkGameOver();
    abstract void moveLeft();
    abstract void moveRight();
    abstract void moveDown();

}
