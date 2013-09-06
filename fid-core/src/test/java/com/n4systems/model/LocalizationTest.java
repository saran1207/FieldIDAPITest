package com.n4systems.model;

import com.google.common.collect.Sets;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;
import com.n4systems.persistence.localization.Localized;
import org.junit.Test;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

public class LocalizationTest {

    @Test
    public void test_localized_entities() {
        Set<Class> entities = Sets.newHashSet();
        entities.addAll(new Reflections(BaseEntity.class.getPackage().getName()).getSubTypesOf(BaseEntity.class));
        entities.addAll(new Reflections("rfid.ejb").getSubTypesOf(LegacyBaseEntity.class));
        for (Class<?> entity:entities) {
            assertClass(entity);
        }
    }

    private void assertClass(Class<?> entity) {
        Set<Field> localizedFields = getAllFields(entity, withAnnotation(Localized.class));
        for (Field localizedField:localizedFields) {
            assertTrue("class " + entity.getName() + " field '" + localizedField.getName() + "' with @" + Localized.class.getSimpleName() + " annotation must return String or List<String>", isStringReturnType(localizedField));
        }
    }

    private boolean isStringReturnType(Field field) {
        Type genericType = field.getGenericType();
        if (Collection.class.isAssignableFrom(field.getType()) && genericType instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) genericType).getRawType();
            if (rawType instanceof Class) {
                ParameterizedType pt = (ParameterizedType) genericType;
                Type[] fieldArgTypes = pt.getActualTypeArguments();
                for(Type fieldArgType : fieldArgTypes) {
                    if (fieldArgType instanceof Class<?>) {
                        Class<?> fieldClass = (Class<?>) fieldArgType;
                        return String.class.isAssignableFrom(fieldClass);
                    }
                }
            }
            return false;
        } else {
            return String.class.equals(field.getType());
        }
    }

}
