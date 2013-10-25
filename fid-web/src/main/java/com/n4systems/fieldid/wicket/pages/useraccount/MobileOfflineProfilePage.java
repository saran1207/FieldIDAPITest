package com.n4systems.fieldid.wicket.pages.useraccount;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.useraccount.table.AssetProfileLinkCell;
import com.n4systems.fieldid.wicket.pages.useraccount.table.RemoveActionCell;
import com.n4systems.model.Asset;
import com.n4systems.model.offlineprofile.OfflineProfile;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class MobileOfflineProfilePage extends FieldIDFrontEndPage {

    @SpringBean
    private OfflineProfileService offlineProfileService;

    public MobileOfflineProfilePage() {
        add(new OfflineProfilePanel("offlineProfilePanel", getCurrentUser()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.my_account_offline_profile"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.details").page("myAccount.action").build(),
                aNavItem().label("nav.notification_settings").page("notificationSettings.action").build(),
                aNavItem().label("nav.change_password").page("editPassword.action").build(),
                aNavItem().label("nav.mobile_passcode").page("viewMobilePasscode.action").build(),
                aNavItem().label("nav.mobile_profile").page(MobileOfflineProfilePage.class).build(),
                aNavItem().label("nav.downloads").page("showDownloads.action").build(),
                aNavItem().label("nav.excel_export").page("exportEvent.action").build()
        ));
    }
}
