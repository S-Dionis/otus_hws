package ru.otus.core.dao;

import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    long insertUser(User user);

    List<User> findAllUsers();

    //void updateUser(User user);
    //void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
