 module com.example.englishapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires json.parse;
   requires org.json;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
     requires jlfgr;
     requires freetts.jsapi10;
     requires freetts;
    requires jfoenix;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires mysql.connector.j;
     requires json.simple;
     requires gtranslateapi;
     requires jsapi;
     requires java.net.http;
     requires java.desktop;
     requires AnimateFX;
     opens com.example.Controllers to javafx.fxml;
     opens com.example.Models to javafx.base;
     opens com.example to javafx.fxml;
     exports com.example.Controllers;
     exports com.example;
     exports com.example.Services;
  exports com.example.Models;

}