package ru.otus.front;

import ru.otus.db.core.model.ListOfUsers;
import ru.otus.db.core.model.User;
import ru.otus.messagesystem.client.MessageCallback;

public interface FrontendService {

    void getUsers(MessageCallback<ListOfUsers> dataConsumer);
    void insertUser(User user, MessageCallback<User> dataConsumer);
}
