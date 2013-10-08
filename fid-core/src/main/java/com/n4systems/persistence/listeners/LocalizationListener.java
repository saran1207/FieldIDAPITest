package com.n4systems.persistence.listeners;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.services.localization.LocalizationService;
import com.n4systems.util.ServiceLocator;
import org.apache.log4j.Logger;
import org.hibernate.EntityMode;
import org.hibernate.event.*;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;
import org.reflections.ReflectionUtils;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class LocalizationListener implements PostLoadEventListener, PreUpdateEventListener {

    private static final Logger logger=Logger.getLogger(LocalizationListener.class);

    private static Map<Class<?>, List<LocalizedProperty>> cache = Maps.newHashMap();


    public LocalizationListener() {
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        if (event.getEntity() instanceof Saveable) {
            return ((Saveable)event.getEntity()).isTranslated();
        }
        return false;
    }

    @Override
    public void onPostLoad(PostLoadEvent event) {
        localize(event.getEntity(), event.getPersister(), event.getSession());
    }

    private void localize(Object entity, EntityPersister persister, EventSource eventSource) {
        if (entity.getClass().isAnnotationPresent(Entity.class) && entity instanceof Saveable) {
            localize((Saveable)entity, persister, eventSource);
        }
    }

    private void localize(Saveable entity, EntityPersister persister, EventSource eventSource) {
        try {
            for (LocalizedProperty property:getLocalizedProperties(entity,persister)) {
                int index = property.getIndex();
                Locale locale = ThreadLocalInteractionContext.getInstance().getLanguageToUse();
                Object translation = getLocalizationService().getTranslation(entity, property.getOgnl(), locale);
                if (translation!=null) {
                    setTranslatedValue(persister, entity, index, translation);
                }
            }
        } catch (Exception e) {
            logger.error("can't localize entity " + entity.getClass().getSimpleName() + " : " + entity , e);
        }
    }

    private void setTranslatedValue(EntityPersister persister, Saveable entity, int index, Object translation) {
        persister.setPropertyValue(entity, index, translation, EntityMode.POJO);
        entity.setTranslated(true);
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
        return ServiceLocator.getLocalizationService();
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
