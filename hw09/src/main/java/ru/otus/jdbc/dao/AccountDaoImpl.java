package ru.otus.jdbc.dao;

import ru.otus.core.dao.AccountDao;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class AccountDaoImpl implements AccountDao {

    private final SessionManagerJdbc sessionManagerJdbc;
    private final JdbcMapper<Account> jdbcMapper;

    public AccountDaoImpl(SessionManagerJdbc sessionManagerJdbc, JdbcMapper<Account> jdbcMapper) {
        this.sessionManagerJdbc = sessionManagerJdbc;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public long insertAccount(Account account) {
        return jdbcMapper.insert(account);
    }

    @Override
    public Optional<Account> getAccount(long id) {
        return jdbcMapper.findById(id, Account.class);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManagerJdbc;
    }

}