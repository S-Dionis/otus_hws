package ru.dio.factory;

import ru.dio.adapters.*;

import java.lang.reflect.Type;
import java.util.*;

public class AdapterFactories {

    static class ArrayAdapterFactory implements AdapterFactory {

        @Override
        public TypeAdapter create(Class<?> type) {
            if (type.isArray()) {
                return new ArrayAdapter();
            }
            return null;
        }
    }

    static class CollectionAdapterFactory implements AdapterFactory {

        @Override
        public TypeAdapter create(Class<?> type) {
            if (Collection.class.isAssignableFrom(type)) {
                return new CollectionAdapter();
            }
            return null;
        }
    }

    static class FloatAdapterFactory implements AdapterFactory {

        Set<Class<?>> doubleNumbers = Set.of(Double.TYPE, Double.class, Float.TYPE, Float.class);

        @Override
        public TypeAdapter create(Class<?> type) {
            if (doubleNumbers.contains(type)) {
                return new FloatAdapter();
            }
            return null;
        }

    }

    static class NumberAdapterFactory implements AdapterFactory {


        Set<Class<?>> integerNumbers = Set.of(Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Byte.class, Short.class, Integer.class, Long.class);

        @Override
        public TypeAdapter create(Class<?> type) {
            if (integerNumbers.contains(type)) {
                return new NumberAdapter();
            }
            return null;
        }
    }

    static class StringAdapterFactory implements AdapterFactory {

        Set<Class<?>> strings = Set.of(String.class, Character.class, Character.TYPE);

        @Override
        public TypeAdapter create(Class<?> type) {
            if (strings.contains(type)) {
                return new StringAdapter();
            }
            return null;
        }
    }

    static class ClassAdapterFactory implements AdapterFactory {

        @Override
        public TypeAdapter create(Class<?> type) {
            if (Object.class.isAssignableFrom(type)) {
                return new ClassAdapter();
            }
            return null;
        }
    }

    static class BooleanAdapterFactory implements AdapterFactory {

        Set<Class<?>> booleans = Set.of(Boolean.class, Boolean.TYPE);

        @Override
        public TypeAdapter create(Class<?> type) {
            if (booleans.contains(type)) {
                return new BooleanAdapter();
            }
            return null;
        }

    }

    static class MapAdapterFactory implements AdapterFactory {

        @Override
        public TypeAdapter create(Class<?> type) {
            if (Map.class.isAssignableFrom(type)) {
                return new MapAdapter();
            }
            return null;
        }
    }


    static List<AdapterFactory> factories = new ArrayList<>();

    static {
        factories.add(new ArrayAdapterFactory());
        factories.add(new FloatAdapterFactory());
        factories.add(new NumberAdapterFactory());
        factories.add(new BooleanAdapterFactory());
        factories.add(new StringAdapterFactory());
        factories.add(new CollectionAdapterFactory());
        factories.add(new ClassAdapterFactory());
    }

    public static TypeAdapter getAdapter(Class<?> type) {

        for (AdapterFactory factory : factories) {
            TypeAdapter typeAdapter = factory.create(type);
            if (typeAdapter != null) {
                return typeAdapter;
            }
        }

        throw new RuntimeException("Found no appropriate type adapter");
    }

}