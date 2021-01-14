package ru.otus.db.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.db.core.dao.UserDao;
import ru.otus.db.core.dao.UserDaoException;
import ru.otus.db.core.model.User;
import ru.otus.db.core.sessionmanager.SessionManager;
import ru.otus.db.sessionmanager.SessionManagerHibernate;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoHibernate implements UserDao {

    private final Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);
    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findById(long id) {
        User user = null;
        try {
            user = getSession().find(User.class, id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(user);
    }

    @Override
    public long insertUser(User user) {
        try {
            Session session = getSession();
            session.persist(user);
            session.flush();
            return user.getId();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new UserDaoException(ex);
        }
    }

    @Override
    public List<User> findAllUsers() {
        try {
            Query<User> from_user = getSession()
                    .createQuery("From User", User.class);
            return from_user.list();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new UserDaoException(ex);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Session getSession() {
        return sessionManager.getCurrentSession().getHibernateSession();
    }
}
