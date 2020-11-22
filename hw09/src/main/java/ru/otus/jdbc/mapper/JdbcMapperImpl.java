package ru.otus.jdbc.mapper;

import org.h2.util.StringUtils;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
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
            return dbExecutor.executeSelect(sessionManager.getCurrentSession().getConnection(), sql, id, rs -> rsHandler(clazz, rs));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private T rsHandler(Class<T> clazz, ResultSet rs) {
        final T instance;
        try {
            rs.next();
            instance = classMetaData.getConstructor().newInstance();
        } catch (ReflectiveOperationException | SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        fillInstanceWithValues(clazz, rs, instance);

        return instance;
    }

    private void fillInstanceWithValues(Class<T> clazz, ResultSet rs, T instance) {
        classMetaData.getAllFields().forEach(field -> {
            try {
                String name = field.getName();
                String setter = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method = clazz.getMethod(setter, field.getType());
                method.invoke(instance, rs.getObject(field.getName()));
            } catch (SQLException | ReflectiveOperationException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }
}