package com.example.Services;

public class WordAlreadyExistsException extends DatabaseException {
    public WordAlreadyExistsException(String thisWord) {
        super(  thisWord +  " is already exists in database");
    }
}
