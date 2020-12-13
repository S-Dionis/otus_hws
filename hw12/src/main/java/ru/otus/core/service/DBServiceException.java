package ru.otus.core.service;

public class DBServiceException extends RuntimeException {
    public DBServiceException(Exception e) {
        super(e);
    }
}
