package com.n4systems.persistence.listeners;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.services.localization.LocalizationService;
import com.n4systems.util.ServiceLocator;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.ejb.event.EJB3FlushEntityEventListener;
import org.hibernate.event.*;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;
import org.reflections.ReflectionUtils;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.util.*;


public class LocalizationListener extends EJB3FlushEntityEventListener implements PostLoadEventListener, PostUpdateEventListener {

    private static final Logger logger=Logger.getLogger(LocalizationListener.class);

    private static Map<Class<?>, List<LocalizedProperty>> cache = Maps.newHashMap();
    private static Map<Object,List<DirtyLocalizedProperty>> dirtyPropertyMap = Maps.newHashMap();

    public LocalizationListener() {
        dirtyPropertyMap = Maps.newHashMap();
    }

    @Override protected void dirtyCheck(FlushEntityEvent event) throws HibernateException {
        super.dirtyCheck(event);
        removeDirtCausedByLocalization(event);
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        localize(event.getEntity(), event.getPersister(), event.getSession());
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
                Locale locale = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();
                Object translation = getLocalizationService().getTranslation(entity, property.getOgnl(), locale);
                if (translation!=null) {
                    setTranslatedValue(persister, entity, eventSource.getSession(EntityMode.POJO), index, translation);
                }
            }
        } catch (Exception e) {
            logger.error("can't localize entity " + entity.getClass().getSimpleName() + " : " + entity , e);
        }
    }

    private void setTranslatedValue(EntityPersister persister, Saveable entity, Session session, int index, Object translation) {
        Object originalValue = persister.getPropertyValue(entity, index, EntityMode.POJO);
        persister.setPropertyValue(entity, index, translation, EntityMode.POJO);
        List<DirtyLocalizedProperty> dirtyProperties = dirtyPropertyMap.get(entity);
        if (dirtyProperties==null) {
            dirtyPropertyMap.put(entity, dirtyProperties = new ArrayList<DirtyLocalizedProperty>());
        }
        dirtyProperties.add(new DirtyLocalizedProperty(index, translation, originalValue));
        // add to list of fields that need to be ignored when hibernate goes to update this object.
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

    @Override
    public void onFlushEntity(FlushEntityEvent event) throws HibernateException {
        super.onFlushEntity(event);
    }

    private void removeDirtCausedByLocalization(FlushEntityEvent event) {
        // TODO DD: check for MODIFIED.  exclude this one if others are removed.
        List<DirtyLocalizedProperty> dirtyProperties = dirtyPropertyMap.get(event.getEntity());
        if (dirtyProperties==null) {
            return;
        }
        // at this point, we know that localization has caused some dirtiness...we will remove that unless the property did change
        // after that (i.e. it is no longer the translated value).
        int[] dirty = event.getDirtyProperties();
        if (dirty==null) {
            return;
        }
        Object[] propertyValues = event.getPropertyValues();
        List<Integer> newDirty = Lists.newArrayList();
        for (int dirtyIndex:dirty) {
            if (!isDirtyBecauseOfLocalization(dirtyProperties, propertyValues[dirtyIndex], dirtyIndex)) {
                newDirty.add(dirtyIndex);
            }
        }
        event.setDirtyProperties(Ints.toArray(newDirty));
    }

    private boolean isDirtyBecauseOfLocalization(List<DirtyLocalizedProperty> dirtyProperties, Object propertyValue, int i) {
        for (DirtyLocalizedProperty dirtyProperty:dirtyProperties) {
            if (dirtyProperty.index==i) {
                return ObjectUtils.equals(propertyValue, dirtyProperty.dirtyValue);
            }
        }
        return false;
    }

    private LocalizationService getLocalizationService() {
        return ServiceLocator.getLocalizationService();
    }

    class DirtyLocalizedProperty {
        int index;
        Object dirtyValue;  // i.e. the translated value.   "rouge" will be the dirty value for the originally loaded "red" value.
        Object originalValue;

        DirtyLocalizedProperty(int index, Object dirtyValue, Object originalValue) {
            this.index = index;
            this.dirtyValue = dirtyValue;
            this.originalValue = originalValue;
        }
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
