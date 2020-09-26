package ru.otus.listener;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.Message;
import ru.otus.handler.ComplexProcessor;
import ru.otus.history.MessageHistoryAdder;
import ru.otus.processor.Processor;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class HistoryListenerTest {

    @Test
    void historyListenerCalled() {
        Message message = new Message.Builder().field10("cat").build();
        Processor processor1 = mock(Processor.class);
        HistoryListener historyListener = mock(HistoryListener.class);
        ComplexProcessor cp = new ComplexProcessor(List.of(processor1),ex -> {});

        when(processor1.process(eq(message))).thenReturn(message);
        cp.addListener(historyListener);

        cp.handle(message);
        cp.handle(message);

        verify(historyListener, times(2)).onUpdated(message, message);
    }

}