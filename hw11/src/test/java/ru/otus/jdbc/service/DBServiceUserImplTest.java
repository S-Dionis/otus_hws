package ru.otus.jdbc.service;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.WeakCache;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.jdbc.dao.UserDaoHibernate;
import ru.otus.jdbc.sessionmanager.SessionManagerHibernate;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DBServiceUserImplTest {

    private DBServiceUser dbServiceUser;
    private DBServiceUser dbServiceUserCached;
    private final Logger logger = LoggerFactory.getLogger(DBServiceUserImplTest.class);
    private final HwCache<String, User> cache = new WeakCache<>();

    @BeforeEach
    void init() throws SQLException {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

        var serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        var metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(PhoneDataSet.class)
                .addAnnotatedClass(AddressDataSet.class)
                .getMetadataBuilder()
                .build();

        var sessionFactory = metadata.getSessionFactoryBuilder().build();

        SessionManagerHibernate smh = new SessionManagerHibernate(sessionFactory);

        UserDao userDao = new UserDaoHibernate(smh);

        dbServiceUser = new DBServiceUserImpl(userDao);
        dbServiceUserCached = new DBServiceUserImplProxy(dbServiceUser, cache);
    }

    @Test
    void testThatGetSavedUserFromCacheIsFasterThenFromDB() {
        long dbTime = getOperationTime(dbServiceUser);
        long cacheTime = getOperationTime(dbServiceUserCached);

        assertTrue(cacheTime < dbTime);
    }

    private long getOperationTime(DBServiceUser dbServiceUser) {
        long start = System.currentTimeMillis();

        User user = createUser();
        long id = dbServiceUser.saveUser(user);
        var saved = dbServiceUser.getUser(id).get();

        long end = System.currentTimeMillis();
        return end - start;
    }

    @Test
    void assertThatListenersAreDeleteWhenDoesNotNeed() throws InterruptedException {

        class Variable {
            public int a = 0;
        }

        Variable variable = new Variable();

        HwListener<String, User> listener = new HwListener<String, User>() {
            @Override
            public void notify(String key, User value, String action) {
                variable.a++;
            }
        };

        cache.addListener(listener);

        dbServiceUserCached.saveUser(createUser());
        dbServiceUserCached.saveUser(createUser());

        listener = null;
        System.gc();
        Thread.sleep(199);
        dbServiceUserCached.saveUser(createUser());
        //assert that listener was called only 2 times
        assertEquals(variable.a, 2);
    }

    private User createUser() {
        User user = new User();
        List<PhoneDataSet> pds = List.of(new PhoneDataSet(null, "21500", user));
        AddressDataSet address = new AddressDataSet(null, "Orion", user);

        user.setAge(20000000);
        user.setName("Bellatrix");
        user.setPhones(pds);
        user.setAddress(address);
        return user;
    }

}