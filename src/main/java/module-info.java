 module com.example.englishapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires json.parse;
   requires org.json;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires mysql.connector.j;
     requires json.simple;
     opens com.example.englishapp to javafx.fxml;
    exports com.example.englishapp;
}