package ru.otus.db.handler;

import org.springframework.stereotype.Component;
import ru.otus.db.core.model.User;
import ru.otus.db.core.service.DBServiceUser;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.Optional;

@Component
public class InsertUserHandler implements RequestHandler<User> {

    private final DBServiceUser dbServiceUser;

    public InsertUserHandler(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        User user = MessageHelper.getPayload(msg);

        long id = dbServiceUser.saveUser(user);
        user.setId(id);

        return Optional.of(MessageBuilder.buildReplyMessage(msg, user));
    }

}
