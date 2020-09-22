package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.history.ListHistory;
import ru.otus.listener.HistoryListener;
import ru.otus.processor.Processor;
import ru.otus.processor.ProcessorSwipeFields;
import ru.otus.processor.ProcessorUpperField10;
import ru.otus.processor.ThrowableProcessor;

import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13
       2. Сделать процессор, который поменяет местами значения field11 и field13
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду
       4. Сделать Listener для ведения истории: старое сообщение - новое
     */

    public static void main(String[] args) {
        List<Processor> processors = List.of(new ThrowableProcessor(new ProcessorUpperField10()), new ProcessorSwipeFields());
        ComplexProcessor cp = new ComplexProcessor(processors, e -> System.out.println(e.getMessage()));
        HistoryListener historyListener = new HistoryListener(new ListHistory());
        cp.addListener(historyListener);

        Message build = new Message.Builder()
                .field1("1")
                .field2("2")
                .field3("3")
                .field4("4")
                .field5("5")
                .field6("6")
                .field7("7")
                .field8("8")
                .field9("9")
                .field10("10")
                .field11("11")
                .field12("12")
                .field13("13")
                .build();

        Message result = cp.handle(build);
        result.toBuilder();
    }
}
