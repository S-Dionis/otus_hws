package ru.otus.services;

public class DummyAdminAuthServiceImpl implements AdminAuthService {

    @Override
    public boolean authenticate(String login, String password) {
        return login.equals("admin") && password.equals("admin");
    }
}
