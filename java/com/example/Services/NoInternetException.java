package com.example.Services;

public class NoInternetException extends Exception {
    public NoInternetException(String message) {
        super("No Internet connection.\n Please check again");
    }
}
