package com.n4systems.fieldid.wicket.components.localization;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;
import java.util.Locale;

public class SelectLanguagePanel extends Panel {

    private Locale language;
    private Form form;
    private FormComponent<Locale> chooseLanguage;

    public SelectLanguagePanel(String id) {
        super(id);

        add(form = new Form<Void>("form"));
        language = FieldIDSession.get().getUserLocale();
        form.add(chooseLanguage = new FidDropDownChoice<Locale>("language", new PropertyModel(this, "language"), getLanguages()).setNullValid(false).setRequired(true));
        chooseLanguage.add(new UpdateComponentOnChange());

        form.add(new AjaxSubmitLink("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                FieldIDSession.get().setUserLocale(language);
                onLanguageSelection(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });
    }

    public void onLanguageSelection(AjaxRequestTarget target) {}

    private List<Locale> getLanguages() {
        return FieldIDSession.get().getTenant().getSettings().getLanguages();
    }
}
