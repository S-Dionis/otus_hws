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
import ru.otus.cachehw.SoftCache;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.jdbc.dao.UserDaoHibernate;
import ru.otus.jdbc.sessionmanager.SessionManagerHibernate;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DBServiceUserImplTest {

    DBServiceUser dbServiceUser;
    DBServiceUser dbServiceUserCached;
    Logger logger = LoggerFactory.getLogger(DBServiceUserImplTest.class);
    HwCache<Long, User> cache = new SoftCache<>();

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

//        jdbc:h2:tcp://192.168.0.4:52675/mem:testDB
//        Server tcpServer = Server.createTcpServer("-tcpPort", "52675");
//        tcpServer.start();
//        System.out.println(tcpServer.getURL());
    }

    @Test
    void testThatGetSavedUserFromCacheIsFasterThenFromDB() {
        long dbTime = 0;
        long cacheTime = 0;
        {
            User user = createUser();
            long id = dbServiceUser.saveUser(user);
            long start = System.currentTimeMillis();
            var saved = dbServiceUser.getUser(id).get();
            long end = System.currentTimeMillis();

            dbTime = end - start;
        }

        {
            User user = createUser();
            long id = dbServiceUserCached.saveUser(user);
            long start = System.currentTimeMillis();
            var saved = dbServiceUserCached.getUser(id).get();
            long end = System.currentTimeMillis();

            cacheTime = end - start;
        }

        assertTrue(cacheTime < dbTime);
    }

    @Test
    void assertThatListenersAreDeleteWhenDoesNotNeed() throws InterruptedException {

        class Variable {
            public int a = 0;
        }

        Variable variable = new Variable();

        HwListener<Long, User> listener = new HwListener<Long, User>() {
            @Override
            public void notify(Long key, User value, String action) {
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
        //assert that listener was call only 2 times
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

    private User createRandomUser(long id) {
        Random random = new Random();
        User user = new User();

        List<PhoneDataSet> pds = List.of(
                new PhoneDataSet(id,
                        "" + random.nextInt(Integer.MAX_VALUE),
                        user
                )
        );
        AddressDataSet address = new AddressDataSet(id, "" + random.nextInt(Integer.MAX_VALUE), user);

        user.setId(id);
        user.setAge(random.nextInt(Integer.MAX_VALUE));
        user.setName(" " + random.nextInt(Integer.MAX_VALUE));

        user.setPhones(pds);
        user.setAddress(address);
        return user;
    }

}