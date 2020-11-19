package ru.otus.jdbc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.h2.DataSourceH2;
import ru.otus.h2.Migration;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.UserDaoImpl;
import ru.otus.jdbc.mapper.*;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

class DBServiceUserImplTest {

    DBServiceUser dbServiceUser;

    @BeforeEach
    void init() {
        DataSource dataSource = new DataSourceH2();
        Migration.migrate(dataSource);

        DbExecutor<User> dbExecutor = new DbExecutorImpl<>();
        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);

        EntityClassMetaData<User> classMetaData = new EntityClassMetaDataImpl<>(User.class);
        EntitySQLMetaData sqlMetaData = new EntitySQLMetaDataImpl(classMetaData);

        JdbcMapperImpl<User> jdbcMapperImpl = new JdbcMapperImpl<>(sessionManager, dbExecutor, classMetaData, sqlMetaData);
        UserDao userDao = new UserDaoImpl(sessionManager, jdbcMapperImpl);

        dbServiceUser = new DBServiceUserImpl(userDao);
    }

    @Test
    void saveUser() {
        User user = new User(0,"Мяукич", 4);
        long l = dbServiceUser.saveUser(user);
        assertEquals(l, 1);
    }

    @Test
    void getUser() {
        long id = dbServiceUser.saveUser(new User(0,"Мяукич", 4));

        User expected = new User(id, "Мяукич", 4);
        User actual = dbServiceUser.getUser(id).get();

        assertEquals(expected, actual);
    }
}