package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.dao.UserDaoHibernate;
import ru.otus.sessionmanager.SessionManagerHibernate;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.*;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
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
        DBServiceUser serviceUser = new ServiceUserImpl(userDao);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        AdminAuthService authService = new DummyAdminAuthServiceImpl();

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, serviceUser, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }

}
