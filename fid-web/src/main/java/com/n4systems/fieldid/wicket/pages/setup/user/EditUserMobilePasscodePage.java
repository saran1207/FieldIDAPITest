package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.user.User;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

/**
 * Created by tracyshi on 2014-08-13.
 */
public class EditUserMobilePasscodePage extends FieldIDTemplatePage {

    @SpringBean
    private UserService userService;

    private Form passcodeInputForm;
    private IModel<User> userModel;
    private Long uniqueId;
    private String mobilePasscode;

    public EditUserMobilePasscodePage(PageParameters params) {
        super(params);
        mobilePasscode = null;
        uniqueId = params.get("uniqueID").toLong();
        userModel = Model.of(userService.getUser(uniqueId));
        passcodeInputForm = new Form("passcodeInputForm") {
            @Override
            public void onSubmit() {
                User user = userModel.getObject();
                user.assignSecruityCardNumber(mobilePasscode);
                userService.update(user);
                FieldIDSession.get().info(new FIDLabelModel("message.passcodeupdated").getObject());
                setResponsePage(ViewUserPage.class, PageParametersBuilder.uniqueId(uniqueId));
            }
        };

        final RequiredTextField passcodeTextField = new RequiredTextField<String>("passcodeInputField", new PropertyModel<String>(this, "mobilePasscode"));
        passcodeTextField.add(new AbstractValidator() {
            @Override
            protected void onValidate(IValidatable iValidatable) {
                String passcode = (String) iValidatable.getValue();
                if (passcode.length() < 4) {
                    ValidationError error = new ValidationError();
                    error.addMessageKey("errors.passcodelength");
                    iValidatable.error(error);
                }
            }
        });

        passcodeInputForm.add(passcodeTextField);
        passcodeInputForm.add(new Button("saveButton"));
        passcodeInputForm.add(new BookmarkablePageLink("cancelLink", EditUserPage.class, PageParametersBuilder.uniqueId(uniqueId)));

        add(passcodeInputForm);
        add(new FIDFeedbackPanel("feedbackPanel"));
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
                aNavItem().label("nav.view").page(ViewUserPage.class).params(PageParametersBuilder.uniqueId(uniqueId)).build(),
                aNavItem().label("nav.edit").page(EditUserPage.class).params(PageParametersBuilder.uniqueId(uniqueId)).build(),
                aNavItem().label("nav.change_password").page(ChangeUserPasswordPage.class).params(uniqueId(uniqueId)).build(),
                aNavItem().label("nav.mobile_passcode").page(EditUserMobilePasscodePage.class).params(PageParametersBuilder.uniqueId(uniqueId)).build(),
                aNavItem().label("nav.mobile_profile").page(UserOfflineProfilePage.class).params(uniqueId(uniqueId)).build(),
                aNavItem().label("nav.add").page(AddUserPage.class).onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()
        ));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.user_change_rfid_number"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/newCss/user/user_mobile_passcode.css");
    }

}
