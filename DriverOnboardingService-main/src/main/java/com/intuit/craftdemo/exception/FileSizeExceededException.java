package com.intuit.craftdemo.exception;

public class FileSizeExceededException extends  RuntimeException{
    public FileSizeExceededException(String message) {
        super(message);
    }
}
