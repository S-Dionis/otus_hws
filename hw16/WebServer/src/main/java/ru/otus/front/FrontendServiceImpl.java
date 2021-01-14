package ru.otus.front;

import ru.otus.db.core.model.ListOfUsers;
import ru.otus.db.core.model.User;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

public class FrontendServiceImpl implements FrontendService {

    private final MsClient msClient;
    private final String dbServiceClientName;

    public FrontendServiceImpl(MsClient msClient, String dbServiceClientName) {
        this.msClient = msClient;
        this.dbServiceClientName = dbServiceClientName;
    }

    @Override
    public void getUsers(MessageCallback<ListOfUsers> dataConsumer) {
        Message message = msClient.produceMessage(dbServiceClientName, null, MessageType.GET_USERS, dataConsumer);
        msClient.sendMessage(message);
    }

    @Override
    public void insertUser(User user, MessageCallback<User> dataConsumer) {
        Message message = msClient.produceMessage(dbServiceClientName, user, MessageType.INSERT_USER, dataConsumer);
        msClient.sendMessage(message);
    }

}
