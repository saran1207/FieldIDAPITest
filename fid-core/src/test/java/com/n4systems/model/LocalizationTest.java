package com.n4systems.model;

import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class LocalizationTest {

    @Before
    public void setUp() {
    }

//    @Test
//    public void test_localized_entities() {
//        Set<Class<?>> entities = new Reflections(BaseEntity.class).getTypesAnnotatedWith(Entity.class);
//        for (Class<?> entity:entities) {
//            if (entity.isAnnotationPresent(Localized.class)) {
//                assertLocalizedClass(entity);
//            } else {
//                assertNonLocalizedClass(entity);
//            }
//        }
//    }
//
//    private void assertNonLocalizedClass(Class<?> entity) {
//        Set<Field> localizedFields = getAllFields(entity, withAnnotation(Localized.class));
//        assertTrue("the class " + entity.getSimpleName() + " is not marked @" + Localized.class.getSimpleName() + " but it has fields that are.  Class level annotation is required.",
//                localizedFields.size() == 0);
//    }
//
//    private void assertLocalizedClass(Class<?> entity) {
//        Set<Field> fields = getAllFields(entity);
//        Map<String,Field> fieldOgnl = Maps.newHashMap();
//        for (Field field:fields) {
//            if (!field.isAnnotationPresent(Localized.class)) {
//                assertNonLocalizedField(entity, field);
//            } else {
//                assertLocalizedField(entity, field, fieldOgnl);
//            }
//        }
//        assertTrue("Warning : you have marked the class " + entity.getSimpleName() + " as being @" + Localized.class.getSimpleName() + " but you don't have any fields annoated as being localizable.  Remove class level annotation if no fields are localized",
//                fieldOgnl.size()>0);
//    }
//
//    private void assertLocalizedField(Class<?> entity, Field field, Map<String, Field> fieldOgnl) {
//        String ognl = field.getAnnotation(Localized.class).value();
//        if (fieldOgnl.get(ognl)!=null) {
//            fail("the ognl '" + ognl + "' is duplicated for fields " + fieldOgnl.get(ognl).getName() + " and " + field.getName());
//        }
//        fieldOgnl.put(ognl, field);
//        assertTrue("the field " + entity.getSimpleName() + "." + field.getName() + " is annotated with @" + Localized.class.getSimpleName() + " and therefore must be of type " + LocalizedText.class.getSimpleName(), field.getType().equals(LocalizedText.class));
//    }
//
//    private void assertNonLocalizedField(Class<?> entity, Field field) {
//        assertTrue("the field " + entity.getSimpleName() + "." + field.getName() + " is of type " + LocalizedText.class.getSimpleName() + " and therefore requires a @" + Localized.class.getSimpleName() + " annotation to specify its ognl", !field.getType().equals(LocalizedText.class));
//    }

}
