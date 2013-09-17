package com.n4systems.fieldid.wicket.components.localization;

import com.google.common.collect.Sets;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;
import com.n4systems.persistence.localization.Localized;
import org.junit.Test;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Set;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

public class LocalizedFieldSorterTest {

    private LocalizedFieldSorter localizedFieldSorter = new LocalizedFieldSorter();

    @Test
    public void test_sorter_handles_all_fields() {
        Set<Class> entities = getAllEntities();
        for (Class entity:entities) {
            assertSorterFor(entity);
        }
    }

    private Set<Class> getAllEntities() {
        Set<Class> entities = Sets.newHashSet();
        entities.addAll(new Reflections(BaseEntity.class.getPackage().getName()).getSubTypesOf(BaseEntity.class));
        entities.addAll(new Reflections("rfid.ejb").getSubTypesOf(LegacyBaseEntity.class));
        return entities;
    }

    private void assertSorterFor(Class entity) {
        Set<Field> localizedFields = getAllFields(entity, withAnnotation(Localized.class));
        for (Field localizedField:localizedFields) {
            if (!localizedField.getAnnotation(Localized.class).ignore()) {
                // will throw exception if failed.
                int i = localizedFieldSorter.getFieldOrdinal(entity, localizedField.getName());
            } else {
                System.out.println("ignoring localization for field " + localizedField.getName() + " in class " + entity.getSimpleName() + "." );
            }
        }
    }

}
