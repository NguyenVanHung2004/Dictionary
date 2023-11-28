package com.example.Models;

import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.ArrayList;

class TrieNode {
    public HashMap<Character, TrieNode> children = new HashMap<>();
    public boolean endOfWord;
}

public class Trie {
    private static TrieNode root = new TrieNode();
    private  Trie() {
        root = new TrieNode();
    }

    // Thêm từ vào Trie
    public static void insert(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode node = current.children.get(ch);
            if (node == null) {
                node = new TrieNode();
                current.children.put(ch, node);
            }
            current = node;
        }
        current.endOfWord = true;
    }


    public static ArrayList<String> getWordsFromNode(String prefix, TrieNode node, ArrayList<String> words) {
        if (node.endOfWord) {
            words.add(prefix);
        }
        for (char ch : node.children.keySet()) {
            getWordsFromNode(prefix + ch, node.children.get(ch), words);
        }
        return words;
    }
    public static ArrayList<String> getWordsWithPrefix(String prefix) {
        TrieNode current = root;
        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            TrieNode node = current.children.get(ch);
            if (node == null) {
                return new ArrayList<String>();  // Trả về danh sách rỗng nếu tiền tố không tồn tại
            }
            current = node;
        }
        return getWordsFromNode(prefix, current, new ArrayList<String>());
    }
}