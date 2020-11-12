package ru.dio.adapters;

import javax.json.Json;
import javax.json.JsonValue;

public class BooleanAdapter implements TypeAdapter<Boolean> {

    @Override
    public JsonValue toJson(Boolean object) {
        if (object) {
            return JsonValue.TRUE;
        } else {
            return JsonValue.FALSE;
        }
    }
}
