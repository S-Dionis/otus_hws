package ru.dio;

import ru.dio.adapters.TypeAdapter;
import ru.dio.factory.AdapterFactories;

import javax.json.JsonValue;

public class JsonMaker {

    public JsonMaker() {
    }

    @SuppressWarnings("unchecked")
    public String toJson(Object object) {
        if (object == null)
            return "null";
        TypeAdapter<Object> adapter = AdapterFactories.getAdapter(object.getClass());
        JsonValue jsonValue = adapter.toJson(object);
        return jsonValue.toString();
    }

}
