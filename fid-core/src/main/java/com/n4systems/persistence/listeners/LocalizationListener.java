package com.n4systems.persistence.listeners;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.persistence.localization.LocalizedText;
import com.n4systems.persistence.localization.LocalizedTextCache;
import org.apache.log4j.Logger;
import org.hibernate.EntityMode;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.*;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocalizationListener implements PostLoadEventListener, PostUpdateEventListener, PostInsertEventListener, Initializable {

    private static final Logger logger=Logger.getLogger(LocalizationListener.class);

    private Map<Class<?>, List<LocalizedProperty>> cache = Maps.newHashMap();
    private Configuration config;

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        localize(event.getEntity(), event.getPersister());
    }

    @Override
    public void onPostLoad(PostLoadEvent event) {
        localize(event.getEntity(), event.getPersister());
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        localize(event.getEntity(), event.getPersister());
    }

    private void localize(Object entity, EntityPersister persister) {
        try {
            for (LocalizedProperty property:getLocalizedProperties(entity,persister)) {
                EntityWithTenant baseEntity = (EntityWithTenant) entity;
                // TODO DD/SU : put the locale in thread local and get it from there.
                Locale locale = Locale.getDefault();
                int index = property.getIndex();
                Map<Locale, String> translations = LocalizedTextCache.getTranslations(baseEntity, property.getOgnl());
//                System.out.println(config.getClassMapping(entity.getClass().getName()).getProperty("text").getColumnIterator().next());
                if (translations!=null) {
                    LocalizedText localizedText = (LocalizedText) persister.getPropertyValue(baseEntity, index, EntityMode.POJO);
                    localizedText.setTranslatedValue(translations, locale);
                }
            }
        } catch (Exception e) {
            logger.error("can't localize entity " + entity.getClass().getSimpleName() + " : " + entity , e);
        }
    }

    private List<LocalizedProperty> getLocalizedProperties(Object entity, EntityPersister persister) throws Exception {
        List<LocalizedProperty> properties = cache.get(entity.getClass());

        if (properties==null) {
            properties = Lists.newArrayList();
            if (entity instanceof EntityWithTenant) {
                for (int i = 0; i < persister.getPropertyTypes().length; i++) {
                    LocalizedProperty localizedProperty = createLocalizedProperty(entity,persister, i);
                    if (localizedProperty!=null) {
                        properties.add(localizedProperty);
                    }
                }
            }
            cache.put(entity.getClass(),properties);
        }
        return properties;
    }

    private LocalizedProperty createLocalizedProperty(Object entity, EntityPersister persister, int i) throws Exception {
        Localized localizedClass = entity.getClass().getAnnotation(Localized.class);
        if (localizedClass==null) {
            return null;
        }
        Type type = persister.getPropertyTypes()[i];
        String propertyName = persister.getPropertyNames()[i];

        if (LocalizedText.class.equals(type.getReturnedClass())) {
            Field field = entity.getClass().getDeclaredField(propertyName);
            Localized localizedField = field.getAnnotation(Localized.class);
            if (localizedField!=null) {
                validate(field, localizedField.value());
                return new LocalizedProperty(i, localizedClass, localizedField);
            } else {
                // e.g. private String foo       should be --->    private @Localized("foo_column") String foo
                throw new IllegalStateException("you have a " + LocalizedText.class.getSimpleName() + " field " + field.getName() + " that doesn't have a mandator @" + Localized.class.getSimpleName() + " annotation with it");
            }
        }
        return null;
    }

    private void validate(Field field, String value) throws IllegalStateException {
        // check to make sure
        return;
    }

    @Override
    public void initialize(Configuration cfg) {
        this.config = cfg;
    }


    class LocalizedProperty {
        String ognl;
        Integer index;

        LocalizedProperty(Integer index, Localized localizedClass, Localized localizedProperty) {
            this.index = index;
            this.ognl = localizedClass.value() + "." + localizedProperty.value();
        }

        public String getOgnl() {
            return ognl;
        }

        public Integer getIndex() {
            return index;
        }
    }

}
