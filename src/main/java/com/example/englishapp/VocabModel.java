package com.example.englishapp;

public class VocabModel {
  String word;
  //String wordType;
  String Definition;

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

 // public String getWordType() {
  //  return wordType;
  //}

  //public void setWordType(String wordType) {
    //this.wordType = wordType;
  //

  public String getDefinition() {
    return Definition;
  }

  public void setDefinition(String definition) {
    Definition = definition;
  }

  public VocabModel(String word, String definition) {
    this.word = word;
    //this.wordType = wordType;
    this.Definition = definition;
  }
}
