package ru.dio.asm;

//java -javaagent:asm.jar -jar asm.jar

public class Main {

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        testClass.calc();
        testClass.calc(1, (byte) 2);
        testClass.calc(false, 'M', (byte) 1, (short) 12, 123, 123.123f, 333, 123.123);
    }

}
