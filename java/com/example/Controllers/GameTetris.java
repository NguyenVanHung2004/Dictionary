package com.example.Controllers;

import com.example.Services.DatabaseConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class GameTetris implements Initializable {

  @FXML private ImageView letterImageView;

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

  @FXML private ImageView paneImage1;

  @FXML private ImageView paneImage2;

  @FXML private ImageView paneImage3;

  @FXML private ImageView paneImage4;

  @FXML private ImageView paneImage5;

  @FXML private ImageView paneImage6;

  @FXML private ImageView paneImage7;
  @FXML private ImageView paneImage8;

  @FXML private Label scoreLabel;

  @FXML private Label wordExplain;
  @FXML private Label suggestionLabel;

  List<Pane> panes = new ArrayList<>();
  List<String> wordList = new ArrayList<>();
  List<String> wordExplainList = new ArrayList<>();
  List<Character> letterList = new ArrayList<>();
  List<Character> letterInPane = new ArrayList<>();
  List<ImageView> imageViewList = new ArrayList<>();
  private List<Boolean> isEmptyPane;
  Random random = new Random();
  Timeline timeline = new Timeline();
  private int randomIndex;
  private double velocity;
  public static final double acceleration = 0.01;
  private int currentWordIndex;
  private int score;

  private int posIndex;
  private int midIndex;
  private char currentLetter;
  private String currentWord;


  private String imagePath;
  private boolean isLose;

  public void loadWordsFromFile() {

    DatabaseConnection connectNow = DatabaseConnection.getInstance();
    Connection connectDB = connectNow.getDatabaseConnection();
    String query = "SELECT word FROM entries;";
    try {
      Statement statement = connectDB.createStatement();
      ResultSet queryOutput = statement.executeQuery(query);
      while (queryOutput.next()) {
        String myWord = queryOutput.getString("word");
        wordList.add(myWord);
      }
    } catch (SQLException e) {
      Logger.getLogger(SideBarController.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  public void loadWordExplainFromFile() {
    DatabaseConnection connectNow = DatabaseConnection.getInstance();
    Connection connectDB = connectNow.getDatabaseConnection();
    String query = "SELECT definition FROM entries;";
    try {
      Statement statement = connectDB.createStatement();
      ResultSet queryOutput = statement.executeQuery(query);
      while (queryOutput.next()) {
        String myWord = queryOutput.getString("definition");
        wordExplainList.add(myWord);
      }
    } catch (SQLException e) {
      Logger.getLogger(SideBarController.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  public void selectRandomWord() {
    do {
      currentWordIndex = random.nextInt(wordList.size());
      currentWord = wordList.get(currentWordIndex);
    } while (currentWord.length() > 7);

    midIndex = (currentWord.length() + 1) / 2;
    posIndex = midIndex;
    String temp = currentWord;
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

  public void displayWordExplain() {
    String currentWordExplain = wordExplainList.get(currentWordIndex);
    wordExplain.setText(currentWordExplain);
    wordExplainList.remove(currentWordIndex);
  }

  public void displayCurrentLetter() {
    for (int i = 0; i < currentWord.length(); i++) {
      letterList.add(currentWord.charAt(i));
    }
    randomIndex = random.nextInt(letterList.size());
    currentLetter = letterList.get(randomIndex);
    imagePath =
        "G:\\Java\\OOP_BTL\\EnglishApp\\src\\main\\resources\\com\\example\\image\\"
            + Character.toLowerCase(currentLetter)
            + ".jpg";
    letterImageView.setImage(new Image(imagePath));
    letterList.remove(randomIndex);
  }

  public double getLayoutXPane(int posIndex) {
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

  public void moveLeft() {
    if (posIndex > 1 && !isLose) {
      posIndex--;

      if (letterImageView.getLayoutY() + letterImageView.getFitHeight() + velocity
          <= pane1.getLayoutY()) {
        letterImageView.setLayoutY(letterImageView.getLayoutY() + velocity * 4);
      }
    }

    letterImageView.setLayoutX(getLayoutXPane(posIndex));
  }

  public void moveRight() {
    if (posIndex < currentWord.length() && !isLose) {
      posIndex++;
      if (letterImageView.getLayoutY() + letterImageView.getFitHeight() + velocity
          <= pane1.getLayoutY()) {
        letterImageView.setLayoutY(letterImageView.getLayoutY() + velocity * 4);
      }
    }

    letterImageView.setLayoutX(getLayoutXPane(posIndex));
  }

  public void moveDown() {
    if ( !isLose )
      letterImageView.setLayoutY(letterImageView.getLayoutY() + 50);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    score = 0;
    isLose = false;
    button.requestFocus();
    button.focusTraversableProperty();
    button.addEventFilter(
        KeyEvent.KEY_PRESSED,
        event -> {
          if (event.getCode() == KeyCode.LEFT) {
            System.out.println("The 'L' key was pressed");
            moveLeft();
            event.consume();
          }
          if (event.getCode() == KeyCode.RIGHT) {
            System.out.println("The 'R' key was pressed");
            moveRight();
            event.consume();
          }
          if (event.getCode() == KeyCode.DOWN) {
            System.out.println("The 'D' key was pressed");
            moveDown();
            event.consume();
          }
        });

    scoreLabel.setText(String.valueOf(score));
    loadWordsFromFile();
    loadWordExplainFromFile();
    selectRandomWord();
    displayWordExplain();
    displayCurrentLetter();
    addPane();
    addLetterInPane();
    loadLevel();
    addImageView();
    fallingDown();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
    letterImageView.setLayoutX(getLayoutXPane(posIndex));
  }

  public void fallingDown() {
    velocity = 0;
    timeline
        .getKeyFrames()
        .add(
            new KeyFrame(
                Duration.seconds(0.017),
                event -> {
                  if (checkLetterOnPane()) {
                    letterImageView.setLayoutY(letterImageView.getLayoutY() + velocity);
                    velocity += acceleration;
                  } else {
                    if (isEmptyPane.get(posIndex - 1)) {
                      setPane();
                      letterInPane.set(posIndex - 1, currentLetter);
                      setLetterImageOnPane();
                      isEmptyPane.set(posIndex - 1, false);

                      if (createNewWord()) {
                        velocity = 0;
                        if (isCorrectWord()) {
                          correctWord();
                        } else {
                          incorrectWord();
                        }
                      }
                      posIndex = midIndex;
                      setImageView();
                      velocity = 0;
                    } else {

                      isLose = true;
                      timeline.pause();
                      System.out.println("lose");
                        loadNewLevel();
                    }

                  }
                }));
  }

  private void finishedGame() {
    timeline.pause();
    isLose = true;
    velocity = 0 ;
  }

  @FXML
  void onPlayAgainClick() {
    score = 0;

    loadNewLevel();
  }

  public void addPane() {
    panes.add(pane1);
    panes.add(pane2);
    panes.add(pane3);
    panes.add(pane4);
    panes.add(pane5);
    panes.add(pane6);
    panes.add(pane7);
    panes.add(pane8);
  }

  public void addImageView() {
    imageViewList.add(paneImage1);
    imageViewList.add(paneImage2);
    imageViewList.add(paneImage3);
    imageViewList.add(paneImage4);
    imageViewList.add(paneImage5);
    imageViewList.add(paneImage6);
    imageViewList.add(paneImage7);
    imageViewList.add(paneImage8);
  }

  public void addLetterInPane() {
    letterInPane = new ArrayList<>();
    for (int i = 0; i < currentWord.length(); i++) {
      letterInPane.add(null);
    }
  }

  public void loadLevel() {
    isEmptyPane = new ArrayList<>();
    for (Pane p : panes) {
      p.setVisible(false);
    }

    for (int i = 0; i < currentWord.length(); i++) {
      panes.get(i).setVisible(true);
      isEmptyPane.add(true);
    }
  }

  public boolean checkLetterOnPane() {
    return (letterImageView.getLayoutY() + letterImageView.getFitHeight() <= pane1.getLayoutY());
  }

  public void setLetterImageOnPane() {
    try {
      switch (posIndex) {
        case 1 -> imageViewList.get(0).setImage(new Image(imagePath));
        case 2 -> imageViewList.get(1).setImage(new Image(imagePath));
        case 3 -> imageViewList.get(2).setImage(new Image(imagePath));
        case 4 -> imageViewList.get(3).setImage(new Image(imagePath));
        case 5 -> imageViewList.get(4).setImage(new Image(imagePath));
        case 6 -> imageViewList.get(5).setImage(new Image(imagePath));
        case 7 -> imageViewList.get(6).setImage(new Image(imagePath));
        case 8 -> imageViewList.get(7).setImage(new Image(imagePath));
        default -> {}
      }
    } catch (IllegalArgumentException e) {
      System.out.println(imagePath);
    }
  }

  public void setImageView() {
    randomIndex = random.nextInt(letterList.size());
    currentLetter = letterList.get(randomIndex);
    imagePath =
        "G:\\Java\\OOP_BTL\\EnglishApp\\src\\main\\resources\\com\\example\\image\\"
            + Character.toLowerCase(currentLetter)
            + ".jpg";
    letterImageView.setImage(new Image(imagePath));
    letterList.remove(randomIndex);

    letterImageView.setLayoutX(getLayoutXPane(posIndex));
    letterImageView.setLayoutY(0);
  }

  public boolean isCorrectWord() {
    StringBuilder word = new StringBuilder();
    for (Character character : letterInPane) {
      if (character == null) return false;
      word.append(character);
    }
    return word.toString().equals(currentWord);
  }

  public boolean createNewWord() {
    for (Boolean aBoolean : isEmptyPane) {
      if (aBoolean) {
        return false;
      }
    }

    return true;
  }

  public void setVisiblePane() {
    for (int i = 0; i < currentWord.length(); i++) {
      imageViewList.get(i).setVisible(false);
    }
  }

  public void setPane() {
    imageViewList.get(posIndex - 1).setVisible(true);
  }

  public void correctWord() {
    score += 10;
    loadNewLevel();
  }

  public void loadNewLevel() {
    velocity = 0;
    isLose = false;
    System.out.println("next level");
    timeline.play();
    letterList.clear();
    letterImageView.setVisible(false);
    scoreLabel.setText(String.valueOf(score));
    selectRandomWord();
    displayWordExplain();
    for (int i = 0; i < 8; i++) {
      imageViewList.get(i).setVisible(false);
    }
    addLetterInPane();
    loadLevel();

    System.out.println(currentWord);

    for (int i = 0; i < currentWord.length(); i++) {
      letterList.add(currentWord.charAt(i));
    }

    for (Character character : letterList) {
      System.out.print(character);
    }
    letterImageView.setVisible(true);
    setImageView();
  }

  public void incorrectWord() {
    scoreLabel.setText(String.valueOf(score));
    setVisiblePane();
    selectRandomWord();
    displayWordExplain();
    addLetterInPane();
    loadLevel();
    System.out.println(currentWord);

    for (int i = 0; i < currentWord.length(); i++) {
      letterList.add(currentWord.charAt(i));
    }

    int randomIndex = random.nextInt(letterList.size());
    currentLetter = letterList.get(randomIndex);
    imagePath =
        "G:\\Java\\OOP_BTL\\EnglishApp\\src\\main\\resources\\com\\example\\image\\"
            + Character.toLowerCase(currentLetter)
            + ".jpg";
    letterImageView.setImage(new Image(imagePath));

    letterImageView.setLayoutX(getLayoutXPane(1));
    letterImageView.setLayoutY(0);
  }
}
