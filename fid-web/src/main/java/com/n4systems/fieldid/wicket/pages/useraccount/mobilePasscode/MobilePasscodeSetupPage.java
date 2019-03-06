package com.n4systems.fieldid.wicket.pages.useraccount.mobilePasscode;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.useraccount.AccountSetupPage;
import com.n4systems.model.user.User;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by agrabovskis on 2019-02-07.
 */
public class MobilePasscodeSetupPage extends AccountSetupPage {

    protected static final Logger logger = Logger.getLogger(MobilePasscodeSetupPage.class);

    @SpringBean
    private UserService userService;

    public MobilePasscodeSetupPage() {
        super();
        addComponents();
    }

    private void addComponents() {

        WebMarkupContainer createMobilePasscodeSection = new WebMarkupContainer("createMobilePasscode") {
            @Override
            public boolean isVisible() {
                return !isMobilePasscodeSet();
            }
        };
        add(createMobilePasscodeSection);

        WebMarkupContainer modifyMobilePasscodeSection = new WebMarkupContainer("modifyMobilePasscode") {
            @Override
            public boolean isVisible() {
                return isMobilePasscodeSet();
            }
        };
        add(modifyMobilePasscodeSection);

        Link createMobilePasscodeButton = new Link("createMobilePasscode") {
            @Override
            public void onClick() {
                setResponsePage(MobilePasscodeEditPage.class);
            }
        };
        createMobilePasscodeSection.add(createMobilePasscodeButton);

        Link removeMobilePasscodeButton = new Link("removeMobilePasscode") {
            @Override
            public void onClick() {
               removeMobilePasscode();
            }
        };
        modifyMobilePasscodeSection.add(removeMobilePasscodeButton);

        Link editMobilePasscodeButton = new Link("editMobilePasscode") {
            @Override
            public void onClick() {
                setResponsePage(MobilePasscodeEditPage.class);
            }
        };
        modifyMobilePasscodeSection.add(editMobilePasscodeButton);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("nav.mobile_passcode"));
    }

    private boolean isMobilePasscodeSet() {
        User user = getCurrentUser();
        return user.getHashSecurityCardNumber() != null && !user.getHashSecurityCardNumber().isEmpty();
    }

    private void removeMobilePasscode() {
        User user = getCurrentUser();
        user.assignSecruityCardNumber(null);
        userService.update(user);
        logger.info("mobile passcode number removed for " + user.getUserID());
        info(new FIDLabelModel("message.passcoderemoved").getObject());
     }
}
