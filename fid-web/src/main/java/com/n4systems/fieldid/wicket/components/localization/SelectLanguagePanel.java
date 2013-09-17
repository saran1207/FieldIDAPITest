package com.n4systems.fieldid.wicket.components.localization;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Locale;

public class SelectLanguagePanel extends Panel {

    private Locale language;
    private Form form;
    private FidDropDownChoice<Locale> chooseLanguage;

    @SpringBean
    UserService userService;

    public SelectLanguagePanel(String id) {
        super(id);

        add(form = new Form<Void>("form"));
        language = FieldIDSession.get().getUserLocale();
        form.add(chooseLanguage = new FidDropDownChoice<Locale>("language", new PropertyModel<Locale>(this, "language"), getLanguages(), new IChoiceRenderer<Locale>() {
            @Override
            public Object getDisplayValue(Locale locale) {
                Locale userLocale = FieldIDSession.get().getUserLocale();
                if(userLocale != null)
                    return locale.getDisplayLanguage(userLocale);
                else
                    return locale.getDisplayLanguage();
            }

            @Override
            public String getIdValue(Locale locale, int index) {
                return index+"";
            }
        }));
        chooseLanguage.setNullValid(false);
        chooseLanguage.setRequired(true);
        chooseLanguage.add(new UpdateComponentOnChange());

        form.add(new AjaxSubmitLink("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                FieldIDSession.get().setUserLocale(language);

                User currentUser = userService.getUser(FieldIDSession.get().getSessionUser().getId());
                currentUser.setLanguage(language);
                userService.update(currentUser);

                onLanguageSelection(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });
    }

    public void onLanguageSelection(AjaxRequestTarget target) {}

    private List<Locale> getLanguages() {
        TenantSettings tenantSettings = FieldIDSession.get().getTenant().getSettings();
        List<Locale> languages = Lists.newArrayList(tenantSettings.getTranslatedLanguages());
        languages.add(0, tenantSettings.getDefaultLanguage());
        return languages;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/newCss/component/buttons.css");
        response.renderCSSReference("style/modal/select-language.css");
    }
}
