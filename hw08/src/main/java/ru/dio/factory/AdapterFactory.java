package ru.dio.factory;

import ru.dio.adapters.TypeAdapter;

public interface AdapterFactory {

    TypeAdapter create(Class<?> type);

}