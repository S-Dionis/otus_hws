package ru.otus.processor;

import ru.otus.Message;

public class ProcessorSwipeFields implements Processor {

    @Override
    public Message process(Message message) {
        Message.Builder builder = message.toBuilder().field11(message.getField13()).field13(message.getField11());
        return builder.build();
    }

}
