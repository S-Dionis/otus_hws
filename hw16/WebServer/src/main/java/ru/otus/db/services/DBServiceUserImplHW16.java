package ru.otus.db.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.db.core.dao.UserDao;
import ru.otus.db.core.model.User;
import ru.otus.db.core.service.DBServiceException;
import ru.otus.db.core.service.DBServiceUser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DBServiceUserImplHW16 implements DBServiceUser {

    private final UserDao userDao;
    private static final Logger logger = LoggerFactory.getLogger(DBServiceUserImplHW16.class);

    public DBServiceUserImplHW16(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userId = userDao.insertUser(user);
                logger.info("USER WAS INSERTED WITH ID:" + userId);
                sessionManager.commitSession();
                return userId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DBServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);
                sessionManager.commitSession();
                return userOptional;
            } catch (Exception e) {
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var users = userDao.findAllUsers();
                sessionManager.commitSession();
                return users;
            } catch (Exception e) {
                sessionManager.rollbackSession();
            }
            return Collections.emptyList();
        }
    }

}