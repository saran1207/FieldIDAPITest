package com.n4systems.persistence.listeners;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.model.api.NamedEntity;
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

    private static final Logger logger=Logger.getLogger("LocalizationLog");

    private static Map<Class<?>, List<LocalizedProperty>> cache = Maps.newHashMap();


    public LocalizationListener() {
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        if (event.getEntity() instanceof Saveable) {
            // TODO DD : determine which fields were changed....if translated & auditing fields were only ones then reject.
            boolean translated = ((Saveable) event.getEntity()).isTranslated();
            logger.error(!translated ? "accepting update for " + describe((Saveable) event.getEntity()) : "rejecting changes for " + describe((Saveable) event.getEntity()));
            return translated;
        }
        return false;
    }

    private void localize(Saveable entity, EntityPersister persister, EventSource eventSource) {
        Locale locale = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();

        try {
            StringBuilder builder = new StringBuilder("looking for localizations in " + describe(entity));
            boolean needsNewLine = true;
            for (LocalizedProperty property:getLocalizedProperties(entity,persister)) {
                int index = property.getIndex();
                builder.append(needsNewLine ? "\n" : "");
                needsNewLine = false;
                Object translation = getLocalizationService().getTranslation(entity, property.getOgnl(), locale);
                if (translation!=null) {
                    builder.append("     ***translating " + property.getOgnl() + " : " + persister.getPropertyValue(entity, property.getIndex(), EntityMode.POJO) + " --> " + translation + "\n");
                    setTranslatedValue(persister, entity, index, translation);
                } else {
                    builder.append("      no translations found for " + property.getOgnl() + "\n");
                }
            }
            if (getLocalizedProperties(entity,persister).size()>0) {
                logger.error(builder.toString());
            }
        } catch (Exception e) {
            logger.error("can't localize entity " + entity.getClass().getSimpleName() + " : " + entity , e);
        }
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
                    return annotation != null && !annotation.ignore();
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

    private String describe(Saveable entity) {
        String className = entity.getClass().getSimpleName();
        String name = entity.getEntityId()+"";
        String thread = Thread.currentThread().getName();
        if (entity instanceof NamedEntity) {
            name=((NamedEntity) entity).getName();
        }
        return String.format("%s : %s - #%d (%s)", className, name, entity.hashCode(), thread);
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
