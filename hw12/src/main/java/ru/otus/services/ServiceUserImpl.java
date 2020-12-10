package ru.otus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DBServiceException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ServiceUserImpl implements DBServiceUser {

    private final UserDao userDao;
    private static final Logger logger = LoggerFactory.getLogger(ServiceUserImpl.class);

    public ServiceUserImpl(UserDao userDao) {
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