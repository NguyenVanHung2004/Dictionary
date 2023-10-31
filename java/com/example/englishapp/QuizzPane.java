package com.example.englishapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class QuizzPane implements Initializable {
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

  List<QuizzModel> questionList = new ArrayList<>();
  public int currentQuestionIndex = 0;
  public int myScore = 0;
  QuizzModel currentQuestion;
  public int selectedAnswer = 0;
  Button selectedAnswerButton;
  Button correctAnswerButton;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    QuizzModel q1 = new QuizzModel(1, "Hello", "xin chao", "a", "c", "e", 1);
    QuizzModel q2 = new QuizzModel(2, "Goodbye", "hahah", "xin loi", "tambiet", "e", 3);
    QuizzModel q3 = new QuizzModel(3, "What the fuck", "hahah", "cam on ban ", "cai deo gi day", "e", 3);
    questionList.add(q1);
    questionList.add(q2);
    questionList.add(q3);
    setQuizz();
  }

  void setQuizz() {
    progressBar.setProgress(currentQuestionIndex * 0.1);
    myScoreLabel.setText("SCORE: " + myScore);
    currentQuestion = questionList.get(currentQuestionIndex);
    option1.setDisable(false);
    option2.setDisable(false);
    option3.setDisable(false);
    option4.setDisable(false);
    submit.setDisable(true);
    next.setDisable(true);


    question.setText(currentQuestion.getQuetion());
    option1.setText(currentQuestion.getOptionOne());
    option2.setText(currentQuestion.getOptionTwo());
    option3.setText(currentQuestion.getOptionThree());
    option4.setText(currentQuestion.getOptionFour());
    switch (currentQuestion.getCorrectAnswer()) {
      case 1:
        correctAnswerButton = option1;
        break;
      case 2:
        correctAnswerButton = option2;
        break;
      case 3:
        correctAnswerButton = option3;
        break;
      case 4:
        correctAnswerButton = option4;
        break;
    }
  }

  @FXML
  public void clickedBackButton() {}

  @FXML
  public void clickedOptionOne() {
    selectedAnswer = 1;
    selectedAnswerButton = option1;
    submit.setDisable(false);
    setDefaultStyle();
    option1.setStyle("-fx-background-color: #00FFFF;");
  }

  @FXML
  public void clickedOptionTwo() {
    selectedAnswer = 2;
    selectedAnswerButton = option2;
    submit.setDisable(false);
    setDefaultStyle();
    option2.setStyle("-fx-background-color: #00FFFF;");
  }

  @FXML
  public void clickedOptionThree() {
    selectedAnswer = 3;
    selectedAnswerButton = option3;
    submit.setDisable(false);
    setDefaultStyle();
    option3.setStyle("-fx-background-color: #00FFFF;");
  }

  @FXML
  public void clickedOptionFour() {
    selectedAnswer = 4;
    selectedAnswerButton = option4;
    submit.setDisable(false);
    setDefaultStyle();
    option4.setStyle("-fx-background-color: #00FFFF;");

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
    setDefaultStyle();
    setQuizz();
  }

  public boolean checkAnswer() {
    return selectedAnswer == currentQuestion.getCorrectAnswer();
  }

  public void setDefaultStyle() {
    option1.setStyle("-fx-background-color : linear-gradient(#555392, #E34A4A)");
    option2.setStyle("-fx-background-color : linear-gradient(#555392, #E34A4A)");
    option3.setStyle("-fx-background-color : linear-gradient(#555392, #E34A4A)");
    option4.setStyle("-fx-background-color : linear-gradient(#555392, #E34A4A)");
  }

  public void setWrongStyle() {
    selectedAnswerButton.setStyle("-fx-background-color: #FF0000"); // red
    correctAnswerButton.setStyle("-fx-background-color: #00FF00"); // green
  }

  public void setCorrectStyle() {
    correctAnswerButton.setStyle("-fx-background-color: #00FF00");
  }
}
