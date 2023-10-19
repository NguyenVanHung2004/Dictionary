package com.example.englishapp;

import animatefx.animation.Bounce;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.animation.*;
import java.net.URL;
import java.util.ResourceBundle;
public class LoadingAnimationController implements Initializable {
    @FXML
    private Circle cir1, cir2, cir3, cir4;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Bounce(cir1).setCycleCount(10).setDelay(Duration.millis(100)).play();
        new Bounce(cir2).setCycleCount(10).setDelay(Duration.millis(200)).play();
        new Bounce(cir3).setCycleCount(10).setDelay(Duration.millis(300)).play();
        new Bounce(cir4).setCycleCount(10).setDelay(Duration.millis(400)).play();
    }
}