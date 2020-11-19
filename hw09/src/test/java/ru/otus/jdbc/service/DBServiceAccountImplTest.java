package ru.otus.jdbc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.model.Account;
import ru.otus.core.service.DBServiceAccount;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.h2.DataSourceH2;
import ru.otus.h2.Migration;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.AccountDaoImpl;
import ru.otus.jdbc.mapper.*;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DBServiceAccountImplTest {

    DBServiceAccount dbServiceAccount;

    @BeforeEach
    void init() {
        DataSource dataSource = new DataSourceH2();
        Migration.migrate(dataSource);

        DbExecutor<Account> dbExecutor = new DbExecutorImpl<>();
        EntityClassMetaData<Account> classMetaData = new EntityClassMetaDataImpl<>(Account.class);
        EntitySQLMetaData sqlMetaData = new EntitySQLMetaDataImpl(classMetaData);
        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        JdbcMapper<Account> mapper = new JdbcMapperImpl<>(sessionManager, dbExecutor, classMetaData, sqlMetaData);
        AccountDao accountDao = new AccountDaoImpl(sessionManager, mapper);

        dbServiceAccount = new DBServiceAccountImpl(accountDao);
    }

    @Test
    void saveAccount() {
        Account account = new Account(0, "cat", new BigDecimal(24));
        long id = dbServiceAccount.saveAccount(account);

        assertEquals(1, id);
    }

    @Test
    void getAccount() {
        long id = dbServiceAccount.saveAccount(new Account(0, "cat", new BigDecimal(24)));
        Account expected = new Account(id, "cat", new BigDecimal(24));
        Account actual  = dbServiceAccount.getAccount(id).get();

        assertEquals(expected, actual);
    }

}