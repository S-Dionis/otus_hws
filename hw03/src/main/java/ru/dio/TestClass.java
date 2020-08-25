package ru.dio;

import ru.dio.testframework.After;
import ru.dio.testframework.Before;
import ru.dio.testframework.BooleanAssertions;
import ru.dio.testframework.Test;

import java.util.Random;

public class TestClass {

    private static int variable = 0;

    @Before
    void setUp() {
        variable++;
    }

    @Test
    void test() {
        BooleanAssertions.assertEquals(new Random().nextInt(3) == 0, true);
    }

    @Test
    void alwaysPass() {
        BooleanAssertions.assertEquals(true, true);
    }

    @Test
    void alwaysFail() {
        BooleanAssertions.assertEquals(true, false);
    }

    @After
    void after() {
        System.out.println(variable);
    }

}