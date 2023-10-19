package com.example.englishapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class DictionaryApplicationController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void goToSearchScreen(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SideBar.fxml")));

        scene = new Scene(root,800 , 500);
        scene.setFill(Color.TRANSPARENT);

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();

    }
}