package com.n4systems.fieldid.wicket.components.localization;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.localization.Translation;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.services.localization.LocalizationService;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocalizationPanel extends Panel {

    private static final Logger logger= Logger.getLogger(LocalizationPanel.class);

    private @SpringBean LocalizationService localizationService;

    private Locale language;
    private final FormComponent<Locale> chooseLanguage;
    private final ListView<LocalizedField> listView;

    public LocalizationPanel(String id, IModel<? extends EntityWithTenant> model) {
        super(id, model);
        setOutputMarkupId(true);
        add(new Form("form")
                .add(listView = new ListView<LocalizedField>("localization", createLocalizedFieldsModel()) {
                    @Override
                    protected void populateItem(ListItem<LocalizedField> item) {
                        item.add(new AttributeAppender("class", Model.of(getCssFor(item))));
                        final LocalizedField field = item.getModelObject();
                        item.add(new Label("label", Model.of(getLabelFor(field))));
                        item.add(new Label("defaultValue", Model.of(field.getDefaultValue())));
                        item.add(new TranslationsListView("translations", new PropertyModel(item.getModel(), "translations")));
                        item.add(createLinksForItem("misc", item));
                    }
                }.setReuseItems(true))
                .add(chooseLanguage = new FidDropDownChoice<Locale>("language", new PropertyModel(this, "language"), getLanguages(), new IChoiceRenderer<Locale>() {
                    @Override public Object getDisplayValue(Locale object) {
                        return object.getDisplayLanguage();
                    }

                    @Override public String getIdValue(Locale object, int index) {
                        return object.toString();
                    }
                }).setNullValid(false).setRequired(true))
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
        language = getLanguages().get(0);
    }

    protected String getLabelFor(LocalizedField field) {
        String className = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL,field.getEntity().getClass().getSimpleName());
        String label = String.format("label.%s.%s", className,field.getName());
        if (field.getOgnl().contains("@")) {
            int i = field.getOgnl().lastIndexOf(".");    // e.g. criteria.recommendations@    -->    label.recommendations.
            int j = field.getOgnl().lastIndexOf("@");
            label = "label."+field.getOgnl().substring(i+1,j);
        }
        return new FIDLabelModel(label).getObject();
    }

    private LocalizedFieldsModel createLocalizedFieldsModel() {
        return new LocalizedFieldsModel(LocalizationPanel.this.getDefaultModel(), getLanguages()) {
            @Override protected boolean isFiltered(Object entity, Field field) {
                boolean result = LocalizationPanel.this.isFieldIgnored(entity,field);
                Class<?> type = field.getType();
                if (field.isAnnotationPresent(Transient.class) || Date.class.isAssignableFrom(type) || Number.class.isAssignableFrom(type) || type.isPrimitive() || type.isEnum()) {
                    return true;
                }
                if (type.equals(String.class)) {
                    result = result || !field.isAnnotationPresent(Localized.class);
                }
                return result;
            }
        };
    }

    protected Component createLinksForItem(String misc, ListItem<LocalizedField> item) {
        return new WebMarkupContainer("misc");
    }

    protected String getCssFor(ListItem<LocalizedField> item) {
        return item.getModelObject().getOgnl().replace('.', '-');
    }

    private List<Translation> convertToTranslations() {
        LocalizedFieldsModel model = (LocalizedFieldsModel) listView.getDefaultModel();
        return model.getAsTranslations();
    }

    protected boolean isFieldIgnored(Object entity, Field field) {
        return false;
    }

    @Override
    protected void onModelChanged() {
        super.onModelChanged();
        detachModel();
        listView.setDefaultModel(createLocalizedFieldsModel());
        listView.detach();
    }

    private List<Locale> getLanguages() {
        return ImmutableList.copyOf(FieldIDSession.get().getTenant().getSettings().getLanguages());
    }


    class TranslationsListView extends ListView<Translation> {
        TranslationsListView(String id, IModel<List<Translation>> list) {
            super(id, list);
            setReuseItems(true);
        }

        @Override protected void populateItem(ListItem<Translation> item) {
            final Locale itemLanguage = getLanguages().get(item.getIndex());
            item.add(new TextField("translation", new PropertyModel(item.getModel(),"value")) {
                @Override public boolean isVisible() {
                    return itemLanguage.equals(language);
                }
            });
        }
    }


}
