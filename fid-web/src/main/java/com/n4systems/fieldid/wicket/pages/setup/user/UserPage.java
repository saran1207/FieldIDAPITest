package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.actions.users.FileSystemUserSignatureFileProcessor;
import com.n4systems.fieldid.actions.users.UploadedImage;
import com.n4systems.fieldid.actions.users.WelcomeMessage;
import com.n4systems.fieldid.service.user.SendWelcomeEmailService;
import com.n4systems.fieldid.service.user.UserListFilterCriteria;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.user.UserFormAccountPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormIdentifiersPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormLocalizationPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormPermissionsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.n4systems.util.timezone.Region;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public abstract class UserPage extends FieldIDTemplatePage {

    @SpringBean
    protected UserService userService;
    @SpringBean
    private SendWelcomeEmailService sendWelcomeEmailService;

    protected UserType userType;
    private Long uniqueId;
    protected IModel<User> userModel;
    protected Country country;
    protected Region region;

    public UserPage(UserType userType) {
        this.userType = userType;
    }

    public UserPage(IModel<User> userModel) {
        this.uniqueId = userModel.getObject().getId();
        this.userModel = userModel;
    }

    public UserPage(PageParameters parameters) {
        uniqueId = parameters.get("uniqueID").toLong();
        userModel = Model.of(loadExistingUser());
    }

    protected abstract void doSave();

    protected abstract Component createAccountPanel(String id);

    protected abstract Component createPermissionsPanel(String id);

    protected UserFormIdentifiersPanel identifiersPanel;
    protected Component accountPanel;
    protected Component permissionsPanel;

    protected User loadExistingUser() {
        return userService.getUser(uniqueId);
    }

    protected UploadedImage getSignatureImage() {
        return new UploadedImage();
    }

    protected void saveSignatureFile(UploadedImage signature) {
        new FileSystemUserSignatureFileProcessor(userModel.getObject()).process(signature);
    }

    protected void sendWelcomeEmail(User user, WelcomeMessage welcomeMessage, boolean passwordAssigned) {
        sendWelcomeEmailService.sendUserWelcomeEmail(!passwordAssigned, user, welcomeMessage.getPersonalMessage(), createActionUrlBuilder());
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        country = CountryList.getInstance().getCountryByFullName(userModel.getObject().getTimeZoneID());
        region = CountryList.getInstance().getRegionByFullId(userModel.getObject().getTimeZoneID());
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new AddUserForm("addUserForm"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, OwnersUsersLocationsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected void addNavBar(String navBarId) {
        UserListFilterCriteria criteria = new UserListFilterCriteria(false);
        Long activeUserCount = userService.countUsers(criteria.withArchivedOnly(false));
        Long archivedUserCount = userService.countUsers(criteria.withArchivedOnly());
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", activeUserCount)).page(UsersListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", archivedUserCount)).page(ArchivedUsersListPage.class).build(),
                aNavItem().label("nav.add").page(SelectUserTypePage.class).onRight().build(),
                aNavItem().label(new FIDLabelModel("nav.import_export")).page(UserImportPage.class).onRight().build()
        ));
    }

    protected UserFormAccountPanel getUserFormAccountPanel() {
        return (UserFormAccountPanel)accountPanel;
    }

    protected UserFormPermissionsPanel getUserFormPermissionsPanel() {
        return (UserFormPermissionsPanel)permissionsPanel;
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
        newUser.setPermissions(getUserFormPermissionsPanel().getPermissions());

        userService.create(newUser);

        UploadedImage signature = identifiersPanel.getUploadedImage();

        if(signature.isNewImage()) {
            saveSignatureFile(signature);
        }

        if(getUserFormAccountPanel().getWelcomeMessage().isSendEmail())
            sendWelcomeEmail(newUser, getUserFormAccountPanel().getWelcomeMessage(), getUserFormAccountPanel().isAssignPassword());

        return newUser;
    }

    protected User update() {
        User user = userModel.getObject();

        user.assignSecruityCardNumber(getUserFormAccountPanel().getRfidNumber());
        user.setPermissions(getUserFormPermissionsPanel().getPermissions());

        UploadedImage signature = identifiersPanel.getUploadedImage();

        if(signature.isNewImage() || signature.isRemoveImage()) {
            saveSignatureFile(signature);
        }

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

            add(identifiersPanel = new UserFormIdentifiersPanel("identifiersPanel", userModel, getSignatureImage()) {
                @Override
                protected void onOwnerPicked(AjaxRequestTarget target) {
                    UserPage.this.onOwnerPicked(target);
                }
            });

            add(new UserFormLocalizationPanel("localizationPanel", userModel));

            add(accountPanel = createAccountPanel("accountPanel"));

            add(permissionsPanel = createPermissionsPanel("permissionsPanel"));

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


