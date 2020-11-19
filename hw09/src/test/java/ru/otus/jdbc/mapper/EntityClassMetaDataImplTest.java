package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.core.annotation.ID;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityClassMetaDataImplTest {

    private static EntityClassMetaData<TestUser> userEntity;

    private static Field idFieldExpected;
    private static List<Field> allFieldsExpected;
    private static List<Field> fieldsWithoutIdExpected;

    @BeforeAll
    static void init() throws NoSuchFieldException {
        TestUser testUser = new TestUser(1, "Cat");
        Class<TestUser> userClass = TestUser.class;

        userEntity = new EntityClassMetaDataImpl<>(TestUser.class);
        idFieldExpected = userClass.getDeclaredField("id");
        fieldsWithoutIdExpected = List.of(userClass.getDeclaredField("name"));
        allFieldsExpected = List.of(idFieldExpected, userClass.getDeclaredField("name"));
    }

    @Test
    void getIdField() {
        assertEquals(idFieldExpected, userEntity.getIdField());
    }

    @Test
    void getAllFields() {
        assertEquals(allFieldsExpected, userEntity.getAllFields());
    }

    @Test
    void getFieldsWithoutId() {
        assertEquals(fieldsWithoutIdExpected, userEntity.getFieldsWithoutId());
    }

    static class TestUser {
        @ID
        private final long id;
        private final String name;

        public TestUser(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

    }

}