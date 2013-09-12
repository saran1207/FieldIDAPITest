package com.n4systems.fieldid.wicket.components.localization;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.localization.Translation;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.services.localization.LocalizationService;
import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.reflections.ReflectionUtils;
import org.springframework.util.ClassUtils;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class LocalizedFieldsModel extends FieldIDSpringModel<List<LocalizedField>> {

    private static final Logger logger= Logger.getLogger(LocalizedFieldsModel.class);

    private @SpringBean LocalizationService localizationService;
    private @SpringBean LocalizedFieldSorter localizedFieldSorter;


    private Map<Object, List<LocalizedField>> loaded;
    private List<LocalizedField> fields;
    private transient Map<Class<?>, Set<Field>> cache = Maps.newHashMap();
    private final IModel<?> model;
    private final List<Locale> languages;

    LocalizedFieldsModel(IModel<?> model,List<Locale> languages) {
        super();
        this.model = model;
        this.languages = languages;
    }

    protected List<LocalizedField> load() {
        loaded = Maps.newHashMap();

        if (model==null||model.getObject()==null) {
            return Lists.newArrayList();
        }
        model.detach();
        List<LocalizedField> localizedFields = loadForEntity(model.getObject());
        return localizedFields;
    }

    private List<LocalizedField> loadForEntities(Collection<Object> entities) {
        List<LocalizedField> localizedFields = Lists.newArrayList();
        if (entities==null) {
            return localizedFields;
        }
        for (Object entity:entities) {
            localizedFields.addAll(loadForEntity(entity));
        }
        return localizedFields;
    }

    private List<LocalizedField> loadForEntity(Object entity) {
        if (isNotApplicableFor(entity)) {
            return Lists.newArrayList();
        }

        List<LocalizedField> localizedFields = loaded.get(entity);
        if (localizedFields !=null) {
            return localizedFields;
        } else {
            localizedFields = Lists.newArrayList();
        }

        Saveable saveable = (Saveable) entity;
        Set<Field> fields = getAllFields(entity.getClass());
        List<Translation> translations = localizationService.getTranslations(saveable);
        List<LocalizedField> embeddedFields = Lists.newArrayList();

        for (Field field:fields) {
            if (ignoreField(entity, field)) {
                continue;
            }
            if (field.isAnnotationPresent(Localized.class)) {
                localizedFields.addAll(createLocalizedFields(entity, field));
            } else if (isCollection(field)) {
                embeddedFields.addAll(loadForEntities(getValues(entity, field)));
            } else if (!ClassUtils.isPrimitiveOrWrapper(field.getType())) {
                embeddedFields.addAll(loadForEntity(getValue(entity, field)));
            }
        }

        localizedFieldSorter.sort(localizedFields).addAll(embeddedFields);

        // ...now populate translations (if they exist)
        for (Translation translation:translations) {
            boolean translationFound = false;
            for (LocalizedField localizedField:localizedFields) {
                if (localizedField.getOgnl().equals(translation.getId().getOgnl())) {
                    translationFound = true;
                    localizedField.addTranslation(translation);
                    break;
                }
            }
            if (!translationFound) {
                logger.error("a translation exists but its not referenced ==> " + translation );
            }
        }

        loaded.put(entity, localizedFields);
        return localizedFields;
    }

    private final boolean ignoreField(Object entity, Field field) {
        Localized localized = field.getAnnotation(Localized.class);
        if (localized!=null && localized.ignore()) {
            return true;
        }
        return isFiltered(entity,field);
    }

    private boolean isNotApplicableFor(Object entity) {
        if (!(entity instanceof Saveable)) {
            return true;
        }
        if (!entity.getClass().isAnnotationPresent(Entity.class)) {   // might be redundant with Saveable but safer for refactoring.
            return true;
        }
        Localized localized = entity.getClass().getAnnotation(Localized.class);
        if (localized!=null && localized.ignore()) {
            return true;
        }
        return false;
    }

    protected boolean isFiltered(Object entity, Field field) {
        return false;
    }

    private List<LocalizedField> createLocalizedFields(Object entity, Field field) {
        List<LocalizedField> result = Lists.newArrayList();
        String ognl = localizationService.getOgnlFor(field);
        Object value = getValue(entity,field);
        if (value==null) {
            return result;
        } else if (value instanceof List) {  // && list is of generic type <String>?
            List<String> values = (List<String>) value;
            for (int i=0;i<values.size();i++) {
                result.add(new LocalizedField(entity, field.getName(), values.get(i), ognl+i, languages));
            }
        } else if (value instanceof String) {
            System.out.println("ERROR - shouldn't display empty strings when translating.");
           //if (StringUtils.isNotEmpty((String) value)) {   // TODO DD : put this back in later.   leave in to help debugging.
                result.add(new LocalizedField(entity, field.getName(), (String)value, ognl, languages));
           //}
        } else {
            throw new IllegalStateException("the field '" + field.getName() + "' is not of expected type <String> or List<String>");
        }
        return result;
    }

    private Set<Field> getAllFields(Class<?> clazz) {
        Set<Field> fields = cache.get(clazz);
        if (fields==null) {
            cache.put(clazz, fields = ReflectionUtils.getAllFields(clazz, new Predicate<Field>() {
                @Override
                public boolean apply(Field field) {
                    return !(Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers()));
                }
            }));
        }
        return fields;
    }

    private boolean isCollection(Field field) {
        Type genericType = field.getGenericType();
        if (Collection.class.isAssignableFrom(field.getType()) && genericType instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) genericType).getRawType();
            if (rawType instanceof Class) {
                ParameterizedType pt = (ParameterizedType) genericType;
                Type[] fieldArgTypes = pt.getActualTypeArguments();
                for(Type fieldArgType : fieldArgTypes) {
                    if (fieldArgType instanceof Class<?>) {
                        Class<?> fieldClass = (Class<?>) fieldArgType;
                        return fieldClass.isAnnotationPresent(Entity.class);
                    }
                }
            }
        }
        if (field.getType().isArray()) {
            return field.getType().getComponentType().isAnnotationPresent(Entity.class);
        }
        return false;
    }

    private Collection<Object> getValues(Object entity, final Field field) {
        Object value = getValue(entity, field);
        if (value==null) {
            return null;
        }
        if (value.getClass().isArray()) {
            return Arrays.asList((Object[]) value);
        }
        if (value instanceof Collection) {
            return (Collection)value;
        }
        throw new IllegalStateException("trying to get collection when field returns " + value==null?" <null>" : value.getClass().getSimpleName());
    }

    private Object getValue(Object entity, final Field field) {
        try {
            field.setAccessible(true);
            Object value = field.get(entity);
            return value;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("can't get value for " + field.getName() + " in class " + entity.getClass().getSimpleName());
        }
    }

    @Override
    public List<LocalizedField> getObject() {
        if (fields==null) {
            fields = load();
        }
        return fields;
    }

    @Override
    public void setObject(List<LocalizedField> object) {
        System.out.println("this shouldn't be called");
    }

    public List<Translation> getAsTranslations() {
        List<Translation> result = Lists.newArrayList();
        for (LocalizedField localizedField:fields) {
            result.addAll(localizedField.getTranslationsToSave());
        }
        return result;
    }

}
