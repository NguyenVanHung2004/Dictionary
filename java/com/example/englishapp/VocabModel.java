package com.example.englishapp;

public class VocabModel {
  String word;
  String Definition;

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getDefinition() {
    return Definition;
  }

  public void setDefinition(String definition) {
    Definition = definition;
  }

  public VocabModel(String word, String definition) {
    this.word = word;
    this.Definition = definition;
  }
}
