package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetImportPage;
import com.n4systems.fieldid.wicket.pages.autoattributes.AutoAttributeActionsPage;
import com.n4systems.fieldid.wicket.pages.customers.CustomerActionsPage;
import com.n4systems.fieldid.wicket.pages.event.EventImportPage;
import com.n4systems.fieldid.wicket.pages.setup.user.UserImportPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;

public class ImportPage extends SetupPage {

    public ImportPage() {
        add(new AjaxLink<CustomerActionsPage>("importOwnersButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getRequestCycle().setResponsePage(CustomerActionsPage.class);
            }
        });
        add(new AjaxLink<AssetImportPage>("importAssetsButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getRequestCycle().setResponsePage(AssetImportPage.class);
            }
        });
        add(new AjaxLink<EventImportPage>("importEventsButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getRequestCycle().setResponsePage(EventImportPage.class);
            }
        });
        add(new AjaxLink<UserImportPage>("importUsersButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getRequestCycle().setResponsePage(UserImportPage.class);
            }
        });
        add(new AjaxLink<AutoAttributeActionsPage>("importAutoAttributesButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getRequestCycle().setResponsePage(
                        AutoAttributeActionsPage.class,
                        PageParametersBuilder.param(
                                AutoAttributeActionsPage.INITIAL_TAB_SELECTION_KEY,
                                AutoAttributeActionsPage.SHOW_IMPORTEXPORT_PAGE));
            }
        });
    }

}
