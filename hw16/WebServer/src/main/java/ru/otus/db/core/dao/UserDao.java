package ru.otus.db.core.dao;

import ru.otus.db.core.model.User;
import ru.otus.db.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    long insertUser(User user);

    List<User> findAllUsers();

    SessionManager getSessionManager();
}
