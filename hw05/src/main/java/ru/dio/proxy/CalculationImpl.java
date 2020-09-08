package ru.dio.proxy;

public class CalculationImpl implements Calculation {

    @Log
    @Override
    public void calc() {
    }

    @Log
    @Override
    public void calc(int a, int b) {
    }

    @Log
    @Override
    public void calc(short a, short b, boolean z) {
    }

    @Log
    @Override
    public void calc(String s) {
        System.out.println("red");
    }


}
