package ru.otus.history;

import ru.otus.Message;

public class MessagePair {

    private final Message oldMessage;
    private final Message newMessage;

    public MessagePair(Message oldMessage, Message newMessage) {
        this.oldMessage = oldMessage;
        this.newMessage = newMessage;
    }

    public Message getOldMessage() {
        return oldMessage;
    }

    public Message getNewMessage() {
        return newMessage;
    }
}
