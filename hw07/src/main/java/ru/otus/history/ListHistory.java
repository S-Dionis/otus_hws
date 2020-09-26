package ru.otus.history;

import ru.otus.Message;

import java.util.ArrayList;
import java.util.List;

public class ListHistory implements MessageHistoryAdder {

    private final List<MessagePair> hashSet = new ArrayList<>();

    @Override
    public void save(Message oldMessage, Message newMessage) {
        hashSet.add(new MessagePair(oldMessage, newMessage));
    }

}
