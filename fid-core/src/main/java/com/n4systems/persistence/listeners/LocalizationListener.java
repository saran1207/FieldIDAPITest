package com.n4systems.persistence.listeners;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.model.*;
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
        // hack...workaround to translate only these type of result entities.
        LocalizeableCriteriaResult translatedCriteriaResult = getCriteriaResultToTranslate(entity);
        if (translatedCriteriaResult!=null) {   //combo, recommendations(prefab), deficiencies, combobox.
            translatedCriteriaResult.localize();
        } else if (entity.getClass().isAnnotationPresent(Entity.class) && entity instanceof Saveable) {
            localize((Saveable)entity, persister, eventSource);
        }
    }

    private LocalizeableCriteriaResult getCriteriaResultToTranslate(Object entity) {
        if (entity instanceof SelectCriteriaResult) {
            SelectCriteriaResult result = (SelectCriteriaResult) entity;
            SelectCriteria criteria = (SelectCriteria) result.getCriteria();
            return new LocalizeableCriteriaResult(result, criteria, result.getValue(), ((SelectCriteria)result.getCriteria()).getOptions());
        } else if (entity instanceof ComboBoxCriteriaResult) {
            ComboBoxCriteriaResult result = (ComboBoxCriteriaResult) entity;
            ComboBoxCriteria criteria = (ComboBoxCriteria) result.getCriteria();
            return new LocalizeableCriteriaResult(result, criteria, result.getValue(), criteria.getOptions());
        }
        return null;
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
        entity.setUntranslatedValue(persister.getPropertyNames()[index], persister.getPropertyValue(entity, index, EntityMode.POJO));
        persister.setPropertyValue(entity, index, translation, EntityMode.POJO);
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

    class LocalizeableCriteriaResult {
        private final String optionsFieldName = "options";
        private final String valueFieldName = "value";
        private final CriteriaResult result;
        private final Criteria criteria;

        public LocalizeableCriteriaResult(CriteriaResult result, Criteria criteria, String value, List<String> values) {
            this.result = result;
            this.criteria = criteria;
        }

        public Saveable getResult() {
            return result;
        }

        public Criteria getCriteria() {
            return criteria;
        }

        public void localize() {
            localizeOption();
//                translate(criteria, "recommendations", result, "recommendations");
//                translate(criteria, "deficiencies", result, "deficiencies");
        }

        private void localizeOption() {

            List<String> options = (List<String>) criteria.getTranslatedValues().get(optionsFieldName);

            if (options==null) {
                return;
            }
            if (result instanceof ValueResult) {
                String value = ((ValueResult)result).getValue();
                for (int i=0;i<options.size();i++) {
                    if (value.equals(options.get(i))) {
                        String ognl = getLocalizationService().getOgnlFor(criteria.getClass(), List.class, "options");
                        Locale language = ThreadLocalInteractionContext.getInstance().getLanguageToUse();
                        Object translation = getLocalizationService().getTranslation(criteria, ognl, language );
                        result.setUntranslatedValue(valueFieldName, value);
                        if (translation!=null && translation instanceof List) {
                            ((ValueResult) result).setValue( ((List<String>)translation).get(i) );
                        }
                        return;
                    }
                }
            }

        }

    }
}
