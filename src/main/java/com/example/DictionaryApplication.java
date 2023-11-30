package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DictionaryApplication extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/main_menu.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 840, 500);
    stage.initStyle(StageStyle.UNDECORATED);
    stage.initStyle(StageStyle.TRANSPARENT);
    scene.setFill(Color.TRANSPARENT);
    stage.setTitle("Hello!");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
