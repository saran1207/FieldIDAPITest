package com.n4systems.fieldid.wicket.pages.useraccount.changepassword;

import com.n4systems.fieldid.handler.password.PasswordHelper;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.user.User;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class UserAccountFormPanel extends Panel {

    @SpringBean private TenantSettingsService tenantSettingsService;
    @SpringBean private UserService userService;

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    private PasswordTextField currentPasswordField;
    private PasswordTextField newPasswordField;
    private PasswordTextField confirmPasswordField;

    public UserAccountFormPanel(String id, final IModel<User> user, Form form) {
        super(id, user);

        add(new Label("username", new PropertyModel<String>(user, "userID")));

        WebMarkupContainer newAccountFields = new WebMarkupContainer("newAccountFields");

        newAccountFields.add(currentPasswordField = new PasswordTextField("currentPassword", new PropertyModel<String>(this, "newPassword")));
        currentPasswordField.add(new IValidator<String>() {
            @Override
            public void validate(IValidatable validatable) {
                if(userService.authenticateUserByPassword(FieldIDSession.get().getTenant().getName(),user.getObject().getUserID(),(String) validatable.getValue()) == null) {
                    ValidationError error = new ValidationError();
                    error.addMessageKey("errors.data.userpassword");
                    validatable.error(error);
                }
            }
        });
        currentPasswordField.add(new Behavior() {
            @Override public void onComponentTag(Component c, ComponentTag tag) {
                FormComponent fc = (FormComponent) c;
                if (!fc.isValid()) {
                    tag.append("class", "error", " ");
                }
            }
        });
        currentPasswordField.setOutputMarkupPlaceholderTag(true);

        newAccountFields.add(newPasswordField = new PasswordTextField("newPassword", new PropertyModel<String>(this, "newPassword")));
        newPasswordField.add(new Behavior() {
            @Override public void onComponentTag(Component c, ComponentTag tag) {
                FormComponent fc = (FormComponent) c;
                if (!fc.isValid()) {
                    tag.append("class", "error", " ");
                }
            }
        });
        newPasswordField.setOutputMarkupPlaceholderTag(true);
        newAccountFields.add(confirmPasswordField = new PasswordTextField("confirmPassword", new PropertyModel<String>(this, "confirmPassword")));

        confirmPasswordField.add(new Behavior() {
            @Override public void onComponentTag(Component c, ComponentTag tag) {
                FormComponent fc = (FormComponent) c;
                if (!fc.isValid()) {
                    tag.append("class", "error", " ");
                }
            }
        });
        confirmPasswordField.setOutputMarkupPlaceholderTag(true);

        final EqualPasswordInputValidator equalPasswordInputValidator = new EqualPasswordInputValidator( newPasswordField, confirmPasswordField);

        newPasswordField.add(new IValidator<String>() {
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
        add(newAccountFields);
        form.add(equalPasswordInputValidator);
    }

    private PasswordPolicy getPasswordPolicy() {
        return tenantSettingsService.getTenantSettings().getPasswordPolicy();
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
