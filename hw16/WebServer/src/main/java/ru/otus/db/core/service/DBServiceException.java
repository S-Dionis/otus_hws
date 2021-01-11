package ru.otus.db.core.service;

public class DBServiceException extends RuntimeException {
    public DBServiceException(Exception e) {
        super(e);
    }
}
