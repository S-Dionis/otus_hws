package ru.otus.processor;

import ru.otus.Message;

import java.time.LocalTime;

public class ThrowableProcessor implements Processor {

    private final Processor processor;

    public ThrowableProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public Message process(Message message) {
        int second = LocalTime.now().getSecond();
        if ((second & 1) == 0) {
            throw new RuntimeException("Error");
        }
        return processor.process(message);
    }

}
