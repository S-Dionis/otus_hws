package ru.otus.jdbc.service;

import ru.otus.core.dao.AccountDao;
import ru.otus.core.model.Account;
import ru.otus.core.service.DBServiceAccount;
import ru.otus.core.service.DBServiceException;

import java.util.Optional;

public class DBServiceAccountImpl implements DBServiceAccount {

    private final AccountDao accountDao;

    public DBServiceAccountImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public long saveAccount(Account account) {
        try(var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userId = accountDao.insertAccount(account);
                sessionManager.commitSession();
                return userId;
            } catch (Exception ex) {
                sessionManager.rollbackSession();
                throw new DBServiceException(ex);
            }
        }
    }

    @Override
    public Optional<Account> getAccount(long id) {
        try (var sessionManager = accountDao.getSessionManager()) {
            try {
                sessionManager.beginSession();
                Optional<Account> account = accountDao.getAccount(id);
                sessionManager.commitSession();
                return account;
            } catch (Exception ex) {
                sessionManager.rollbackSession();
                throw new DBServiceException(ex);
            }
        }

    }

}
