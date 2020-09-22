package ru.otus.processor;

import org.junit.jupiter.api.Test;
import ru.otus.Message;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProcessorsTest {

    @Test
    void swipeFieldsTest() {
        ProcessorSwipeFields psf = new ProcessorSwipeFields();
        Message in = new Message.Builder()
                .field11("Field11")
                .field13("Field13")
                .build();

        Message result = psf.process(in);

        assertThat(new Message.Builder()
                .field11("Field13")
                .field13("Field11")
                .build())
                .isEqualTo(result);
    }

    @Test
    void throwableProcessorTest() {
        ThrowableProcessor throwableProcessor = new ThrowableProcessor(null);
        int second = LocalTime.now().getSecond();
        if ((second & 1) == 0) {
            assertThatThrownBy(() -> throwableProcessor.process(null))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Error");
        }
    }
}