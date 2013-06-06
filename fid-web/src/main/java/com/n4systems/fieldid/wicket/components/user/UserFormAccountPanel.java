package com.n4systems.fieldid.wicket.components.user;

import com.n4systems.fieldid.actions.users.WelcomeMessage;
import com.n4systems.fieldid.handler.password.PasswordHelper;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class UserFormAccountPanel extends Panel {

    @SpringBean
    private TenantSettingsService tenantSettingsService;
    @SpringBean
    private UserService userService;

    private String password;
    private String confirmPassword;
    private String rfidNumber;
    private Boolean assignPassword = true;
    private WelcomeMessage welcomeMessage;

    private RequiredTextField userID;
    private PasswordTextField passwordField;
    private PasswordTextField cpasswordField;

    private WebMarkupContainer welcomeEmailMessageContainer;

    public UserFormAccountPanel(String id, final IModel<User> user, final Form form) {
        super(id, user);

        add(userID = new RequiredTextField<String>("username", new PropertyModel<String>(user, "userID")));
        userID.add(new IValidator<String>() {
            @Override
            public void validate(IValidatable validatable) {
                if(!userService.userIdIsUnique(FieldIDSession.get().getTenant().getId(), (String) validatable.getValue(), user.getObject().getId())) {
                    ValidationError error = new ValidationError();
                    error.addMessageKey("errors.data.userduplicate");
                    validatable.error(error);
                }
            }
        });

        WebMarkupContainer newAccountFields = new WebMarkupContainer("newAccountFields");

        newAccountFields.add(passwordField = new PasswordTextField("password", new PropertyModel<String>(this, "password")));
        passwordField.setOutputMarkupPlaceholderTag(true);
        newAccountFields.add(cpasswordField = new PasswordTextField("confirmPassword", new PropertyModel<String>(this, "confirmPassword")));
        cpasswordField.setOutputMarkupPlaceholderTag(true);
        newAccountFields.add(new TextField<String>("rfidNumber", new PropertyModel<String>(this, "rfidNumber")));

        final EqualPasswordInputValidator equalPasswordInputValidator = new EqualPasswordInputValidator(passwordField, cpasswordField);

        newAccountFields.add(new CheckBox("assignPassword", new PropertyModel<Boolean>(this, "assignPassword")).add(new UpdateComponentOnChange() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                passwordField.setVisible(assignPassword);
                cpasswordField.setVisible(assignPassword);
                if(assignPassword) {
                    add(equalPasswordInputValidator);
                } else {
                    remove(equalPasswordInputValidator);
                }
                target.add(passwordField, cpasswordField);
            }
        }));

        welcomeEmailMessageContainer = new WebMarkupContainer("welcomeEmailMessageContainer");

        newAccountFields.add(new CheckBox("sendWelcomeEmail", new PropertyModel<Boolean>(this, "welcomeMessage.sendEmail")).add(new UpdateComponentOnChange() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                welcomeEmailMessageContainer.setVisible(welcomeMessage.isSendEmail());
                target.add(welcomeEmailMessageContainer);
            }
        }));

        welcomeEmailMessageContainer.add(new TextArea<String>("welcomeEmailMessage", new PropertyModel<String>(this, "welcomeMessage.personalMessage")));
        welcomeEmailMessageContainer.setVisible(false);
        welcomeEmailMessageContainer.setOutputMarkupPlaceholderTag(true);
        newAccountFields.add(welcomeEmailMessageContainer);

        passwordField.add(new IValidator<String>() {
            @Override
            public void validate(IValidatable<String> validatable) {
                PasswordHelper passwordHelper = new PasswordHelper(getPasswordPolicy());
                if (!passwordHelper.isValidPassword(validatable.getValue())) {

                    PasswordPolicy policy = passwordHelper.getPasswordPolicy();
                    String message = new FIDLabelModel("error.password_policy", policy.getMinLength() + "",
                            policy.getMinCapitals() + "",
                            policy.getMinNumbers() + "",
                            policy.getMinSymbols() + "").getObject();

                    ValidationError error = new ValidationError();
                    error.setMessage(message);
                    validatable.error(error);
                }
            }
        });
        newAccountFields.setVisible(user.getObject().isNew());
        add(newAccountFields);
        add(equalPasswordInputValidator);
    }

    private PasswordPolicy getPasswordPolicy() {
        return tenantSettingsService.getTenantSettings().getPasswordPolicy();
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRfidNumber() {
        return rfidNumber;
    }

    public void setRfidNumber(String rfidNumber) {
        this.rfidNumber = rfidNumber;
    }

    public Boolean isAssignPassword() {
        return assignPassword;
    }

    public void setAssignPassword(Boolean assignPassword) {
        this.assignPassword = assignPassword;
    }

    public WelcomeMessage getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(WelcomeMessage welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }
}
