package com.n4systems.fieldid.wicket.pages.useraccount.changepassword;

import com.n4systems.fieldid.handler.password.PasswordHelper;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.useraccount.AccountSetupPage;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.user.User;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class ChangeUserAccountPasswordPage extends AccountSetupPage {
    private static final Logger logger = Logger.getLogger(ChangeUserAccountPasswordPage.class);
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    private PasswordTextField currentPasswordField;
    private PasswordTextField newPasswordField;
    private PasswordTextField confirmPasswordField;

    @SpringBean protected UserService userService;
    @SpringBean private TenantSettingsService tenantSettingsService;

    private Long uniqueId;
    private IModel<User> userModel;

    public ChangeUserAccountPasswordPage(IModel<User> userModel) {
        super();
        this.uniqueId = userModel.getObject().getId();
        this.userModel = userModel;
        addComponents();
    }

    public ChangeUserAccountPasswordPage(PageParameters parameters) {
        super(parameters);
        uniqueId = parameters.get("uniqueID").toLong();
        userModel = Model.of(loadExistingUser());
        addComponents();
    }

    public ChangeUserAccountPasswordPage() {
        super();
        uniqueId = getSessionUser().getId();
        userModel = Model.of(loadExistingUser());
        addComponents();
    }

    private void addComponents() {

        Form form = new Form("form") {
            @Override
            protected void onSubmit() {
                updateUser();
                setResponsePage(ChangeUserAccountPasswordPage.class);
            }
        };
        add(form);

        form.add(new Link("cancel") {
            @Override
            public void onClick() {
                setResponsePage(ChangeUserAccountPasswordPage.class);
            }
        });

        form.add(new Label("userName", new PropertyModel<String>(userModel, "userID")));

        form.add(currentPasswordField = new PasswordTextField("currentPassword", new PropertyModel<String>(this, "newPassword")));
        currentPasswordField.add(new IValidator<String>() {
            @Override
            public void validate(IValidatable validatable) {
                if(userService.authenticateUserByPassword(FieldIDSession.get().getTenant().getName(),userModel.getObject().getUserID(),(String) validatable.getValue()) == null) {
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

        form.add(newPasswordField = new PasswordTextField("newPassword", new PropertyModel<String>(this, "newPassword")));
        newPasswordField.add(new Behavior() {
            @Override public void onComponentTag(Component c, ComponentTag tag) {
                FormComponent fc = (FormComponent) c;
                if (!fc.isValid()) {
                    tag.append("class", "error", " ");
                }
            }
        });
        newPasswordField.setOutputMarkupPlaceholderTag(true);
        form.add(confirmPasswordField = new PasswordTextField("confirmPassword", new PropertyModel<String>(this, "confirmPassword")));

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
        form.add(equalPasswordInputValidator);
    }


    private User loadExistingUser() {
        return getCurrentUser();
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.change_password"));
    }

    private void updateUser() {
        User user = userModel.getObject();
        user.updatePassword(getNewPassword());

        userService.update(user);
        logger.info("account profile password has been changed for " + getSessionUser().getUserID());
        FieldIDSession.get().info(new FIDLabelModel("message.users_password_updated").getObject());
    }

    private PasswordPolicy getPasswordPolicy() {
        return tenantSettingsService.getTenantSettings().getPasswordPolicy();
    }

    private String getNewPassword() {
        return newPassword;
    }

    private void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    private String getConfirmPassword() {
        return confirmPassword;
    }

    private void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


}
