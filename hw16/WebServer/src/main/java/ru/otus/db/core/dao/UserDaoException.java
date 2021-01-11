package ru.otus.db.core.dao;

public class UserDaoException extends RuntimeException {

    public UserDaoException(Exception ex) {
        super(ex);
    }

}
