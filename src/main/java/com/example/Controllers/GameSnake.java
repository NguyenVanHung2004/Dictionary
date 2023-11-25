package com.example.Controllers;

import static javafx.scene.input.KeyCode.*;

import com.example.Models.VocabModel;
import com.example.Services.DatabaseConnection;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

class Letter{
    char c;
    int posX;
    int posY;
    Letter( char c){
        this.c = c;
        this.posX = 0 ;
        this.posY = 0 ;
    }
    Letter( char c, int x, int y ){
        this.c = c;
        this.posX = x ;
        this.posY = y ;
    }


}

public class GameSnake implements Initializable {
    @FXML
    StackPane stackPane;
    @FXML
    GridPane root;
    @FXML
     private Button button;
    @FXML
    private Label suggestionLabel;
    @FXML private Label scoreLabel;
    @FXML private Label wordExplain;

    private List<Point> snakeBody = new ArrayList<>();
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    Random random = new Random();
    private String currentWord;
    private String currentDefinition;
    StringBuilder myWord = new StringBuilder();
    private boolean isWin = false;
    private Point snakeHead;
    private GraphicsContext gc;
    private int currentDirection;
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    boolean isStart;
    private int score = 0;
    List<String> wordList = new ArrayList<>();
    char[] characters;

    List<String> wordExplainList = new ArrayList<>();
    List<Letter> myLetters = new LinkedList<>();
    boolean isGameOver ;
    Timeline timeline;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        isStart = false;
        loadVocabFromDatabase();
        selectRandomWord();
        button.requestFocus();
        generateFood();
        button.addEventFilter(
                KeyEvent.KEY_PRESSED,
                event -> {
                    if (event.getCode() == KeyCode.LEFT ) {
                        System.out.println("The 'L' key was pressed");
                        if (currentDirection != RIGHT) {
                            currentDirection = LEFT;
                        }

                        event.consume();
                    }
                    if (event.getCode() == KeyCode.RIGHT) {
                        if (currentDirection != LEFT) {
                            currentDirection = RIGHT;
                        }
                        isStart =true;
                        System.out.println("The 'R' key was pressed");
                        event.consume();
                    }
                    if (event.getCode() == KeyCode.DOWN) {
                        if (currentDirection != UP) {
                            currentDirection = DOWN;
                        }
                        System.out.println("The 'D' key was pressed");
                        event.consume();
                    }
                    if (event.getCode() == KeyCode.UP) {
                        if (currentDirection != DOWN) {
                            currentDirection = UP;
                        }
                        System.out.println("The 'W' key was pressed");
                        event.consume();
                    }
                });

        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        timeline = new Timeline(new KeyFrame(Duration.millis(200 ), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }
    public void loadVocabFromDatabase() {
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

    public void selectRandomWord() {
        int currentWordIndex;
        do {
            currentWordIndex = random.nextInt(wordList.size());
            currentWord = wordList.get(currentWordIndex);
        } while (currentWord.length() > 7);

        String temp = currentWord;
        currentDefinition = wordExplainList.get(currentWordIndex);
        wordExplain.setText(currentDefinition);
        List<Character> characters = new ArrayList<>();
        for (char c : temp.toCharArray()) {
            characters.add(c);
        }

        Collections.shuffle(characters);

        StringBuilder shuffle = new StringBuilder();
        for (char c : characters) {
            shuffle.append(c);
        }
        suggestionLabel.setText(shuffle.toString());

        wordList.remove(currentWordIndex);
    }
    private Image loadLetterImage( Character c ){
       String imagePath =
                "G:\\Java\\OOP_BTL\\EnglishApp\\src\\main\\resources\\com\\example\\image\\"
                        + Character.toLowerCase(c)
                        + ".jpg";

       return new Image(imagePath);
    }
    private void run(GraphicsContext gc) {
        drawBackground(gc);
        drawSnake(gc);
        drawFood(gc);
        if ( ! isStart){
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 30));
            gc.fillText("Press right arrow key to start", WIDTH / 3.5, (double) HEIGHT / 2);
            return;
        }
        if (isGameOver) {
            timeline.pause();
            gameOver();
            return;
        }



        switch (currentDirection) {
            case RIGHT -> moveRight();
            case LEFT -> moveLeft();
            case UP -> moveUp();
            case DOWN -> moveDown();
        }
        checkGameOver();
        eatFood();
        checkAnswer();
        if ( isWin )
            loadNewLevel();

    }

    private void drawFood(GraphicsContext gc) {
        for (Letter myLetter : myLetters) {
            gc.drawImage(loadLetterImage(myLetter.c), myLetter.posX * SQUARE_SIZE, myLetter.posY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        }
    }
    private void eatFood() {
        for (Letter myLetter : myLetters) {
            if (snakeHead.getX() == myLetter.posX && snakeHead.getY() == myLetter.posY) {
                snakeBody.add(new Point(-1, -1));
                myLetter.posX = -100;
                myLetter.posY = -100;
                myWord.append(myLetter.c);
                System.out.println(myWord);

            }
        }
    }
    private void generateFood(){
        String temp = currentWord;
        System.out.println(temp);
        char[] characters = temp.toCharArray();

        for (char character : characters) {

            Letter letter = new Letter(character, (int) (Math.random() * ROWS), ((int) (Math.random() * ROWS)));
            myLetters.add(letter);
        }
    }
    private void drawSnake(GraphicsContext gc) {

        gc.setFill(Color.web("4674E9"));
        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);

        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE, SQUARE_SIZE - 1,
                    SQUARE_SIZE - 1, 20, 20);
        }

        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }
    }
    private void drawBackground(GraphicsContext gc) {
        scoreLabel.setText(String.valueOf(score));
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
    private void checkAnswer(){
        StringBuilder currWord = new StringBuilder(currentWord);
        if ( myWord.length() == currWord.length())
            if (myWord.compareTo(currWord) == 0){
                isWin = true;
                score++;
                System.out.println("correct");
        }
    }
    private void loadNewLevel(){
        snakeBody.clear();
        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        isWin =false;
        selectRandomWord();
        myWord.delete(0, myWord.length());
        myLetters.clear();
        generateFood();

    }
    private void moveRight() {
        snakeHead.x++;
    }

    private void moveLeft() {
        snakeHead.x--;
    }

    private void moveUp() {
        snakeHead.y--;
    }

    private void moveDown() {
        snakeHead.y++;
    }
    @FXML
    private void onPlayAgainClick(){
        timeline.play();
        score = 0 ;
        isGameOver = false;
        loadNewLevel();
    }
    public void checkGameOver() {
        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x * SQUARE_SIZE >= WIDTH || snakeHead.y * SQUARE_SIZE >= HEIGHT) {
            isGameOver = true;
        }

        //destroy itself
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY()) {
                isGameOver = true;
                break;
            }
        }
    }
    public void gameOver(){

        JFXDialogLayout content  = new JFXDialogLayout();
    content.setHeading(new Text("Heading"));
    content.setBody(new Text("Your score is " + score
                                + "\nThe word is " + currentWord
                                + " \nDo you want save it to my dictionary? "));
        JFXButton okButton = new JFXButton("OK");
        JFXButton cancelButton = new JFXButton("CANCEL");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        okButton.setOnAction(actionEvent -> {
            DatabaseConnection.insertToDatabase("mydictionary", new VocabModel(currentWord,currentDefinition));
            dialog.close();
        });
        cancelButton.setOnAction(actionEvent -> {
            dialog.close();
        });
        content.setActions(okButton, cancelButton);

        dialog.show();

    }



}
