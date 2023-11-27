package com.example.catalogodefilmes.util.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectConverterImplTests
{
    ObjectConverter converter;

    @BeforeEach
    void init()
    {
        converter = new ObjectConverterImpl();
    }

    @Test
    void testConvertToExtendsEntity()
    {
        TestEntity testEntity = new TestEntity(1, "test", "Lorem Ipsum");
        EntityExtendsTestEntity extendsEntity =
                converter.convert(testEntity, EntityExtendsTestEntity.class);

        assertEquals(testEntity.getId(), extendsEntity.getId());
        assertEquals(testEntity.getName(), extendsEntity.getName());
        assertEquals(testEntity.getDescription(), extendsEntity.getDescription());
    }

    @Test
    void testConvertToTestEntity()
    {
        EntityExtendsTestEntity extendsEntity =
                new EntityExtendsTestEntity(10);
        extendsEntity.setId(1);
        extendsEntity.setName("test");
        extendsEntity.setDescription("Lorem Ipsum");

        TestEntity testEntity =
                converter.convert(extendsEntity, TestEntity.class);

        assertEquals(extendsEntity.getId(), testEntity.getId());
        assertEquals(extendsEntity.getName(), testEntity.getName());
        assertEquals(extendsEntity.getDescription(), testEntity.getDescription());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class TestEntity
    {
        int id;
        String name;
        String description;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class EntityExtendsTestEntity extends TestEntity
    {
        int money;
    }
}
