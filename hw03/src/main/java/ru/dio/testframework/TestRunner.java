package ru.dio.testframework;

import ru.dio.TestClass;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TestRunner {

    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001b[31m";

    private final List<Method> beforeMethods = new ArrayList<>();
    private final List<Method> afterMethods = new ArrayList<>();
    private final List<Method> testMethods = new ArrayList<>();
    private final PrintStream out = System.out;
    private String className;
    private Class<?> clazz = null;
    private int methodsCount = 0;
    private int passed = 0;

    public static void main(String[] args) {
        new TestRunner().runTestFor(TestClass.class);
    }

    public void runTestFor(Class<?> c) {
        clazz = Objects.requireNonNull(c);
        className = clazz.getName();

        try {
            findAnnotatedMethods();
            setMethodsCount(testMethods.size());
            runTests();
            printResult();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void findAnnotatedMethods() {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            method.setAccessible(true);

            if (method.getDeclaredAnnotation(Test.class) != null) {
                testMethods.add(method);
            }
            if (method.getDeclaredAnnotation(After.class) != null) {
                afterMethods.add(method);
            }
            if (method.getDeclaredAnnotation(Before.class) != null) {
                beforeMethods.add(method);
            }
        }
    }

    private void setMethodsCount(int passedMethodsCount) {
        passed = methodsCount = passedMethodsCount;
    }

    private void runTests() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Method testMethod : testMethods) {
            boolean pass = runTest(testMethod, getInstanceOf(clazz));
            String messageToPrint = className + "." + testMethod.getName() + "()";
            if (pass) {
                printOnSuccess(messageToPrint);
            } else {
                printOnError(messageToPrint);
                passed--;
            }
        }
    }

    private Object getInstanceOf(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private boolean runTest(Method testMethod, Object o) {
        boolean pass = true;

        try {
            for (Method beforeMethod : beforeMethods) {
                beforeMethod.invoke(o);
            }
            testMethod.invoke(o);
        } catch (Exception ex) {
            pass = false;
        } finally {
            for (Method afterMethod : afterMethods) {
                try {
                    afterMethod.invoke(o);
                } catch (Exception exception) {
                    pass = false;
                }
            }
        }

        return pass;
    }

    private void printOnError(String message) {
        out.println(RED + message + RESET + System.lineSeparator());
    }

    private void printOnSuccess(String message) {
        out.println(GREEN + message + RESET + System.lineSeparator());
    }

    private void printResult() {
        out.println(YELLOW
                + "Test failed " + (methodsCount - passed)
                + " Test passed " + passed
                + RESET);
    }

}