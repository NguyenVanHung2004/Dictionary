package com.example.Controllers;

import com.example.Models.Letter;
import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class GameTetris extends AbstractGame implements Initializable {

  @FXML private Button button;
  @FXML private Pane pane;
  @FXML private Pane pane1;

  @FXML private Pane pane2;

  @FXML private Pane pane3;

  @FXML private Pane pane4;

  @FXML private Pane pane5;

  @FXML private Pane pane6;

  @FXML private Pane pane7;
  @FXML private Pane pane8;
  @FXML private Label scoreLabel;

  @FXML private Label wordExplain;
  @FXML private Label suggestionLabel;
  private final List<Letter> lettersInPaneList = new ArrayList<>();
  List<Pane> panesList = new ArrayList<>();

  StringBuilder myAnswer = new StringBuilder();
  List<Character> letterInWordList = new ArrayList<>();

  private static final int WIDTH = 580;
  private static final int HEIGHT = 500;

  Letter currentLetter;
  int currColumnIndex = 1;

  boolean isWin;
  boolean isGameOver;
  List<Boolean> isEmptyPane;
  Random random = new Random();
  Timeline timeline;
  private GraphicsContext gc;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    Pane animationPane = null;
    try {
      animationPane =
          FXMLLoader.load(
              Objects.requireNonNull(
                  getClass().getResource("/com/example/view/loading_animation.fxml")));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    JFXDialog jfxDialogAnimation =
        new JFXDialog(stackPane, animationPane, JFXDialog.DialogTransition.LEFT);
    jfxDialogAnimation.show();
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws Exception {

            loadVocabFromDatabase();
            return null;
          }

          @Override
          protected void succeeded() {
            super.succeeded();
            jfxDialogAnimation.close();
            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            pane.getChildren().add(canvas);
            gc = canvas.getGraphicsContext2D();
            panesList.add(pane1);
            panesList.add(pane2);
            panesList.add(pane3);
            panesList.add(pane4);
            panesList.add(pane5);
            panesList.add(pane6);
            panesList.add(pane7);
            panesList.add(pane8);
            button.requestFocus();
            button.focusTraversableProperty();
            button.addEventFilter(
                KeyEvent.KEY_PRESSED,
                event -> {
                  if (event.getCode() == KeyCode.LEFT) {
                    moveLeft();
                    event.consume();
                  }
                  if (event.getCode() == KeyCode.RIGHT) {
                    moveRight();
                    event.consume();
                  }
                  if (event.getCode() == KeyCode.DOWN) {
                    moveDown();
                    event.consume();
                  }
                });
            loadNewLevel();
            timeline = new Timeline(new KeyFrame(Duration.millis(50), e -> runLoop(gc)));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
          }
        };
    new Thread(task).start();
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

    isEmptyPane = new ArrayList<>();
    for (int i = 0; i < currentWord.length(); i++) {
      letterInWordList.add(currentWord.charAt(i));
    }
    System.out.println(currentWord);
    for (Pane p : panesList) p.setVisible(false);
    for (int i = 0; i < currentWord.length(); i++) {
      panesList.get(i).setVisible(true);
      isEmptyPane.add(true);
    }
  }

  public void selectRandomLetterInWord() {
    if (!letterInWordList.isEmpty()) {
      int randomIndex = random.nextInt(letterInWordList.size());
      currentLetter = new Letter(letterInWordList.get(randomIndex), currColumnIndex, 100);
      System.out.println(currentLetter.c);
      letterInWordList.remove(randomIndex);
    }
  }

  @Override
  protected void runLoop(GraphicsContext gc) {
    if (isWin) {
      loadNewLevel();
    }
    if (isGameOver) {
      timeline.pause();
      gameOver();
      return;
    }
    gc.clearRect(0, 0, 500, 500);
    drawBackground(gc);
    drawCurrentLetter(gc);

    fallingDown();
  }

  public void drawBackground(GraphicsContext gc) {
    for (Letter letters : lettersInPaneList) {
      if (letters != null)
        gc.drawImage(loadLetterImage(letters.c), letters.posX, letters.posY, 50, 50);
    }
  }

  private void drawCurrentLetter(GraphicsContext gc) {
    gc.drawImage(
        loadLetterImage(currentLetter.c),
        (int) getLayoutXOfPane(currentLetter.getPosX()),
        currentLetter.posY,
        50,
        50);
  }

  private void fallingDown() {

    if (currentLetter.posY <= pane1.getLayoutY() - 50) {
      currentLetter.posY += 5;
    } else {
      lettersInPaneList.add(
          currColumnIndex - 1,
          new Letter(
              currentLetter.c, (int) getLayoutXOfPane(currColumnIndex), (int) pane1.getLayoutY()));
      myAnswer.setCharAt(currColumnIndex - 1, currentLetter.c);
      checkGameOver();
      if (isWin || isGameOver) {
        return;
      }
      isEmptyPane.set(currColumnIndex - 1, false);
      selectRandomLetterInWord();
    }
  }

  @Override
  public void loadNewLevel() {
    letterInWordList.clear();
    lettersInPaneList.clear();
    isWin = false;
    isGameOver = false;
    scoreLabel.setText(String.valueOf(score));
    selectRandomWord();
    selectRandomLetterInWord();
    currColumnIndex = 1;
    myAnswer.delete(0, myAnswer.length());
    myAnswer.append("0".repeat(currentWord.length()));
    for (int i = 0; i < currentWord.length(); i++) {
      lettersInPaneList.add(null);
    }
  }

  public void moveRight() {
    if (currColumnIndex < currentWord.length() && !isGameOver) {
      {
        currColumnIndex++;
      currentLetter.moveRight();
      }
    }
  }

  public void moveLeft() {
    if (currColumnIndex > 1 && !isGameOver) {
      currColumnIndex--;
      currentLetter.moveLeft();
    }
  }

  public void moveDown() {
    currentLetter.moveDown();
  }

  public double getLayoutXOfPane(int posIndex) {
    switch (posIndex) {
      case 1 -> {
        return pane1.getLayoutX();
      }
      case 2 -> {
        return pane2.getLayoutX();
      }
      case 3 -> {
        return pane3.getLayoutX();
      }
      case 4 -> {
        return pane4.getLayoutX();
      }
      case 5 -> {
        return pane5.getLayoutX();
      }
      case 6 -> {
        return pane6.getLayoutX();
      }
      case 7 -> {
        return pane7.getLayoutX();
      }
      case 8 -> {
        return pane8.getLayoutX();
      }
      default -> {}
    }
    return 0;
  }

  @FXML
  private void onPlayAgainClick() {
    score = 0;
    isGameOver = false;
    loadNewLevel();
    timeline.play();
  }

  @Override
  public void checkGameOver() {
    if (!isEmptyPane.get(currColumnIndex - 1)) {
      isGameOver = true;
      System.out.println("trung nhau");
    }
    checkAnswer();
  }

  @Override
  public void checkAnswer() {
    StringBuilder currWord = new StringBuilder(currentWord);
    if (letterInWordList.isEmpty())
      if (myAnswer.compareTo(currWord) == 0) {
        isWin = true;
        score++;
        System.out.println("correct");
      } else {
        isGameOver = true;
        System.out.println("sai tu");
      }
  }
}
