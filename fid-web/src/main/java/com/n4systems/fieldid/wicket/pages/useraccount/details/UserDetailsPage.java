package com.n4systems.fieldid.wicket.pages.useraccount.details;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormAccountPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormIdentifiersPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.pages.setup.user.UsersListPage;
import com.n4systems.fieldid.wicket.pages.setup.user.ViewUserPage;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

public class UserDetailsPage extends FieldIDTemplatePage {

    @SpringBean protected UserService userService;

    protected UserType userType;
    private Long uniqueId;
    protected IModel<User> userModel;

    public UserDetailsPage(UserType userType) {
        this.userType = userType;
    }

    public UserDetailsPage(IModel<User> userModel) {
        this.uniqueId = userModel.getObject().getId();
        this.userModel = userModel;
    }

    public UserDetailsPage(PageParameters parameters) {
        uniqueId = parameters.get("uniqueID").toLong();
        userModel = Model.of(loadExistingUser());
    }

    public UserDetailsPage() {
        uniqueId = getSessionUser().getId();
        userModel = Model.of(loadExistingUser());
    }

    protected void doSave() {
        update();
        setResponsePage(UserDetailsPage.class);
    }

    protected Component createAccountPanel(String id) {
        return new UserFormAccountPanel(id, userModel);
    }

    protected UserFormIdentifiersPanel identifiersPanel;
    protected Component accountPanel;

    protected User loadExistingUser() {
        return userService.getUser(uniqueId);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new AddUserForm("addUserForm"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, UserDetailsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    protected UserFormAccountPanel getUserFormAccountPanel() {
        return (UserFormAccountPanel)accountPanel;
    }

    protected IModel<User> createUser(UserType userType) {
        User newUser = new User();
        newUser.setTenant(getTenant());
        newUser.setRegistered(true);
        newUser.setModifiedBy(getCurrentUser());
        newUser.setCreatedBy(getCurrentUser());
        newUser.setUserType(userType);
        newUser.setTimeZoneID(getSessionUser().getOwner().getInternalOrg().getDefaultTimeZone());
        return Model.of(newUser);
    }

    protected User create() {
        User newUser = userModel.getObject();
        if(getUserFormAccountPanel().isAssignPassword())
            newUser.assignPassword(getUserFormAccountPanel().getPassword());
        else {
            newUser.assignPassword(null);
            newUser.createResetPasswordKey();
        }
        newUser.assignSecruityCardNumber(getUserFormAccountPanel().getRfidNumber());

        userService.create(newUser);

        return newUser;
    }

    protected User update() {
        User user = userModel.getObject();

        user.assignSecruityCardNumber(getUserFormAccountPanel().getRfidNumber());

        if(user.isArchived()) {
            user.activateEntity();
        }

        userService.update(user);

        return user;
    }

    protected void addConfirmBehavior(SubmitLink submitLink) {
    }

    protected void onOwnerPicked(AjaxRequestTarget target) {}


    class AddUserForm extends Form {

        SubmitLink submitLink;

        public AddUserForm(String id) {
            super(id);

            add(identifiersPanel = new UserFormIdentifiersPanel("identifiersPanel", userModel, null) {
                @Override
                protected void onOwnerPicked(AjaxRequestTarget target) {
                    UserDetailsPage.this.onOwnerPicked(target);
                }
            });

            add(accountPanel = createAccountPanel("accountPanel"));

            add(submitLink = new SubmitLink("save"));

            addConfirmBehavior(submitLink);

            add(new BookmarkablePageLink<UsersListPage>("cancel", UsersListPage.class));
        }

        @Override
        protected void onSubmit() {
            doSave();
            FieldIDSession.get().info(new FIDLabelModel("message.user_saved").getObject());
        }
    }

}


