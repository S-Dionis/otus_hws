package ru.dio.adapters;

import javax.json.*;
import java.util.Collection;
import java.util.Set;

public class NumberAdapter implements TypeAdapter {

    @Override
    public JsonValue toJson(Object object) {
        long l = ((Number) object).longValue();
        return Json.createValue(l);
    }

}