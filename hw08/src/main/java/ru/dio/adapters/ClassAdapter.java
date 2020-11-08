package ru.dio.adapters;

import ru.dio.factory.AdapterFactories;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.lang.reflect.Field;

public class ClassAdapter implements TypeAdapter {

    @Override
    public JsonValue toJson(Object object) {
        Class<?> aClass = object.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        for (Field field : declaredFields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            TypeAdapter adapter = AdapterFactories.getAdapter(type);
            Object fieldValue = null;
            try {
                fieldValue = field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Can't get field value");
            }
            if (fieldValue != null) {
                JsonValue jsonValue = adapter.toJson(fieldValue);
                objectBuilder.add(field.getName(), jsonValue);
            }
        }

        return objectBuilder.build();
    }

}
