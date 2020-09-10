package ru.dio.proxy;

public class Main {

    public static void main(String[] args) {
        final Calculation calculation = Proxy.getCalculation();

        calculation.calc();
        calculation.calc(1, 2);
        calculation.calc((short) 1, (short) 2, false);
    }

}
