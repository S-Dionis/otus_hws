package ru.otus.history;

import ru.otus.Message;

import java.util.LinkedHashSet;

public class ListHistory implements MessageHistoryAdder {

    private final LinkedHashSet<MessagePair> hashSet = new LinkedHashSet<>();

    @Override
    public void save(Message oldMessage, Message newMessage) {
        hashSet.add(new MessagePair(oldMessage, newMessage));
    }

}
