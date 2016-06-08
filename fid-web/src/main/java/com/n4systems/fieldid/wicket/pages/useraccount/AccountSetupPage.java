package com.n4systems.fieldid.wicket.pages.useraccount;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.useraccount.notificationsettings.NotificationSettingsListPage;
import com.n4systems.fieldid.wicket.pages.useraccount.mobileofflineprofile.MobileOfflineProfilePage;
import com.n4systems.model.ExtendedFeature;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public abstract class AccountSetupPage extends FieldIDTemplatePage {

    public AccountSetupPage() {
    }

    public AccountSetupPage(PageParameters params) {
        super(params);
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.details").page("myAccount.action").build(),
                aNavItem().label("nav.notification_settings").page(NotificationSettingsListPage.class).cond(hasEmailAlerts()).build(),
                aNavItem().label("nav.change_password").page("editPassword.action").build(),
                aNavItem().label("nav.mobile_passcode").page("viewMobilePasscode.action").build(),
                aNavItem().label("nav.mobile_profile").page(MobileOfflineProfilePage.class).build(),
                aNavItem().label("nav.downloads").page("showDownloads.action").build(),
                aNavItem().label("nav.excel_export").page("exportEvent.action").build()
        ));
    }

    protected boolean hasEmailAlerts() {
        return FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.EmailAlerts);
    }
}
