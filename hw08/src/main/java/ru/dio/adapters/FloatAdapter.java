package ru.dio.adapters;

import javax.json.Json;
import javax.json.JsonValue;
import java.util.Set;

public class FloatAdapter implements TypeAdapter<Number> {

    @Override
    public JsonValue toJson(Number object) {
        return Json.createValue(object.doubleValue());
    }

}