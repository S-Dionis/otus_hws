package ru.otus.messagesystem.message;

public enum MessageType {
    GET_USERS("UserData"),
    INSERT_USER("InsertUsers");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
