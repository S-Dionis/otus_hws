package ru.otus.jdbc.service;

import ru.otus.cachehw.HwCache;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;

import java.util.Optional;

public class DBServiceUserImplProxy implements DBServiceUser {

    DBServiceUser serviceUser;
    public HwCache<String, User> cache;

    public DBServiceUserImplProxy(DBServiceUser serviceUser, HwCache<String, User> cache) {
        this.serviceUser = serviceUser;
        this.cache = cache;
    }

    @Override
    public long saveUser(User user) {
        Long id = serviceUser.saveUser(user);
        cache.put(id.toString(), new User(user));
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        return Optional
                .ofNullable(cache.get(Long.toString(id)))
                .or(() -> serviceUser.getUser(id));
    }

}
