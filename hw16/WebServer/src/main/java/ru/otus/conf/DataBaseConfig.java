package ru.otus.conf;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.db.core.dao.UserDao;
import ru.otus.db.core.model.AddressDataSet;
import ru.otus.db.core.model.PhoneDataSet;
import ru.otus.db.core.model.User;
import ru.otus.db.dao.UserDaoHibernate;
import ru.otus.db.sessionmanager.SessionManagerHibernate;

@Configuration
public class DataBaseConfig {

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
    public SessionFactory sessionFactory(Metadata metadata) {
        return metadata.getSessionFactoryBuilder().build();
    }

    @Bean
    public UserDao userDao(SessionManagerHibernate sessionManagerHibernate) {
        return new UserDaoHibernate(sessionManagerHibernate);
    }

}
