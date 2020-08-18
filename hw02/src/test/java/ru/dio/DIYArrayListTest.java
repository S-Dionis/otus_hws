package ru.dio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class DIYArrayListTest {

    private List<String> lowerCaseLetters;
    private DIYArrayList<String> upperCaseLetters;

    @BeforeEach
    void setUp() {
        lowerCaseLetters = new DIYArrayList<>();
        upperCaseLetters = new DIYArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            lowerCaseLetters.add(String.valueOf(c));
        }
        for (char c = 'A'; c <= 'Z'; c ++) {
            upperCaseLetters.add(String.valueOf(c));
        }
    }

    @DisplayName("Check the Collections.addAll() method")
    @Test
    void collectionsAddAllTest() {
        List<String> expected = new DIYArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            expected.add(String.valueOf(c));
        }

        String[] elements = new String[26];
        for (char c = 'A'; c <= 'Z'; c ++) {
            expected.add(String.valueOf(c));
            elements[c - 65] = String.valueOf(c);
        }

        Collections.addAll(lowerCaseLetters,elements);

        Assertions.assertIterableEquals(expected, lowerCaseLetters);
    }

    @DisplayName("Check the Collections.copy() method")
    @Test
    void collectionsCopyTest() {
        List<String> expected = new DIYArrayList<>();
        for (char c = 'A'; c <= 'Z'; c ++) {
            expected.add(String.valueOf(c));
        }
        Collections.copy(lowerCaseLetters, upperCaseLetters);
        Assertions.assertIterableEquals(expected, lowerCaseLetters);
    }

    @DisplayName("Check the Collections.sort() method")
    @Test
    void collectionsSortTest() {
        List<String> expected = new DIYArrayList<>();
        for (char c = 'A'; c <= 'Z'; c ++) {
            expected.add(String.valueOf(c));
        }
        Collections.shuffle(upperCaseLetters);
        Collections.sort(upperCaseLetters);
        Assertions.assertIterableEquals(expected, upperCaseLetters);
    }

}
