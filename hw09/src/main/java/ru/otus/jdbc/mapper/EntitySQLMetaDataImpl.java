package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> classMetaData;
    private final String tableName;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> classMetaData) {
        this.classMetaData = classMetaData;
        this.tableName = classMetaData.getName();
    }

    @Override
    public String getSelectAllSql() {
        return String.format("SELECT * FROM %s", tableName);
    }

    @Override
    public String getSelectByIdSql() {
        String id = classMetaData.getIdField().getName();
        return String.format("SELECT * FROM %s WHERE %s = %s", tableName, id, '?');
    }

    @Override
    public String getInsertSql() {
        List<Field> fieldsWithoutId = classMetaData.getFieldsWithoutId();
        String fieldsToInsert = fieldsWithoutId.stream().map(Field::getName).collect(Collectors.joining(","));
        String questionMarks = fieldsWithoutId.stream().map(field -> "?").collect(Collectors.joining(","));
        String fields = "(" + fieldsToInsert + ")";
        String parameters = "(" + questionMarks + ")";

        return "INSERT INTO " +
                tableName +
                fields +
                " VALUES " +
                parameters;

    }

    @Override
    public String getUpdateSql() {
        return null;
    }
}
