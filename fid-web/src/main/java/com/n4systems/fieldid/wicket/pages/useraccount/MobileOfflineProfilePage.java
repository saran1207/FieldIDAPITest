package com.n4systems.fieldid.wicket.pages.useraccount;

import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class MobileOfflineProfilePage extends FieldIDFrontEndPage {

    @SpringBean
    private OfflineProfileService offlineProfileService;

    public MobileOfflineProfilePage() {
        add(new OfflineProfilePanel("offlineProfilePanel", getCurrentUser()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/buttons.css");
        response.renderCSSReference("style/newCss/user/mobile-offline.css");
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
