package com.example.englishapp;

public class QuizzModel {
    public int id;
    public String quetion;
    public String optionOne;
    public String optionTwo;
    public String optionThree;
    public String optionFour;
    public int correctAnswer;
    public static int TOTAL_QUESTION = 10;
    public QuizzModel(int id, String quetion, String optionOne, String optionTwo, String optionThree, String optionFour, int correctAnswer) {
        this.id = id;
        this.quetion = quetion;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }

    public String getQuetion() {
        return quetion;
    }

    public String getOptionOne() {
        return optionOne;
    }

    public String getOptionTwo() {
        return optionTwo;
    }

    public String getOptionThree() {
        return optionThree;
    }

    public String getOptionFour() {
        return optionFour;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}
