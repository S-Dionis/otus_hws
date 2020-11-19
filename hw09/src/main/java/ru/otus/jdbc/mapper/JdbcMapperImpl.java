package ru.otus.jdbc.mapper;

import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {

    private final DbExecutor<T> dbExecutor;
    private final EntityClassMetaData<T> classMetaData;
    private final EntitySQLMetaData sqlMetaData;
    private final SessionManagerJdbc sessionManager;

    public JdbcMapperImpl(
            SessionManagerJdbc sessionManager,
            DbExecutor<T> dbExecutor,
            EntityClassMetaData<T> classMetaData,
            EntitySQLMetaData sqlMetaData
    ) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.classMetaData = classMetaData;
        this.sqlMetaData = sqlMetaData;
    }

    @Override
    public long insert(T objectData) {
        String sql = sqlMetaData.getInsertSql();

        List<Field> fieldsWithoutId = classMetaData.getFieldsWithoutId();
        List<Object> fieldValues = fieldsWithoutId.stream().map(field -> {
            try {
                field.setAccessible(true);
                return field.get(objectData);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Can't access field " + field + " for class " + objectData.getClass());
            }
        }).collect(Collectors.toList());

        try {
            return dbExecutor.executeInsert(sessionManager.getCurrentSession().getConnection(), sql, fieldValues);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public long update(T objectData) {
        return 0;
    }

    @Override
    public long insertOrUpdate(T objectData) {
        return 0;
    }

    @Override
    public Optional<T> findById(Object id, Class<T> clazz) {
        String sql = sqlMetaData.getSelectByIdSql();
        try {
            return dbExecutor.executeSelect(sessionManager.getCurrentSession().getConnection(), sql, id, this::rsHandler);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private T rsHandler(ResultSet rs) {
        try {
            rs.next();

            Object[] args = classMetaData.getAllFields().stream().map(field -> {
                try {
                    return rs.getObject(field.getName());
                } catch (SQLException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }).toArray();

            return classMetaData.getConstructor().newInstance(args);
        } catch (SQLException | ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
