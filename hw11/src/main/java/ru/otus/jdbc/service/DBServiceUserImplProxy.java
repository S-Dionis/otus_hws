package ru.otus.jdbc.service;

import ru.otus.cachehw.HwCache;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;

import java.util.Optional;

public class DBServiceUserImplProxy implements DBServiceUser {

    DBServiceUser serviceUser;
    public HwCache<Long, User> cache;

    public DBServiceUserImplProxy(DBServiceUser serviceUser, HwCache<Long, User> cache) {
        this.serviceUser = serviceUser;
        this.cache = cache;
    }

    @Override
    public long saveUser(User user) {
        long id = serviceUser.saveUser(user);
        cache.put(id, new User(user));
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        User value = cache.get(id);
        if (value == null) {
            return serviceUser.getUser(id);
        } else {
            return Optional.of(value);
        }

    }
}
