package com.n4systems.fieldid.wicket.pages.admin.languages;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
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
import org.apache.wicket.markup.html.form.TextField;
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

    private String language;
    private String country;
    private RequiredTextField<String> languageField;
    private TextField<String> countryField;
    private FIDFeedbackPanel feedbackPanel;
    private WebMarkupContainer languageList;

    public ConfigureLanguagesPage() {

        add(languageList = new WebMarkupContainer("languageList"));
        languageList.setOutputMarkupId(true);
        languageList.add(new ListView<Language>("language", getSystemLanguages()) {
            @Override
            protected void populateItem(final ListItem<Language> item) {
                item.add(new Label("code", new PropertyModel<String>(item.getModel(), "locale.language")));
                item.add(new Label("displayName", new PropertyModel<String>(item.getModel(), "displayName")));
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
        add(form = new Form<Void>("form") {
            @Override
            protected void onValidate() {
                String language = languageField.getRawInput();
                String country = countryField.getRawInput();

                if (Lists.newArrayList(Locale.getISOLanguages()).contains(language)) {

                    Locale locale = null;

                    if (country != null) {
                        if (Lists.newArrayList(Locale.getISOCountries()).contains(country)) {
                            locale = new Locale(language, country);
                        } else {
                            error(new FIDLabelModel("error.invalid_country_code").getObject());
                        }
                    } else {
                        locale = new Locale(language);
                    }
                    if (locale != null && localizationService.hasSystemLanguage(locale)) {
                        error(new FIDLabelModel("error.language_exists").getObject());
                    }
                } else {
                    error(new FIDLabelModel("error.invalid_language_code").getObject());
                }
            }
        });

        form.add(languageField = new RequiredTextField<>("language", new PropertyModel<>(this, "language")));
        form.add(countryField = new TextField<>("country", new PropertyModel<>(this, "country")));

        form.add(new AjaxSubmitLink("add") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Locale locale;
                if (country == null) {
                    locale = new Locale(language);
                } else {
                    locale = new Locale(language,country);
                }
                localizationService.addSystemLanguage(new Language(locale));
                language = null;
                languageField.clearInput();
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
