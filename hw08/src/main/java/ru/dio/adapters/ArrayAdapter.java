package ru.dio.adapters;

import ru.dio.factory.AdapterFactories;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;
import java.lang.reflect.Array;

public class ArrayAdapter implements TypeAdapter {

    @Override
    public JsonValue toJson(Object object) {
        Class<?> aClass = object.getClass();
        int length = Array.getLength(object);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        Class<?> componentType = aClass.getComponentType();

        TypeAdapter adapter = AdapterFactories.getAdapter(componentType);

        for (int i = 0; i < length; i++) {
            Object arrayElement = Array.get(object, i);
            JsonValue jsonValue = adapter.toJson(arrayElement);
            arrayBuilder.add(jsonValue);
        }

        return arrayBuilder.build();
    }

}
