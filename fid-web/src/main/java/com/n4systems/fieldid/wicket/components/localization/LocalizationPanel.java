package com.n4systems.fieldid.wicket.components.localization;

import com.google.common.base.CaseFormat;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.api.HasTenant;
import com.n4systems.model.localization.Translation;
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
import org.apache.wicket.markup.html.form.TextArea;
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

    public static final int ONE_LINE_OF_CHARACTERS = 100;

    private @SpringBean LocalizationService localizationService;

    private Locale language;
    private final FormComponent<Locale> chooseLanguage;
    private final ListView<LocalizedField> listView;
    private IModel<List<Locale>> languages = null;

    private IModel<?> model;


    public LocalizationPanel(String id, final IModel<? extends HasTenant> model) {
        this(id,model,new PropertyModel(model,"tenant.settings.translatedLanguages"));
    }


    public LocalizationPanel(String id, final IModel<?> model, final IModel<List<Locale>> languages) {
        super(id, model);
        this.languages = languages;
        this.model = model;
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
                        item.add(createLinksForItem("misc", item, languages));
                    }
                }.setReuseItems(true))
                .add(chooseLanguage = new FidDropDownChoice<Locale>("language", new PropertyModel(this, "language"), getLanguages() , new IChoiceRenderer<Locale>() {
                    @Override public Object getDisplayValue(Locale object) {
                        return object.getDisplayName();
                    }

                    @Override public String getIdValue(Locale object, int index) {
                        return object.toString();
                    }
                }).setNullValid(false).setRequired(true))
                .add(new AjaxSubmitLink("submit") {
                    @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        onLocalizationsSaved(target);
                        localizationService.save(convertToTranslations());
                        forceReload();
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
        if(!getLanguages().getObject().isEmpty())
            language = getLanguages().getObject().get(0);
    }

    protected void onLocalizationsSaved(AjaxRequestTarget target) {
    }

    private void forceReload() {
        LocalizedFieldsModel model = (LocalizedFieldsModel) listView.getModel();
        model.forceReload();
    }

    protected boolean forceDefaultLanguage() {
        return true;
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
        return new LocalizedFieldsModel(LocalizationPanel.this.getDefaultModel(), getLanguages().getObject()) {
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

    protected Component createLinksForItem(String misc, ListItem<LocalizedField> item, IModel<List<Locale>> languages ) {
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

    protected IModel<List<Locale>> getLanguages() {
        return languages;
    }

    class TranslationsListView extends ListView<Translation> {
        TranslationsListView(String id, IModel<List<Translation>> list) {
            super(id, list);
            setReuseItems(true);
        }

        @Override protected void populateItem(ListItem<Translation> item) {
            final Locale itemLanguage = getLanguages().getObject().get(item.getIndex());
            TextArea text;
            item.add(text = new TextArea("translation", new PropertyModel(item.getModel(),"value")) {
                @Override public boolean isVisible() {
                    return itemLanguage.equals(language);
                }
            });
            String value = text.getDefaultModelObjectAsString();
            if (value!=null&&value.length()> ONE_LINE_OF_CHARACTERS) {
                text.add(new AttributeAppender("class", "large"));
            }
        }
    }


}
