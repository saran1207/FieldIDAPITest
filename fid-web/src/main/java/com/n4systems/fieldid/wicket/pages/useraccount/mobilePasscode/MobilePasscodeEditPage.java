package com.n4systems.fieldid.wicket.pages.useraccount.mobilePasscode;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.useraccount.AccountSetupPage;
import com.n4systems.fieldid.wicket.validators.UniqueUserMobilePasscodeValidator;
import com.n4systems.model.user.User;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Created by agrabovskis on 2019-02-08.
 */
public class MobilePasscodeEditPage extends AccountSetupPage {

    protected static final Logger logger = Logger.getLogger(MobilePasscodeEditPage.class);

    @SpringBean
    private UserService userService;

    public MobilePasscodeEditPage() {
        super();
        addComponents();
    }

    private void addComponents() {

        final IModel<String> securityCardNumberModel = Model.of("");
        TextField securityCardNumberField = new TextField("securityCardNumber", securityCardNumberModel);
        securityCardNumberField.setRequired(true);
        securityCardNumberField.add(new StringValidator.MinimumLengthValidator(4));
        securityCardNumberField.add(new UniqueUserMobilePasscodeValidator(userService, getTenantId(), getCurrentUser().getId()));

        Form form = new Form("form") {
            @Override
            protected void onSubmit() {
                updateSecurityCardNumber(securityCardNumberModel);
                setResponsePage(MobilePasscodeSetupPage.class);
            }
        };
        add(form);

        form.add(securityCardNumberField);
        form.add(new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(MobilePasscodeSetupPage.class);
            }
        });
        
    }

    private void updateSecurityCardNumber(IModel<String> securityCardNumberModel) {
        User user = getCurrentUser();
        user.assignSecruityCardNumber(securityCardNumberModel.getObject());
        userService.update(user);
        logger.info("mobile passcode updated for " + getSessionUser().getUserID());
        info(new FIDLabelModel("message.passcodeupdated").getObject());
    }

}
