package ru.otus.jdbc.dao;

import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private final JdbcMapper<User> jdbcMapper;
    private final SessionManagerJdbc sessionManager;

    public UserDaoImpl(SessionManagerJdbc sessionManager, JdbcMapper<User> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcMapper.findById(id, User.class);
    }

    @Override
    public long insertUser(User user) {
        return jdbcMapper.insert(user);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

}
