package com.example.Services;

public class EmptyInPutException extends  DatabaseException{

    public EmptyInPutException(String message) {
        super("Please fill all the data");
    }
}
