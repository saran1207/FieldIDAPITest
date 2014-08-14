package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.handler.password.PasswordHelper;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.user.User;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class ChangeUserPasswordPage extends FieldIDTemplatePage {

    @SpringBean
    protected UserService userService;
    @SpringBean
    private TenantSettingsService tenantSettingsService;

    private Long uniqueId;
    protected IModel<User> userModel;
    private String newPassword;
    private String confirmPassword;

    private FIDFeedbackPanel feedbackPanel;

    public ChangeUserPasswordPage(PageParameters params) {
        uniqueId = params.get("uniqueID").toLong();
        userModel = Model.of(userService.getUser(uniqueId));

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        Form form;
        add(form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                User user = userModel.getObject();
                user.updatePassword(newPassword);
                userService.update(user);
            }
        });

        PasswordTextField passwordField;
        PasswordTextField confirmField;

        form.add(passwordField = new PasswordTextField("newPassword", new PropertyModel<String>(this, "newPassword")));
        form.add(confirmField = new PasswordTextField("confirmPassword", new PropertyModel<String>(this, "confirmPassword")));

        form.add(new EqualPasswordInputValidator(passwordField, confirmField));
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

        form.add(new SubmitLink("save"));
        form.add(new BookmarkablePageLink<ViewUserPage>("cancel", ViewUserPage.class, PageParametersBuilder.uniqueId(uniqueId)));

        add(new NonWicketLink("sendResetEmail",
                "adminSendResetPassword.action?userName=" + userModel.getObject().getUserID() + "&uniqueID=" + userModel.getObject().getId(),
                new AttributeAppender("class", "btn-secondary")));
    }

    private PasswordPolicy getPasswordPolicy() {
        return tenantSettingsService.getTenantSettings().getPasswordPolicy();
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, OwnersUsersLocationsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(UsersListPage.class).build(),
                aNavItem().label("nav.view_all_archived").page(ArchivedUsersListPage.class).build(),
                aNavItem().label("nav.view").page(ViewUserPage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.edit").page(EditUserPage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.change_password").page(ChangeUserPasswordPage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.mobile_passcode")
                        .page((userModel.getObject().getHashSecurityCardNumber()==null) ? EditUserMobilePasscodePage.class : ManageUserMobilePasscode.class)
                        .params(PageParametersBuilder.uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.mobile_profile").page(UserOfflineProfilePage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.add").page(SelectUserTypePage.class).onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()
        ));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.user_change_password"));
    }
}
