package ru.dio.testframework;

public class BooleanAssertions {

    public static void assertEquals(boolean expected, boolean actual) {
        if (expected != actual) {
            throw new AssertionError();
        }
    }

}
