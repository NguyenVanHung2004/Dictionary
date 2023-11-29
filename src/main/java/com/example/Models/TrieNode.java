package com.example.Models;

import java.util.HashMap;

class TrieNode {
  public HashMap<Character, TrieNode> children = new HashMap<>();
  public boolean endOfWord;
}
