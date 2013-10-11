package com.n4systems.fieldid.wicket.pages.admin.languages;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.model.localization.Language;
import com.n4systems.services.localization.LocalizationService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ConfigureLanguagesPage extends FieldIDAdminPage {

    @SpringBean
    private LocalizationService localizationService;

    private String code;
    private RequiredTextField<String> codeField;
    private FIDFeedbackPanel feedbackPanel;
    private WebMarkupContainer languageList;

    public ConfigureLanguagesPage() {

        add(languageList = new WebMarkupContainer("languageList"));
        languageList.setOutputMarkupId(true);
        languageList.add(new ListView<Language>("language", getSystemLanguages()) {
            @Override
            protected void populateItem(final ListItem<Language> item) {
                item.add(new Label("code", new PropertyModel<String>(item.getModel(), "locale.language")));
                item.add(new Label("displayName", new PropertyModel<String>(item.getModel(), "displayLanguage")));
                item.add(new AjaxLink<Void>("remove") {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        localizationService.removeSystemLanguage(item.getModelObject());
                        languageList.detach();
                        target.add(languageList);
                    }
                });
            }
        });

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupId(true);

        Form<Void> form;
        add(form = new Form<Void>("form"));

        form.add(codeField = new RequiredTextField<String>("code", new PropertyModel<String>(this, "code")));

        codeField.add(new AbstractValidator<String>() {
            @Override
            protected void onValidate(IValidatable<String> validatable) {
                String languageCode = validatable.getValue();
                if(Lists.newArrayList(Locale.getISOLanguages()).contains(languageCode)) {
                    Locale locale = new Locale(languageCode);
                    if(localizationService.hasSystemLanguage(locale)) {
                        ValidationError error = new ValidationError();
                        error.addMessageKey("error.language_exists");
                        validatable.error(error);
                    }
                } else {
                    ValidationError error = new ValidationError();
                    error.addMessageKey("error.invalid_language_code");
                    validatable.error(error);
                }
            }
        });

        form.add(new AjaxSubmitLink("add") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Locale locale = new Locale(code);
                localizationService.addSystemLanguage(new Language(locale));
                code = null;
                codeField.clearInput();
                target.add(languageList, feedbackPanel);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });
    }

    private LoadableDetachableModel<List<Language>> getSystemLanguages() {
        return new LoadableDetachableModel<List<Language>>() {
            @Override
            protected List<Language> load() {
                List<Language> languageList = localizationService.getSystemLanguages();
                Collections.sort(languageList);
                return languageList;
            }
        };
    }
}
