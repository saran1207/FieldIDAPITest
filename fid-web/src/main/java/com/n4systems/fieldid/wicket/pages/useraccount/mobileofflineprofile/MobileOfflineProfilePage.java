package com.n4systems.fieldid.wicket.pages.useraccount.mobileofflineprofile;

import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.useraccount.AccountSetupPage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class MobileOfflineProfilePage extends AccountSetupPage {

    @SpringBean
    private OfflineProfileService offlineProfileService;

    public MobileOfflineProfilePage() {
        add(new OfflineProfilePanel("offlineProfilePanel", getCurrentUser()));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.my_account_offline_profile"));
    }

}
