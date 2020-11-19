package ru.otus.core.dao;

import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AccountDao {

    long insertAccount(Account account);
    Optional<Account> getAccount(long id);
    SessionManager getSessionManager();
}
