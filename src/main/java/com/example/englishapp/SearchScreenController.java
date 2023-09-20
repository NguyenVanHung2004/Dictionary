package com.example.englishapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchScreenController implements Initializable {
  @FXML private TableView<VocabModel> vocabTableView;
  @FXML private TableColumn<VocabModel, String> wordTableColumn;
  @FXML private TableColumn<VocabModel, String> wordTypeTableColumn;
  @FXML private TableColumn<VocabModel, String> definitionTableColumn;
  @FXML private TextField keyWordField;
  ObservableList<VocabModel> vocabModelObservableList = FXCollections.observableArrayList();

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    DatabaseConnection connectNow = new DatabaseConnection();
    Connection connectDB = connectNow.getDatabaseConnection();

    String query = "SELECT word, wordtype,definition FROM entries;";

    try {
      Statement statement = connectDB.createStatement();
      ResultSet queryOutput = statement.executeQuery(query);
      while (queryOutput.next()) {
        String myWord = queryOutput.getString("word");
        String myWordType = queryOutput.getString("wordType");
        String myDefinition = queryOutput.getString("definition");
        vocabModelObservableList.add(new VocabModel(myWord, myWordType, myDefinition));
      }
      wordTableColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
      wordTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("wordType"));
      definitionTableColumn.setCellValueFactory(new PropertyValueFactory<>("definition"));

      vocabTableView.setItems(vocabModelObservableList);

      FilteredList<VocabModel> filteredData = new FilteredList<>(vocabModelObservableList, b->true);
      keyWordField.textProperty().addListener( (observable, oldValue, newValue) ->{
        filteredData.setPredicate(vocabModel -> {
          if ( newValue.isEmpty() || newValue.isBlank() || newValue == null)
            return true;
          String searchKeyWord = newValue.toLowerCase();
          if ( vocabModel.getWord().toLowerCase().indexOf(searchKeyWord) > -1)
              return true;
          return false;
        }) ;
      } );
      SortedList<VocabModel> sortedData = new SortedList<>(filteredData);
      sortedData.comparatorProperty().bind(vocabTableView.comparatorProperty());
      vocabTableView.setItems(sortedData);

    } catch (SQLException e) {
      Logger.getLogger(SearchScreenController.class.getName()).log(Level.SEVERE,null,e);
    }
  }
}
