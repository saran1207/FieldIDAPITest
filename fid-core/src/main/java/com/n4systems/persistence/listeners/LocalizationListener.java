package com.n4systems.persistence.listeners;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.services.localization.LocalizationService;
import org.apache.log4j.Logger;
import org.hibernate.EntityMode;
import org.hibernate.event.*;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;
import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class LocalizationListener implements PostLoadEventListener, PostUpdateEventListener, PostInsertEventListener, ApplicationContextAware {

    private static final Logger logger=Logger.getLogger(LocalizationListener.class);

    private Map<Class<?>, List<LocalizedProperty>> cache = Maps.newHashMap();
    private ApplicationContext applicationContext;

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        localize(event.getEntity(), event.getPersister());
    }

    @Override
    public void onPostLoad(PostLoadEvent event) {
        localize(event.getEntity(), event.getPersister());
    }

    private void localize(Object entity, EntityPersister persister) {
        if (entity instanceof Saveable) {
            localize((Saveable)entity, persister);
        }
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        localize(event.getEntity(), event.getPersister());
    }

    private void localize(Saveable entity, EntityPersister persister) {
        try {
            for (LocalizedProperty property:getLocalizedProperties(entity,persister)) {
                // TODO DD/SU : put the locale in thread local and get it from there.
                Locale locale = Locale.GERMAN;
                int index = property.getIndex();
                Object translation = getLocalizationService().getTranslation(entity, property.getOgnl(), locale);
                if (translation!=null) {
                    persister.setPropertyValue(entity, index, translation, EntityMode.POJO);
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
            Set<Field> fields = ReflectionUtils.getAllFields(entity.getClass(), new Predicate() {
                @Override
                public boolean apply(Object input) {
                    Field field = (Field) input;
                    Localized annotation = field.getAnnotation(Localized.class);
                    return annotation != null;
                }
            });
            for (Field field:fields) {
                if (field.getName().equals("recommendations")) {
                    System.out.println("huh");
                }
                LocalizedProperty localizedProperty = createLocalizedProperty(field, persister);
                if (localizedProperty!=null) {
                    properties.add(localizedProperty);
                }
            }
            cache.put(entity.getClass(),properties);
        }
        return properties;
    }

    private LocalizedProperty createLocalizedProperty(Field field, EntityPersister persister) throws Exception {
        Type[] types = persister.getPropertyTypes();
        String[] names = persister.getPropertyNames();
        for (int i=0; i< types.length; i++) {
            if (names[i].equals(field.getName())) {
                return new LocalizedProperty(getLocalizationService().getOgnlFor(field),i);
            }
        }
        // this should never happen? duplicate property names?
        logger.error("can't find localized property " + field.getName() + " in " + persister.getEntityName());
        return null;
    }

    private LocalizationService getLocalizationService() {
        return applicationContext.getBean(LocalizationService.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    class LocalizedProperty {
        String ognl;
        Integer index;

        LocalizedProperty(String ognl, Integer index) {
            this.index = index;
            this.ognl = ognl;
        }

        public String getOgnl() {
            return ognl;
        }

        public Integer getIndex() {
            return index;
        }
    }

}
