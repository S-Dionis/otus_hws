package ru.dio.adapters;

import ru.dio.factory.AdapterFactories;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;
import java.util.Collection;

public class CollectionAdapter implements TypeAdapter<Collection> {

    @Override
    public JsonValue toJson(Collection object) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for(Object obj: object) {
            if (obj == null) {
                arrayBuilder.addNull();
                continue;
            }
            Class<?> type = obj.getClass();
            TypeAdapter adapter = AdapterFactories.getAdapter(type);
            JsonValue jsonValue = adapter.toJson(obj);
            arrayBuilder.add(jsonValue);
        }

        return arrayBuilder.build();
    }

}