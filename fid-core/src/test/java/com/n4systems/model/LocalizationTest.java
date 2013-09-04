package com.n4systems.model;

import com.google.common.collect.Sets;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;
import com.n4systems.persistence.localization.Localized;
import org.junit.Test;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.Assert.assertEquals;
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
            assertEquals("field '" + localizedField.getName() + "' with @" + Localized.class.getSimpleName() + " annotation must return String", String.class, localizedField.getType());
        }
    }

}
