package ru.otus;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.dao.UserDaoHibernate;
import ru.otus.sessionmanager.SessionManagerHibernate;

public class DBConfig {

    private static final String URL = "jdbc:h2:mem:testDB;DB_CLOSE_DELAY=-1";

    @Bean
    public org.hibernate.cfg.Configuration hibernateConfiguration() {
        return new org.hibernate.cfg.Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", URL)
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "create")
                .setProperty("hibernate.generate_statistics", "true");
    }

    @Bean
    public StandardServiceRegistry serviceRegistry(org.hibernate.cfg.Configuration hibernateConfiguration) {
        return new StandardServiceRegistryBuilder()
                .applySettings(hibernateConfiguration.getProperties()).build();
    }

    @Bean
    public Metadata metadata(StandardServiceRegistry serviceRegistry) {
        return new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(PhoneDataSet.class)
                .addAnnotatedClass(AddressDataSet.class)
                .getMetadataBuilder()
                .build();
    }

    @Bean
    public SessionManagerHibernate sessionManagerHibernate(Metadata metadata) {
        return new SessionManagerHibernate(metadata.getSessionFactoryBuilder().build());
    }

    @Bean
    public UserDao userDao(SessionManagerHibernate sessionManagerHibernate) {
        return new UserDaoHibernate(sessionManagerHibernate);
    }
}
