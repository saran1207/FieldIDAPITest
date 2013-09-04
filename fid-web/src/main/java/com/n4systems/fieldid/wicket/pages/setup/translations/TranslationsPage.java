package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

abstract public class TranslationsPage extends FieldIDFrontEndPage {

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

}
