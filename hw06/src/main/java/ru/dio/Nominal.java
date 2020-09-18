package ru.dio;

public enum Nominal {

    ONE_HUNDRED(100),
    FIVE_HUNDRED(500),
    TEN_HUNDRED(1000),
    FIFTY_HUNDRED(5000);

    int value;

    Nominal(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}

