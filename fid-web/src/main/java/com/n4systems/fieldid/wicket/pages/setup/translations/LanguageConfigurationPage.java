package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.MultiSelectDropDownChoice;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.tenant.TenantSettings;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Locale;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class LanguageConfigurationPage extends FieldIDFrontEndPage {

    @SpringBean
    private TenantSettingsService tenantSettingsService;

    private IModel<TenantSettings> tenantSettingsModel;

    public LanguageConfigurationPage() {

        tenantSettingsModel = Model.of(getTenant().getSettings());
        Form form;
        add(form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                tenantSettingsService.update(tenantSettingsModel.getObject());
                FieldIDSession.get().info(new FIDLabelModel("mesasge.updated_configured_languages").getObject());
                setResponsePage(getPageClass());
            }
        });

        form.add(new MultiSelectDropDownChoice<Locale>("languages",
                new PropertyModel<List<Locale>>(tenantSettingsModel, "translatedLanguages"),
                getAvailableLanguages(),
                new IChoiceRenderer<Locale>() {
                    @Override
                    public Object getDisplayValue(Locale locale) {
                        return locale.getDisplayLanguage();
                    }

                    @Override
                    public String getIdValue(Locale locale, int index) {
                        return index+"";
                    }
                }));
        form.add(new SubmitLink("save"));

    }

    public List<Locale> getAvailableLanguages() {
        return Lists.newArrayList(
                new Locale("da"), //Danish
                Locale.FRENCH,
                Locale.GERMAN,
                Locale.ITALIAN,
                new Locale("lv"),  //Latvian
                new Locale("es"), //Spanish
                new Locale("sv") //Swedish
        );
    }

    @Override
    protected void addNavBar(String navBarId) {
        boolean hasTranslatedLanguages = !getTenant().getSettings().getTranslatedLanguages().isEmpty();
        add(new NavigationBar(navBarId,
                aNavItem().label("title.manage_asset_type_groups.singular").cond(hasTranslatedLanguages).page(AssetTypeGroupTranslationsPage.class).build(),
                aNavItem().label("title.asset_type").cond(hasTranslatedLanguages).page(AssetTypeTranslationsPage.class).build(),
                aNavItem().label("label.eventtypegroup").cond(hasTranslatedLanguages).page(EventTypeGroupTranslationsPage.class).build(),
                aNavItem().label("label.event_type").cond(hasTranslatedLanguages).page(EventTypeTranslationsPage.class).build(),
                aNavItem().label("label.eventbook").cond(hasTranslatedLanguages).page(EventBookTranslationsPage.class).build(),
                aNavItem().label("label.assetstatus").cond(hasTranslatedLanguages).page(AssetStatusTranslationsPage.class).build(),
                aNavItem().label("label.event_status").cond(hasTranslatedLanguages).page(EventStatusTranslationsPage.class).build(),
                aNavItem().label("label.configure_languages").page(LanguageConfigurationPage.class).onRight().build()
        ));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/buttons.css");
        response.renderCSSReference("style/pageStyles/localization.css");
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.translations"));
    }
}
