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
import org.hibernate.event.spi.*;
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

    // TODO : change this to a spring InitializingBean that afterPropertiesSet() reads its values from database.
    // will make it easier to share, refactor, test.
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
        LocalizeableCriteriaResult translatedCriteriaResult = getCriteriaResultToLocalize(entity);
        if (translatedCriteriaResult!=null) {
            translatedCriteriaResult.localize();
        } else if (entity.getClass().isAnnotationPresent(Entity.class) && entity instanceof Saveable) {
            localize((Saveable)entity, persister);
        }
    }

    private LocalizeableCriteriaResult getCriteriaResultToLocalize(Object entity) {
        if (entity instanceof CriteriaResult) {
            CriteriaResult result = (CriteriaResult) entity;
            Criteria criteria = result.getCriteria();
            return new LocalizeableCriteriaResult(result, criteria);
        }
        return null;
    }

    private void localize(Saveable entity, EntityPersister persister) {
        try {
            Locale locale = ThreadLocalInteractionContext.getInstance().getLanguageToUse();
            for (LocalizedProperty property:getLocalizedProperties(entity,persister)) {
                Object translation = getLocalizationService().getTranslation(entity, property.getOgnl(), locale);
                if (translation!=null) {
                    setTranslatedValue(persister, entity, property.getIndex(), translation);
                }
            }
        } catch (Exception e) {
            logger.error("can't localize entity " + entity.getClass().getSimpleName() + " : " + entity , e);
        }
    }

    private void setTranslatedValue(EntityPersister persister, Saveable entity, int index, Object translation) {
        entity.setUntranslatedValue(persister.getPropertyNames()[index], persister.getPropertyValue(entity, index));
        persister.setPropertyValue(entity, index, translation);
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


    /**
     * Blargh : this is all a complete hack. god forgive my sins.
     * here's the reason.
     * Select/ComboCriteriaResults currently store a string instead of a reference to a localized Criteria option.
     * for example, a criteria colour could have localizeable options {Red,Green,Blue} but when you perform an event it will store the string Green instead of a reference to the
     * localzied CriteriaOptions array.  .: it has lost all ability to be translated thru the default mechanism.
     *
     * to get around this, we hack our way thru select/combo results.
     * 1: store the untranslated value when performing the event (Green, NOT Vert)
     * 2: when viewing the event in another language, we lookup the associated criteria for the criteriaResult.
     * 3: from that entity we get the "untranslatedValues" (the values that were nuked by this LocalizationListener but stashed away before doing do so we could have them later.   e.g. save Red,Green,Blue... in a safe place before we overwrite it with Rouge,Vert,Bleu....
     * 4: try to find the matching value.  from that we know the index.  i.e. if the value is Green we know index is 2.
     * 5: lookup the translated value via the localization service.   ie. with the parameters Green, criteria {NOT criteriaResult}, select_criteria@options, 2, FRENCH we will get back Vert.
     * 6: set this value in the criteria result entity.
     * this really sucks.  really, really sucks.  alternatives are A: store reference to value instead of literal string.   B: all of the above.
     */

    class LocalizeableCriteriaResult {
        private final String optionsFieldName = "options";
        private final String valueFieldName = "value";
        private final String recommendationsFieldName = "recommendations";
        private final String deficienciesFieldName = "deficiencies";
        private final String textFieldName = "text";


        private final CriteriaResult result;
        private final Criteria criteria;

        public LocalizeableCriteriaResult(CriteriaResult result, Criteria criteria) {
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
            if (criteria.getTranslatedValues()==null) {
                return;
            }
            localizeOption();
            localizeRecommendations();
            localizeDeficiencies();
        }

        private void localizeRecommendations() {
            List<String> untranslatedRecommendations = (List<String>) criteria.getTranslatedValues().get(recommendationsFieldName);

            if (untranslatedRecommendations==null) {
                return;
            }
            List<Recommendation> recommendations = result.getRecommendations();
            for (Recommendation recommendation:recommendations) {
                for (int i=0;i<untranslatedRecommendations.size();i++) {
                    String text = recommendation.getText();
                    if (text!=null && text.equals(untranslatedRecommendations.get(i))) {
                        recommendation.setText(criteria.getRecommendations().get(i));
                        recommendation.markDirty();
                        result.markDirty();
                    }
                }
            }

        }

        private void localizeDeficiencies() {
            List<String> untranslatedDeficiencies = (List<String>) criteria.getTranslatedValues().get(deficienciesFieldName);

            if (untranslatedDeficiencies==null) {
                return;
            }
            List<Deficiency> deficiencies= result.getDeficiencies();
            for (Deficiency deficiency:deficiencies) {
                for (int i=0;i<untranslatedDeficiencies.size();i++) {
                    String text = deficiency.getText();
                    if (text!=null && text.equals(untranslatedDeficiencies.get(i))) {
                        deficiency.setText(criteria.getDeficiencies().get(i));
                        deficiency.markDirty();
                        result.markDirty();
                    }
                }
            }

        }

        private void localizeOption() {
            List<String> translatedOptions = criteria instanceof SelectCriteria ? ((SelectCriteria) criteria).getOptions() :
                                                criteria instanceof ComboBoxCriteria ? ((ComboBoxCriteria)criteria).getOptions() : null;
            List<String> options = (List<String>) criteria.getTranslatedValues().get(optionsFieldName);

            if (options==null || translatedOptions==null) {
                return;
            }
            if (result instanceof ValueResult) {
                String value = ((ValueResult)result).getValue();
                if (value==null) return;
                for (int i=0;i<options.size();i++) {
                    if (value.equals(options.get(i))) {
                        result.setUntranslatedValue(valueFieldName, value);
                        ((ValueResult) result).setValue( translatedOptions.get(i) );
                        return;
                    }
                }
            }

        }

    }
}
