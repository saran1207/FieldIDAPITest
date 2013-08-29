package com.n4systems.fieldid.wicket.components.localization;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.model.localization.Translation;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.persistence.localization.LocalizedText;
import com.n4systems.services.localization.LocalizationService;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.reflections.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.reflections.ReflectionUtils.withAnnotation;

public class LocalizationPanel extends Panel {

    private static final Logger logger= Logger.getLogger(LocalizationPanel.class);

    private @SpringBean LocalizationService localizationService;

    // TODO DD : get these values from proper source.
    private List<String> languages = Lists.newArrayList("fr", "de", "it");
    private final LocalizedFieldsModel localizedFields;
    private String language = languages.get(0);


    public LocalizationPanel(String id, IModel<?> model) {
        super(id, model);

        add(new Form("form")
                .add(new ListView<LocalizedField>("values", localizedFields = new LocalizedFieldsModel()) {
                    @Override
                    protected void populateItem(ListItem<LocalizedField> item) {
                        LocalizedField field = item.getModelObject();
                        item.add(new Label("label", Model.of(field.getName())));
                        item.add(new Label("defaultValue", Model.of(field.getDefaultValue())));
                        item.add(new TranslationsListView("translations", new PropertyModel(item.getModel(), "values")));
                    }
                })
                .add(new FidDropDownChoice<String>("language", new PropertyModel(this, "language"), languages).setNullValid(false).setRequired(true))
                .add(new AjaxSubmitLink("submit") {
                    @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        List<LocalizedField> fields = localizedFields.getObject();
                        for (LocalizedField field:fields) {
                            System.out.println(field);
                        }
                    }
                    @Override protected void onError(AjaxRequestTarget target, Form<?> form) {

                    }
                })
        );
    }


    class TranslationsListView extends ListView<String> {
        TranslationsListView(String id, IModel<List<? extends String>> list) {
            super(id, list);
        }

        @Override protected void populateItem(ListItem<String> item) {
            String translation = item.getModelObject();
            item.add(new TextField("translation", item.getModel() ));
        }
    }

    class LocalizedFieldsModel extends LoadableDetachableModel<List<LocalizedField>> {

        Set<Object> loaded;

        LocalizedFieldsModel() {
            super();
        }

        @Override
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

            // TODO DD : refactor this all into service??
            List<Translation> translations = localizationService.getTranslations(entity);
            Set<Field> fields = ReflectionUtils.getAllFields(entity.getClass(), withAnnotation(Localized.class));

            // stick in default values...
            for (Field field:fields) {
                String ognl = localizationService.getOgnlFor(entity.getClass(), field);
                LocalizedText text = getLocalizedTextValue(entity, field);
                localizedFields.put(ognl+id, new LocalizedField(field.getName(), text.getText(), ognl));
            }

            // ...now add translations (if they are there)
            for (Translation translation:translations) {
                LocalizedField localizedField = localizedFields.get(translation.getId().getOgnl());
                if (localizedField==null) {   // doesn't apply..? this is a weird state.  there is a translation for a field that isn't marked as translatable?
                    logger.warn("the translation " + translation + " exists but it doesn't seem to map to any current entity/field");
                } else {
                    localizedField.addTranslation(translation);
                }
            }

            // .. now scan for embedded LocalizedText fields.
            fields = ReflectionUtils.getAllFields(entity.getClass(), new Predicate() {
                @Override public boolean apply(Object input) {
                    Field field = (Field) input;
                    return !Modifier.isStatic(field.getModifiers()) && field.getType().isAnnotationPresent(Localized.class);
                }
            });
            for (Field field : fields) {
                localizedFields.putAll(loadForEntity(getValue(entity, field)));
            }

            // ...now handle all collections that might have embedded LocalizedText.
            fields = ReflectionUtils.getAllFields(entity.getClass(), new Predicate() {
                @Override public boolean apply(Object input) {
                    Field field = (Field) input;
                    if (Modifier.isStatic(field.getModifiers())) return false;
                    return isCollectionOfLocalizedEntities(field.getGenericType());
                }
            });
            for (Field field : fields) {
                localizedFields.putAll(loadForEntities(getValues(entity, field)));
            }
            return localizedFields;
        }

        private boolean isCollectionOfLocalizedEntities(Type type) {
            if (type instanceof ParameterizedType ) {
                Type rawType = ((ParameterizedType) type).getRawType();
                if (rawType instanceof Class && ((Class) rawType).isAssignableFrom(Collection.class)) {
                    ParameterizedType pt = (ParameterizedType) type;
                    Type[] fieldArgTypes = pt.getActualTypeArguments();
                    for(Type fieldArgType : fieldArgTypes) {
                        if (fieldArgType instanceof Class<?>) {
                            Class<?> fieldClass = (Class<?>) fieldArgType;
                            return fieldClass.isAnnotationPresent(Localized.class);
                        }
                    }
                }
            }
            return false;
        }

        private Collection<?> getValues(Object entity, final Field field) {
            Object value = getValue(entity, field);
            if (value instanceof Collection) {
                return (Collection)value;
            }
            throw new IllegalStateException("trying to get collection when field returns " + value==null?" <null>" : value.getClass().getSimpleName());
        }

        private LocalizedText getLocalizedTextValue(Object entity, final Field field) {
            Object value = getValue(entity, field);
            if (value instanceof LocalizedText) {
                return (LocalizedText)value;
            }
            throw new IllegalStateException("trying to get " + LocalizedText.class.getSimpleName() + " when field returns " + value==null?" <null>" : value.getClass().getSimpleName());
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

    }


    class LocalizedField implements Serializable {
        private String ognl;
        private String defaultValue;
        private String name;
        private List<String> values = new ArrayList<String>(Collections.nCopies(languages.size(),(String)null));

        public LocalizedField(String name, String defaultValue, String ognl) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.ognl = ognl;
        }

        public LocalizedField addTranslation(Translation translation) {
            int index = languages.indexOf(translation.getId().getLanguage());
            if (index<0) {
                logger.error("language of translation " + translation + " is not supported in list " + languages);
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
