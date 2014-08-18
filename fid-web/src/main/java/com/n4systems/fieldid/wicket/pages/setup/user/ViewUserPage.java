package com.n4systems.fieldid.wicket.pages.setup.user;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.actions.users.WelcomeMessage;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.user.SendWelcomeEmailService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.ConfirmBehavior;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.security.Permissions;
import com.n4systems.util.BitField;
import com.n4systems.util.timezone.CountryList;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class ViewUserPage extends FieldIDTemplatePage{

    @SpringBean
    private UserService userService;
    @SpringBean
    private SendWelcomeEmailService sendWelcomeEmailService;

    @SpringBean
    private S3Service s3Service;

    private Long uniqueId;
    protected IModel<User> userModel;

    public ViewUserPage(PageParameters params) {
        super(params);
        uniqueId = params.get("uniqueID").toLong();
        userModel = Model.of(userService.getUser(uniqueId));

        boolean isPerson = userModel.getObject().isPerson();

        Link archiveLink;
        add(archiveLink = new Link<Void>("archive") {
            @Override
            public void onClick() {
                userService.archive(userModel.getObject());
                FieldIDSession.get().info(new FIDLabelModel("message.userarchived").getObject());
                setResponsePage(UsersListPage.class);
            }
        });

        archiveLink.setVisible(!userModel.getObject().isAdmin());
        archiveLink.add(new ConfirmBehavior(new FIDLabelModel("warning.archiveuser", userModel.getObject().getFullName())));

        add(new BookmarkablePageLink<UpgradeUserPage>("upgrade", UpgradeUserPage.class, PageParametersBuilder.uniqueId(userModel.getObject().getId()))
                .setVisible(!userModel.getObject().isAdmin()));

        add(new Link<Void>("welcomeEmail") {
            @Override
            public void onClick() {
                sendWelcomeEmail(userModel.getObject(), new WelcomeMessage(), userModel.getObject().getHashPassword() != null);
            }
        });

        add(new Label("owner", new PropertyModel<String>(userModel, "owner.displayName")));

        add(new ListView<UserGroup>("userGroupList", Lists.newArrayList(userModel.getObject().getGroups())) {
            @Override
            protected void populateItem(final ListItem<UserGroup> item) {
                item.add(new Label("comma", ", ") {
                    @Override
                    public boolean isVisible() {
                        return item.getIndex() != 0;
                    }
                });
                item.add(new Label("name", new PropertyModel<UserGroup>(item.getModel(), "displayName")));
            }
        });

        add(new SmartLinkLabel("email", new PropertyModel<String>(userModel, "emailAddress")));

        add(new Label("name", new PropertyModel<String>(userModel, "fullName")));
        add(new Label("initials", new PropertyModel<String>(userModel, "initials")));
        add(new Label("identifier", new PropertyModel<String>(userModel, "identifier")));
        add(new Label("position", new PropertyModel<String>(userModel, "position")));

        add(new ExternalImage("signature", s3Service.getUserSignatureUrl(userModel.getObject())));

        String timeZoneId = userModel.getObject().getTimeZoneID();

        add(new Label("country", CountryList.getInstance().getCountryByFullName(timeZoneId).getDisplayName()));
        add(new Label("timezone", CountryList.getInstance().getRegionByFullId(timeZoneId).getDisplayName()));

        add(new Label("accountType", new PropertyModel<String>(userModel, "userType.label")));

        WebMarkupContainer accountInfoContainer;

        add(accountInfoContainer = new WebMarkupContainer("accountInfoContainer"));

        accountInfoContainer.add(new Label("lastLogin", new DayDisplayModel(new PropertyModel<Date>(userModel, "lastLogin")).includeTime().withTimeZone(getSessionUser().getTimeZone())));
        accountInfoContainer.add(new Label("username", new PropertyModel<String>(userModel, "userID")));
        accountInfoContainer.add(new Label("failedLoginAttempts", new PropertyModel<String>(userModel, "failedLoginAttempts")));
        if(userModel.getObject().isLocked())
            accountInfoContainer.add(new Label("status", new FIDLabelModel("label.locked")));
        else
            accountInfoContainer.add(new Label("status", new FIDLabelModel("label.unlocked")));

        WebMarkupContainer permissionsContainer;

        add(permissionsContainer = new WebMarkupContainer("permissionsContainer"));

        permissionsContainer.add(new ListView<Permission>("permission", getPermissionsList(userModel)) {
            @Override
            protected void populateItem(ListItem<Permission> item) {
                IModel<Permission> permission = item.getModel();
                item.add(new Label("name", new FIDLabelModel(new PropertyModel<String>(permission, "label"))));
                if (permission.getObject().enabled) {
                    item.add(new Label("state", new FIDLabelModel("label.on")));
                } else {
                    item.add(new Label("state", new FIDLabelModel("label.off")));
                }
            }
        });

        permissionsContainer.setVisible(!isPerson);
    }

    private List<Permission> getPermissionsList(IModel<User> userModel) {
        List<Permission> permissions = Lists.newArrayList();
        int[] permissionList;
        User user = userModel.getObject();
        BitField permField = new BitField(user.isNew() ? 0 : user.getPermissions());

        if(user.isLiteUser() || user.isUsageBasedUser())
            permissionList =  Permissions.getVisibleLiteUserPermissions();
        else
            permissionList =  Permissions.getVisibleSystemUserPermissions();

        for (int permission: permissionList) {
            permissions.add(new Permission(permission, Permissions.getLabel(permission), permField.isSet(permission)));
        }
        return permissions;
    }

    protected void sendWelcomeEmail(User user, WelcomeMessage welcomeMessage, boolean passwordAssigned) {
        sendWelcomeEmailService.sendUserWelcomeEmail(!passwordAssigned, user, welcomeMessage.getPersonalMessage(), createActionUrlBuilder());
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
                aNavItem().label("nav.change_password").page(ChangeUserPasswordPage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.mobile_passcode")
                        .page((userModel.getObject().getHashSecurityCardNumber() == null) ? EditUserMobilePasscodePage.class : ManageUserMobilePasscodePage.class)
                        .params(PageParametersBuilder.uniqueId(uniqueId)).build(),
                aNavItem().label("nav.mobile_profile").page(UserOfflineProfilePage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.add").page(this.getClass()).onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()
        ));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.view_user"));
    }

    private class Permission implements Serializable {
        int id;
        String label;
        Boolean enabled;

        public Permission(int id, String label, boolean enabled) {
            this.id = id;
            this.label = label;
            this.enabled = enabled;
        }

    }
}
