package ru.otus.db.handler;

import org.springframework.stereotype.Component;
import ru.otus.db.core.model.ListOfUsers;
import ru.otus.db.core.model.User;
import ru.otus.db.core.service.DBServiceUser;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;

import java.util.List;
import java.util.Optional;

@Component
public class GetAllUsersHandler implements RequestHandler<ListOfUsers> {

    private final DBServiceUser dbServiceUser;

    public GetAllUsersHandler(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        ListOfUsers listOfUsers = new ListOfUsers(dbServiceUser.getAllUsers());
        Message message = MessageBuilder.buildReplyMessage(msg, listOfUsers);
        return Optional.of(message);
    }

}
