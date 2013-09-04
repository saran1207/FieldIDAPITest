package com.n4systems.fieldid.wicket.components.localization;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.model.Criteria;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.ScoreCriteria;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.localization.Translation;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.services.localization.LocalizationService;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.reflections.ReflectionUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class LocalizationPanel extends Panel {

    private static final Logger logger= Logger.getLogger(LocalizationPanel.class);

    private @SpringBean LocalizationService localizationService;

    private Locale language;
    private final FormComponent<Locale> chooseLanguage;
    private final ListView<LocalizedField> listView;

    public LocalizationPanel(String id, IModel<? extends EntityWithTenant> model) {
        super(id, model);

        add(new Form("form")
                .add(listView = new ListView<LocalizedField>("translations", new LocalizedFieldsModel()) {
                    @Override
                    protected void populateItem(ListItem<LocalizedField> item) {
                        final LocalizedField field = item.getModelObject();
                        item.add(new Label("label", Model.of(field.getName())));
                        item.add(new Label("defaultValue", Model.of(field.getDefaultValue())));
                        item.add(new TranslationsListView("translations", new PropertyModel(item.getModel(), "translations")));
                        item.add(new AjaxLink("scoreGroups") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {

                            }

                            @Override
                            public boolean isVisible() {
                                return field.entity instanceof ScoreCriteria;
                            }
                        });
                        item.add(new AjaxLink("buttonGroups") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {

                            }

                            @Override
                            public boolean isVisible() {
                                return field.entity instanceof OneClickCriteria;
                            }
                        });
                        item.add(new AjaxLink("observations") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {

                            }

                            @Override
                            public boolean isVisible() {
                                if (field.entity instanceof Criteria) {
                                    Criteria criteria = (Criteria) field.entity;
                                    return !criteria.getRecommendations().isEmpty() || !criteria.getDeficiencies().isEmpty();
                                }
                                return false;
                            }
                        });
                    }
                }.setReuseItems(true))
                .add(chooseLanguage = new FidDropDownChoice<Locale>("language", new PropertyModel(this, "language"), getLanguages()).setNullValid(false).setRequired(true))
                .add(new AjaxSubmitLink("submit") {
                    @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        localizationService.save(convertToTranslations());
                    }

                    @Override protected void onError(AjaxRequestTarget target, Form<?> form) {

                    }
                })
        );
        chooseLanguage.add(new AjaxFormSubmitBehavior("onchange") {
            @Override protected void onSubmit(AjaxRequestTarget target) {
                target.add(LocalizationPanel.this);
            }
            @Override protected void onError(AjaxRequestTarget target) {

            }
        });

    }

    private List<Translation> convertToTranslations() {
        LocalizedFieldsModel model = (LocalizedFieldsModel) listView.getDefaultModel();
        return model.getAsTranslations();
    }

//    public LocalizationPanel withFieldFilter(Predicate<Field> filter) {
//        this.fieldFilter = filter;
//        return this;
//    }
//
//    public Predicate<Field> getFieldFilter() {
//        return fieldFilter;
//    }

    @Override
    protected void onModelChanged() {
        super.onModelChanged();
        listView.setDefaultModel(new LocalizedFieldsModel());
    }

    private List<Locale> getLanguages() {
        return FieldIDSession.get().getTenant().getSettings().getLanguages();
    }


    class TranslationsListView extends ListView<Translation> {
        TranslationsListView(String id, IModel<List<Translation>> list) {
            super(id, list);
            setReuseItems(true);
        }

        @Override protected void populateItem(ListItem<Translation> item) {
            Translation translation = item.getModelObject();
            final Locale itemLanguage = getLanguages().get(item.getIndex());
            item.add(new TextField("translation", new PropertyModel(item.getModel(),"value")) {
                @Override public boolean isVisible() {
                    return itemLanguage.equals(language);
                }
            });
        }
    }

    class LocalizedFieldsModel implements IModel<List<LocalizedField>> {

        private Set<Object> loaded;
        private List<LocalizedField> fields;
        private transient Map<Class<?>, Set<Field>> cache = Maps.newHashMap();

        LocalizedFieldsModel() {
            super();
        }

        protected List<LocalizedField> load() {
            loaded = Sets.newHashSet();
            IModel<?> model = LocalizationPanel.this.getDefaultModel();
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
            List<LocalizedField> localizedFields = Lists.newArrayList();
            if (!(entity instanceof Saveable)) {
                return localizedFields;
            }

            Saveable saveable = (Saveable) entity;
            if (loaded.contains(entity)) {
                return localizedFields;    //already done?
            }
            loaded.add(entity);

            Long id = (Long) ((Saveable) entity).getEntityId();

            Set<Field> fields = getAllFields(entity.getClass());

            List<Translation> translations = localizationService.getTranslations(id);

            for (Field field:fields) {
//                System.out.println("scanning field " + field.getName() + " in class " + field.getDeclaringClass().getSimpleName());
                if (field.isAnnotationPresent(Localized.class) && field.getType().equals(String.class)) {
                    String ognl = localizationService.getOgnlFor(field);
                    // create empty translation for all languages here....override later if other ones exist.
                    localizedFields.add(new LocalizedField(entity, field.getName(), (String) getValue(entity, field), ognl));
                } else if (isCollection(field)){
                    localizedFields.addAll(loadForEntities(getValues(entity, field)));
                } else if (!ClassUtils.isPrimitiveOrWrapper(field.getType())) {
                    localizedFields.addAll(loadForEntity(getValue(entity, field)));
                }
            }

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
            return localizedFields;
        }

        private Set<Field> getAllFields(Class<?> clazz) {
            Set<Field> fields = cache.get(clazz);
            if (fields==null) {
               cache.put(clazz, fields = ReflectionUtils.getAllFields(clazz, new Predicate<Field>() {
                   @Override public boolean apply(Field field) {
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
                            return Saveable.class.isAssignableFrom(fieldClass);
                        }
                    }
                }
            }
            if (field.getType().isArray()) {
                return Saveable.class.isAssignableFrom(field.getType().getComponentType());
            }
            return false;
        }

        private Collection<Object> getValues(Object entity, final Field field) {
            Object value = getValue(entity, field);
            if (value==null) {
                return null;
            }
            if (value.getClass().isArray()) {
                return Arrays.asList((Object[])value);
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

        @Override
        public void detach() {
        }

        public List<Translation> getAsTranslations() {
            List<Translation> result = Lists.newArrayList();
            for (LocalizedField localizedField:fields) {
                result.addAll(localizedField.getTranslationsToSave());
            }
            return result;
        }
    }


    class LocalizedField implements Serializable {
        private List<Translation> translations = new ArrayList<Translation>(Collections.nCopies(getLanguages().size(),(Translation)null));
        private String defaultValue;
        private String name;
        private final Object entity;
        private final String ognl;

        public LocalizedField(Object entity, String name, String defaultValue, String ognl) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.entity = entity;
            this.ognl = ognl;

            Long tenantId = FieldIDSession.get().getTenant().getId();
            Long entityId = (Long) ((Saveable) entity).getEntityId();
            for (Locale language:getLanguages()) {
                addTranslation(Translation.makeNew(tenantId, entityId, ognl, language));
            }
        }

        public LocalizedField addTranslation(Translation translation) {
            // all tenantid, entityid, ognl should be same in this list. just the language should be different.
            Locale locale = StringUtils.parseLocaleString(translation.getId().getLanguage());
            int index = getLanguages().indexOf(locale);
            if (index<0) {
                logger.error("language of translation " + translation + " is not supported in list " + getLanguages());
            } else {
                translations.set(index, translation);
            }
            return this;
        }

        public List<Translation> getTranslationsToSave() {
            List<Translation> result = Lists.newArrayList();
            for (Translation translation:translations) {
                if (!translation.isNew() || org.apache.commons.lang.StringUtils.isNotBlank(translation.getValue())) {
                    result.add(translation);
                }
            }
            return result;
        }

        public List<Translation> getTranslations() {
            return translations;
        }

        public void setTranslations(List<Translation> translations) {
            this.translations = translations;
        }

        @Override
        public String toString() {
            return defaultValue + ':' + translations;
        }

        public String getName() {
            return name;
        }

        public String getOgnl() {
            return ognl;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public Object getEntity() {
            return entity;
        }
    }

}
