package com.n4systems.fieldid.wicket.components.localization;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.model.Criteria;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.ScoreCriteria;
import com.n4systems.model.localization.Translation;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;
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
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
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
                .add(listView = new ListView<LocalizedField>("values", new LocalizedFieldsModel()) {
                    @Override
                    protected void populateItem(ListItem<LocalizedField> item) {
                        final LocalizedField field = item.getModelObject();
                        item.add(new Label("label", Model.of(field.getName())));
                        item.add(new Label("defaultValue", Model.of(field.getDefaultValue())));
                        item.add(new TranslationsListView("translations", new PropertyModel(item.getModel(), "values")));
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
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        List<LocalizedField> fields = (List<LocalizedField>) getDefaultModel().getObject();
                        for (LocalizedField field : fields) {
                            System.out.println(field);
                        }
                    }

                    @Override
                    protected void onError(AjaxRequestTarget target, Form<?> form) {

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

    @Override
    protected void onModelChanged() {
        super.onModelChanged();
        listView.setDefaultModel(new LocalizedFieldsModel());
    }

    private List<Locale> getLanguages() {
        return FieldIDSession.get().getTenant().getSettings().getLanguages();
    }


    class TranslationsListView extends ListView<String> {
        TranslationsListView(String id, IModel<List<? extends String>> list) {
            super(id, list);
            setReuseItems(true);
        }

        @Override protected void populateItem(ListItem<String> item) {
            String translation = item.getModelObject();
            final Locale itemLanguage = getLanguages().get(item.getIndex());
            item.add(new TextField("translation", item.getModel()) {
                @Override public boolean isVisible() {
                    return itemLanguage.equals(language);
                }
            });
        }
    }

    class LocalizedFieldsModel implements IModel<List<LocalizedField>> {

        private Set<Object> loaded;
        private List<LocalizedField> fields;

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

            Map<String,LocalizedField> localizedFields = loadForEntity(model.getObject());
            return Lists.newArrayList(localizedFields.values());
        }

        private Map<String, LocalizedField> loadForEntities(Collection<?> entities) {
            Map<String,LocalizedField> localizedFields = Maps.newTreeMap();
            for (Object entity:entities) {
                localizedFields.putAll(loadForEntity(entity));
            }
            return localizedFields;
        }

        private Map<String, LocalizedField> loadForEntity(Object entity) {
            Map<String,LocalizedField> localizedFields = Maps.newTreeMap();
            if (loaded.contains(entity)) {
                return localizedFields;    //already done?
            }
            loaded.add(entity);

            Long id = null;
            if (entity instanceof EntityWithTenant) {
                id = ((EntityWithTenant)entity).getId();
            } else if (entity instanceof LegacyBaseEntity) {
                id = ((LegacyBaseEntity)entity).getUniqueID();
            }

//            // TODO DD : refactor this all into service??
//            List<Translation> translations = localizationService.getTranslations(entity);
//            Set<Field> fields = ReflectionUtils.getAllFields(entity.getClass(), withAnnotation(Localized.class));
//
//            // stick in default values...
//            for (Field field:fields) {
//                String ognl = localizationService.getOgnlFor(entity.getClass(), field);
//                LocalizedText text = getLocalizedTextValue(entity, field);
//                localizedFields.put(ognl+id, new LocalizedField(entity, field.getName(), text.getText()));
//            }
//
//            // ...now add translations (if they are there)
//            for (Translation translation:translations) {
//                LocalizedField localizedField = localizedFields.get(translation.getId().getOgnl());
//                if (localizedField==null) {   // doesn't apply..? this is a weird state.  there is a translation for a field that isn't marked as translatable?
//                    logger.warn("the translation " + translation + " exists but it doesn't seem to map to any current entity/field");
//                } else {
//                    localizedField.addTranslation(translation);
//                }
//            }
//
//            // .. now scan for embedded LocalizedText fields.
//            fields = ReflectionUtils.getAllFields(entity.getClass(), new Predicate() {
//                @Override public boolean apply(Object input) {
//                    Field field = (Field) input;
//                    return !Modifier.isStatic(field.getModifiers()) && field.getType().isAnnotationPresent(Localized.class);
//                }
//            });
//            for (Field field : fields) {
//                localizedFields.putAll(loadForEntity(getValue(entity, field)));
//            }
//
//            // ...now handle all collections that might have embedded LocalizedText.
//            fields = ReflectionUtils.getAllFields(entity.getClass(), new Predicate() {
//                @Override public boolean apply(Object input) {
//                    Field field = (Field) input;
//                    if (Modifier.isStatic(field.getModifiers())) return false;
//                    return isCollectionOfLocalizedEntities(field.getGenericType());
//                }
//            });
//            for (Field field : fields) {
//                localizedFields.putAll(loadForEntities(getValues(entity, field)));
//            }
            return localizedFields;
        }

//        private boolean isCollectionOfLocalizedEntities(Type type) {
//            if (type instanceof ParameterizedType ) {
//                Type rawType = ((ParameterizedType) type).getRawType();
//                if (rawType instanceof Class && ((Class) rawType).isAssignableFrom(Collection.class)) {
//                    ParameterizedType pt = (ParameterizedType) type;
//                    Type[] fieldArgTypes = pt.getActualTypeArguments();
//                    for(Type fieldArgType : fieldArgTypes) {
//                        if (fieldArgType instanceof Class<?>) {
//                            Class<?> fieldClass = (Class<?>) fieldArgType;
//                            return fieldClass.isAnnotationPresent(Localized.class);
//                        }
//                    }
//                }
//            }
//            return false;
//        }

        private Collection<?> getValues(Object entity, final Field field) {
            Object value = getValue(entity, field);
            if (value instanceof Collection) {
                return (Collection)value;
            }
            throw new IllegalStateException("trying to get collection when field returns " + value==null?" <null>" : value.getClass().getSimpleName());
        }

//        private LocalizedText getLocalizedTextValue(Object entity, final Field field) {
//            Object value = getValue(entity, field);
//            if (value instanceof LocalizedText) {
//                return (LocalizedText)value;
//            }
//            throw new IllegalStateException("trying to get " + LocalizedText.class.getSimpleName() + " when field returns " + value==null?" <null>" : value.getClass().getSimpleName());
//        }

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
//            IModel<?> model = LocalizationPanel.this.getDefaultModel();
//            if (model!=null) {
//                model.detach();
//            }
        }
    }


    class LocalizedField implements Serializable {
        private String defaultValue;
        private String name;
        private final Object entity;
        private List<String> values = new ArrayList<String>(Collections.nCopies(getLanguages().size(),(String)null));

        public LocalizedField(Object entity, String name, String defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.entity = entity;
        }

        public LocalizedField addTranslation(Translation translation) {
            Locale locale = StringUtils.parseLocaleString(translation.getId().getLanguage());
            int index = getLanguages().indexOf(locale);
            if (index<0) {
                logger.error("language of translation " + translation + " is not supported in list " + getLanguages());
            } else {
                values.set(index, translation.getValue());
            }
            return this;
        }

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }

        @Override
        public String toString() {
            return defaultValue + ':' + values;
        }

        public String getName() {
            return name;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }


}


/**
 *  use @Type for strings.
 *  will set the localized view.
 *  optional getLocalizedName() methods can be added.
 *  remove @TypeDef, use explicit.
 *  editor needs to search for @typedef's, not @localized
 *  use hibernate columns for creating keys.
 */
