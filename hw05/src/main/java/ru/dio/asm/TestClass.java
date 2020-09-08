package ru.dio.asm;

/**
 * Class for testing ASM logging
 */
public class TestClass {

    @Log
    void calc() {
    }

    @Log
    void calc(int a, byte b) {
    }

    @Log
    void calc(boolean Z, char C, byte B, short S, int I, float F, long J, double D) {
    }

}
