package ru.dio;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    JsonMaker json;
    Gson gson;

    @BeforeEach
    void setUp() {
        json = new JsonMaker();
        gson = new Gson();
    }

    @Test
    public void ClassToJson() {
        PrimitivesAndWrappers primitives = new PrimitivesAndWrappers();

        String expected = gson.toJson(primitives);
        String actual = json.toJson(primitives);

        assertEquals(expected, actual);
    }

    static class PrimitivesAndWrappers {
        private byte b = 1;
        private char c = 'A';
        private short s = 3;
        private int i = 4;
        private long l = 5;
        private double d = 6;
        private float f = 7;
        private boolean z = true;

        private Byte bb = 1;
        private Character cc = 'A';
        private Short ss = 3;
        private Integer ii = 4;
        private Long ll = 5l;
        private Double dd = 6d;
        private Float ff = 7f;
        private Boolean zz = true;
    }

    @Test
    public void arraysAndCollectionsToJson() {
        ArraysAndCollections arraysAndCollections = new ArraysAndCollections();

        String expected = gson.toJson(arraysAndCollections);
        String actual = json.toJson(arraysAndCollections);

        assertEquals(expected, actual);
    }

    static class ArraysAndCollections {
        private int i[] = new int[] {1, 2, 3};
        private int empty[] = new int[0];
        private List<String> strs = List.of("a", "b", "c");
    }

    @Test
    public void nullToJson() {
        String expected = gson.toJson(null);
        String actual = json.toJson(null);
        assertEquals(expected, actual);
    }

    @Test
    public void nullFieldToJson() {
        Nulls nulls = new Nulls();
        String expected = gson.toJson(nulls);
        String actual = json.toJson(nulls);
        assertEquals(expected, actual);
    }

    static class Nulls {

        private List<String> strs = null;
        private List<String> withNull = new ArrayList<>();

        {
            withNull.add(null);
        }

    }

}