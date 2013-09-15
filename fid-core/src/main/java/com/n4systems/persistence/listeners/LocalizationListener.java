package com.n4systems.persistence.listeners;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.services.localization.LocalizationService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.event.*;
import org.hibernate.event.def.DefaultFlushEntityEventListener;
import org.hibernate.event.def.DefaultInitializeCollectionEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;
import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.util.*;


public class LocalizationListener implements FlushEntityEventListener, PostLoadEventListener, PostUpdateEventListener, PostInsertEventListener, ApplicationContextAware {

    private static final Logger logger=Logger.getLogger(LocalizationListener.class);

    private Map<Class<?>, List<LocalizedProperty>> cache = Maps.newHashMap();
    private ApplicationContext applicationContext;
    private DefaultFlushEntityEventListener flushEntityEventListener;
    private Map<Session,Map<Object,List<DirtyLocalizedProperty>>> translated = Maps.newHashMap();
    private DefaultInitializeCollectionEventListener intializeCollectionEventListener = new DefaultInitializeCollectionEventListener();

    public LocalizationListener() {
        flushEntityEventListener = new DefaultFlushEntityEventListener() {
            @Override protected void dirtyCheck(FlushEntityEvent event) throws HibernateException {
                super.dirtyCheck(event);
                removeDirtCausedByLocalizationProperties(event);
            }
        };
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        localize(event.getEntity(), event.getPersister(), event.getSession());
    }

    @Override
    public void onPostLoad(PostLoadEvent event) {
        localize(event.getEntity(), event.getPersister(), event.getSession());
        event.getSession();
    }

    private void localize(Object entity, EntityPersister persister, EventSource eventSource) {
        if (entity.getClass().isAnnotationPresent(Entity.class) && entity instanceof Saveable) {
            localize((Saveable)entity, persister, eventSource);
        }
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        // not needed...new entities will never be translated.
        //localize(event.getEntity(), event.getPersister(), event.getSession());
    }

    private void localize(Saveable entity, EntityPersister persister, EventSource eventSource) {
        try {
            //System.out.println("localizing " + entity.getClass().getSimpleName());
            for (LocalizedProperty property:getLocalizedProperties(entity,persister)) {
                int index = property.getIndex();
                Locale locale = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();
//                locale = Locale.GERMAN;   // for testing only- force to german.
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
        Map<Object, List<DirtyLocalizedProperty>> entityMap = translated.get(session);
        if (entityMap==null) {
            translated.put(session,entityMap = new HashMap<Object,List<DirtyLocalizedProperty>>());
        }
        List<DirtyLocalizedProperty> dirtyProperties = entityMap.get(entity);
        if (dirtyProperties==null) {
            entityMap.put(entity, dirtyProperties = new ArrayList<DirtyLocalizedProperty>());
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
        flushEntityEventListener.onFlushEntity(event);
        removeOldSessionsFromDirtyPropertyMap();
    }

    private void removeDirtCausedByLocalizationProperties(FlushEntityEvent event) {
        // check for MODIFIED.  exclude this one if others are removed.
        Map<Object,List<DirtyLocalizedProperty>> entityMap = translated.get(event.getSession());
        if (entityMap==null) {
            return;
        }
        List<DirtyLocalizedProperty> dirtyProperties = entityMap.get(event.getEntity());
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

    private void removeOldSessionsFromDirtyPropertyMap() {
        List<Session> oldSessions = Lists.newArrayList();
        for (Session s:translated.keySet()) {
            System.out.println("checking session : open " + s.isOpen() + " , connected " + s.isConnected());
            if (!s.isOpen()) {
                oldSessions.add(s);
            }
        }
        for (Session s:oldSessions) {
            translated.remove(s);
        }
    }

    private LocalizationService getLocalizationService() {
        return applicationContext.getBean(LocalizationService.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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
