package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.wicket.components.localization.LocalizationPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

abstract public class TranslationsPage<T extends EntityWithTenant> extends FieldIDFrontEndPage {

    private final Component choice;
    private final LocalizationPanel localizationPanel;

    protected TranslationsPage() {
        super();
        add(new Form("form").
                add(choice = createChoice("choice").add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override protected void onUpdate(AjaxRequestTarget target) {
                        selected(target);
                    }
                })
                ));
        choice.setOutputMarkupPlaceholderTag(true);
        add(localizationPanel = new LocalizationPanel("localization",null));
        localizationPanel.setVisible(false);
        localizationPanel.setOutputMarkupPlaceholderTag(true);
    }

    protected abstract DropDownChoice<T> createChoice(String choice);

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.translations"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("title.manage_asset_type_groups.singular").page(AssetTypeGroupTranslationsPage.class).build(),
                aNavItem().label("title.asset_type").page(AssetTypeTranslationsPage.class).build(),
                aNavItem().label("label.eventtypegroup").page(EventTypeGroupTranslationsPage.class).build(),
                aNavItem().label("label.event_type").page(EventTypeTranslationsPage.class).build(),
                aNavItem().label("label.eventbook").page(EventBookTranslationsPage.class).build()
        ));
    }

    protected void selected(AjaxRequestTarget target) {
        Object modelObject = choice.getDefaultModelObject();
        EntityWithTenant entity = (EntityWithTenant) modelObject;
        localizationPanel.setVisible(true);
        localizationPanel.setDefaultModel(new EntityModel(entity.getClass(), entity));
        target.add(localizationPanel);
    }

}
