package ru.otus.jdbc.mapper;

import ru.otus.core.annotation.ID;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private Field id;
    private String name;
    private List<Field> fields;
    private List<Field> fieldsWithoutId;
    private Constructor<T> constructor;
    private final static HashMap<Class<?>, EntityClassMetaDataImpl<?>> obj = new HashMap<>();

    @SuppressWarnings("unchecked")
    public EntityClassMetaDataImpl(Class<T> aClass) {
        if (obj.containsKey(aClass)) {
            EntityClassMetaDataImpl<?> cached = obj.get(aClass);
            id = cached.id;
            name = cached.name;
            fields = cached.fields;
            fieldsWithoutId = cached.fieldsWithoutId;
            constructor = (Constructor<T>) cached.constructor;
        } else {
            findAndSetClassName(aClass);
            findAndSetConstructor(aClass);
            findAndSetIdField(aClass);
            findAndSetAllFields(aClass);
            findAndSetFieldsExceptID(aClass);

            obj.put(aClass, this);
        }

    }

    private void findAndSetClassName(Class<T> aClass) {
        name = aClass.getSimpleName();
    }

    @SuppressWarnings("unchecked")
    private void findAndSetConstructor(Class<T> aClass) {
        constructor = (Constructor<T>) aClass.getDeclaredConstructors()[0];
    }

    private void findAndSetIdField(Class<?> clazz) {
        Optional<Field> id = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ID.class))
                .findFirst();

        id.ifPresentOrElse(
                field -> this.id = field,
                () -> {
                    throw new RuntimeException("Id annotation does not found for class " + clazz.getName());
                }
        );
    }

    private void findAndSetAllFields(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        fields = Arrays.asList(declaredFields);
    }

    private void findAndSetFieldsExceptID(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        this.fieldsWithoutId = Arrays.stream(declaredFields).filter(field -> !field.isAnnotationPresent(ID.class)).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return id;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

}