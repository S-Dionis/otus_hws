package ru.dio.adapters;

import javax.json.Json;
import javax.json.JsonValue;
import java.util.Set;

public class StringAdapter implements TypeAdapter {

    @Override
    public JsonValue toJson(Object object) {
        return Json.createValue(object.toString());
    }

}