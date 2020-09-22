package ru.otus.history;

import ru.otus.Message;

public interface MessageHistoryAdder {

    void save(Message newMessage, Message oldMessage);

}
