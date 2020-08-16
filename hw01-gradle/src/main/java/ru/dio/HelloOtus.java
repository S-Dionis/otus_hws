package ru.dio;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class HelloOtus {

    public static void main(String[] args) {
        Set<String> greeting = ImmutableSet.of("h", "i");
        greeting.forEach(System.out::print);
    }

}