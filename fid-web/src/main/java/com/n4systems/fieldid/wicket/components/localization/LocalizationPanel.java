package com.n4systems.fieldid.wicket.components.localization;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.model.localization.Translation;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.persistence.localization.Localized;
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
import java.util.*;

import static org.reflections.ReflectionUtils.withAnnotation;

public class LocalizationPanel extends Panel {

    private static final Logger logger= Logger.getLogger(LocalizationPanel.class);

    private @SpringBean LocalizationService localizationService;

    // TODO DD : get these values from proper source.
    private List<String> languages = Lists.newArrayList("fr", "de", "it");
    private final LocalizedFieldsModel localizedFields;
    private String language = languages.get(0);


    public LocalizationPanel(String id, IModel<? extends EntityWithTenant> model) {
        super(id, model);
        Preconditions.checkArgument(model.getObject().getClass().isAnnotationPresent(Localized.class));

        add(new Form("form")
                .add(new ListView<LocalizedField>("values", localizedFields = new LocalizedFieldsModel(model)) {
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

        private final IModel<? extends EntityWithTenant> model;

        LocalizedFieldsModel(IModel<? extends EntityWithTenant> model) {
            super();
            this.model = model;
        }

        @Override
        protected List<LocalizedField> load() {
            Map<String,LocalizedField> localizedFields = Maps.newHashMap();

            List<Translation> translations = localizationService.getTranslations(model.getObject());

            Set<Field> fields = ReflectionUtils.getAllFields(model.getObject().getClass(), withAnnotation(Localized.class));
            // stick in default values....
            for (Field field:fields) {
                String ognl = localizationService.getOgnlFor(model.getObject().getClass(), field);
                localizedFields.put(ognl, new LocalizedField(field.getName(), new PropertyModel<String>(model, field.getName()).getObject()));
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
            return Lists.newArrayList(localizedFields.values());
        }
    }


    class LocalizedField implements Serializable {
        private String defaultValue;
        private String name;
        private List<String> values = new ArrayList<String>(Collections.nCopies(languages.size(),(String)null));

        public LocalizedField(String name, String defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
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
