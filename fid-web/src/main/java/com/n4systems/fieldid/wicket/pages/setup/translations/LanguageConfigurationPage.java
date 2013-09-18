package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.MultiSelectDropDownChoice;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.tenant.TenantSettings;
import org.apache.wicket.markup.html.IHeaderResponse;
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
                FieldIDSession.get().info("Updated languages");
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
        return Lists.newArrayList(Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, new Locale("es"), new Locale("da"), new Locale("sv"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("title.manage_asset_type_groups.singular").page(AssetTypeGroupTranslationsPage.class).build(),
                aNavItem().label("title.asset_type").page(AssetTypeTranslationsPage.class).build(),
                aNavItem().label("label.eventtypegroup").page(EventTypeGroupTranslationsPage.class).build(),
                aNavItem().label("label.event_type").page(EventTypeTranslationsPage.class).build(),
                aNavItem().label("label.eventbook").page(EventBookTranslationsPage.class).build(),
                aNavItem().label("label.assetstatus").page(AssetStatusTranslationsPage.class).build(),
                aNavItem().label("label.event_status").page(EventStatusTranslationsPage.class).build(),
                aNavItem().label("label.configure_languages").page(LanguageConfigurationPage.class).onRight().build()
        ));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/buttons.css");
        response.renderCSSReference("style/pageStyles/localization.css");
    }
}
