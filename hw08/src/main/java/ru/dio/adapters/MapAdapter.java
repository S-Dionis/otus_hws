package ru.dio.adapters;

import javax.json.JsonValue;
import java.util.Map;

public class MapAdapter implements TypeAdapter {

    @Override
    public JsonValue toJson(Object object) {
        //Map object = (Map) object;
        return null;
    }
}
