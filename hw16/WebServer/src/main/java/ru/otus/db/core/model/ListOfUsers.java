package ru.otus.db.core.model;

import ru.otus.messagesystem.client.ResultDataType;

import java.util.List;

public class ListOfUsers extends ResultDataType {

    private final List<User> users;

    public ListOfUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

}
