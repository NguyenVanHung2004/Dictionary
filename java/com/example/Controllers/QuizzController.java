package com.example.Controllers;

import com.example.Models.QuizzModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class QuizzController implements Initializable {
  @FXML Label question;
  @FXML Button option1;
  @FXML Button option2;
  @FXML Button option3;
  @FXML Button option4;
  @FXML Button submit;
  @FXML Button next;
  @FXML public Button back;
  @FXML public ProgressBar progressBar;
  @FXML public Label myScoreLabel;
  @FXML Label smallLabel;
  @FXML public StackPane contentArea;
  List<QuizzModel> questionList = new ArrayList<>();
  List<QuizzModel> randomQuestion = new ArrayList<>();
  public int currentQuestionIndex = 0;
  public int myScore = 0;
  QuizzModel currentQuestion;
  public int selectedAnswer = 0;
  Button selectedAnswerButton;
  Button correctAnswerButton;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      getDataFromFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  void setQuizz() {
    setDefaultStyle();
    progressBar.setProgress(currentQuestionIndex * 0.1);
    myScoreLabel.setText("SCORE: " + myScore);
    smallLabel.setText(currentQuestionIndex + " / " + "10");
    currentQuestion = randomQuestion.get(currentQuestionIndex);
    option1.setDisable(false);
    option2.setDisable(false);
    option3.setDisable(false);
    option4.setDisable(false);
    submit.setDisable(true);
    next.setDisable(true);
    setDefaultStyle();
    question.setText(currentQuestion.getQuetion());
    option1.setText(currentQuestion.getOptionOne());
    option2.setText(currentQuestion.getOptionTwo());
    option3.setText(currentQuestion.getOptionThree());
    option4.setText(currentQuestion.getOptionFour());
      switch (currentQuestion.getCorrectAnswer()) {
          case 1 -> correctAnswerButton = option1;
          case 2 -> correctAnswerButton = option2;
          case 3 -> correctAnswerButton = option3;
          case 4 -> correctAnswerButton = option4;
      }
  }

  @FXML
  public void clickedBackButton() {
    GameController.closeDialog();
  }

  @FXML
  public void clickedOptionOne() {
    selectedAnswer = 1;
    selectedAnswerButton = option1;
    submit.setDisable(false);
    setDefaultStyle();
    // option1.setStyle("-fx-background-color: #00FFFF;");
    option1.getStyleClass().clear();
    option1.getStyleClass().add("selected-style");
  }

  @FXML
  public void clickedOptionTwo() {
    selectedAnswer = 2;
    selectedAnswerButton = option2;
    submit.setDisable(false);
    setDefaultStyle();
    option2.getStyleClass().clear();
    option2.getStyleClass().add("selected-style");
    // option2.setStyle("-fx-background-color: #00FFFF;");
  }

  @FXML
  public void clickedOptionThree() {
    selectedAnswer = 3;
    selectedAnswerButton = option3;
    submit.setDisable(false);
    setDefaultStyle();
    option3.getStyleClass().clear();
    option3.getStyleClass().add("selected-style");
  }

  @FXML
  public void clickedOptionFour() {
    selectedAnswer = 4;
    selectedAnswerButton = option4;
    submit.setDisable(false);
    setDefaultStyle();
    option4.getStyleClass().clear();
    option4.getStyleClass().add("selected-style");
  }

  @FXML
  public void clickedSubmitButton() {
    option1.setDisable(true);
    option2.setDisable(true);
    option3.setDisable(true);
    option4.setDisable(true);
    next.setDisable(false);
    submit.setDisable(true);
    if (checkAnswer()) {
      myScore++;
      setCorrectStyle();
    } else {
      setWrongStyle();
    }
  }

  @FXML
  public void clickedNextButton() {
    currentQuestionIndex++;
    if (currentQuestionIndex > 10) {
      System.out.println("Finished");
      resultDialog();
    } else {
      setQuizz();
    }
  }

  public boolean checkAnswer() {
    return selectedAnswer == currentQuestion.getCorrectAnswer();
  }

  public void setDefaultStyle() {
    option1.getStyleClass().clear();
    option1.getStyleClass().add("answer-button");
    option2.getStyleClass().clear();
    option2.getStyleClass().add("answer-button");
    option3.getStyleClass().clear();
    option3.getStyleClass().add("answer-button");
    option4.getStyleClass().clear();
    option4.getStyleClass().add("answer-button");
  }

  public void setWrongStyle() {
    selectedAnswerButton.getStyleClass().clear();
    selectedAnswerButton.getStyleClass().add("wrong-style");
    correctAnswerButton.getStyleClass().clear();
    correctAnswerButton.getStyleClass().add("correct-style");
  }

  public void setCorrectStyle() {
    correctAnswerButton.getStyleClass().clear();
    correctAnswerButton.getStyleClass().add("correct-style");
  }

  public void getDataFromFile() throws IOException {
    Pane animationPane =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getResource("/com/example/view/loading_animation.fxml")));
    JFXDialog jfxDialog =
        new JFXDialog(contentArea, animationPane, JFXDialog.DialogTransition.LEFT);
    jfxDialog.show();
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            File quizDataFile =
                new File(
                    "G:\\Java\\OOP_BTL\\EnglishApp\\src\\main\\resources\\com\\example\\text\\QuizQuestion.txt");
            Scanner sc = new Scanner(quizDataFile);
            int id = 0;
            while (sc.hasNextLine()) {
              QuizzModel newQuiz =
                  new QuizzModel(
                      id,
                      sc.nextLine(),
                      sc.nextLine(),
                      sc.nextLine(),
                      sc.nextLine(),
                      sc.nextLine(),
                      Integer.parseInt(sc.nextLine().replace(" ", "")));
              questionList.add(newQuiz);
              id++;
            }
            return null;
          }

          protected void succeeded() {
            jfxDialog.close();
            Collections.shuffle(questionList);
            randomQuestion = questionList.subList(0, 11);
            setQuizz();
          }
        };
    new Thread(task).start();
  }

  public void resultDialog() {
    JFXDialogLayout content = new JFXDialogLayout();
    content.setHeading(new Text("SCORE"));
    content.setBody(new Text("Your score is " + myScore + "/10" + "\n"));
    JFXButton saveButton = new JFXButton("Save");
    saveButton.setOnAction(
            actionEvent -> GameController.closeDialog());
    content.setActions(saveButton);
    JFXDialog jfxDialog = new JFXDialog(contentArea, content, JFXDialog.DialogTransition.CENTER);
    jfxDialog.show();
  }
}
