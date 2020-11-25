package ru.otus.jdbc.service;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.jdbc.dao.UserDaoHibernate;
import ru.otus.jdbc.sessionmanager.SessionManagerHibernate;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DBServiceUserImplTest {

    DBServiceUserImpl dbServiceUser;

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

//        jdbc:h2:tcp://192.168.0.4:52675/mem:testDB
//        Server tcpServer = Server.createTcpServer("-tcpPort", "52675");
//        tcpServer.start();
//        System.out.println(tcpServer.getURL());
    }

    @Test
    void saveUser() {

        User user = new User();
        List<PhoneDataSet> pds = List.of(new PhoneDataSet(null, "21500", user));
        AddressDataSet address = new AddressDataSet(null, "Orion", user);

        user.setAge(20000000);
        user.setName("Bellatrix");
        user.setPhones(pds);
        user.setAddress(address);

        long id = dbServiceUser.saveUser(user);
        assertTrue(id > 0);

    }

    @Test
    void getUser() {

        User expected = new User();
        List<PhoneDataSet> pds = List.of(new PhoneDataSet(null, "21500", expected));
        AddressDataSet address = new AddressDataSet(null, "Orion", expected);

        expected.setAge(20000000);
        expected.setName("Bellatrix");
        expected.setPhones(pds);
        expected.setAddress(address);

        long id = dbServiceUser.saveUser(expected);
        User actual = dbServiceUser.getUser(id).get();

        assertEquals(expected, actual);
    }

}