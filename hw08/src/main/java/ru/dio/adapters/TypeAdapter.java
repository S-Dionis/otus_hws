package ru.dio.adapters;

import javax.json.JsonValue;
import java.util.Collection;
import java.util.Set;

public interface TypeAdapter<T> {

    JsonValue toJson(T object);

}
