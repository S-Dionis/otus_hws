package ru.otus.listener;

import ru.otus.Message;
import ru.otus.history.ListHistory;
import ru.otus.history.MessageHistoryAdder;

public class HistoryListener implements Listener {

    private final MessageHistoryAdder mha;

    public HistoryListener(MessageHistoryAdder mha) {
        this.mha = mha;
    }

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        mha.save(oldMsg, newMsg);
    }
}
