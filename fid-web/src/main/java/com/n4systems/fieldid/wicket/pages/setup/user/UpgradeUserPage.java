package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class UpgradeUserPage extends FieldIDTemplatePage {

    @SpringBean
    protected UserService userService;

    @SpringBean
    UserLimitService userLimitService;

    private Long uniqueId;
    protected IModel<User> userModel;


    public UpgradeUserPage(PageParameters parameters) {
        uniqueId = parameters.get("uniqueID").toLong();
        userModel = Model.of(userService.getUser(uniqueId));

        add(new Link<Void>("changeToAdminUser") {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                if(userModel.getObject().isFullUser()) {
                    add(new AttributeAppender("class", Model.of("disabled"), " "));
                }
            }
            @Override
            public void onClick() {
                upgrade(UserType.FULL);
            }
        }.add(new FlatLabel("label", new FIDLabelModel(userModel.getObject().isFullUser() ? "label.current_user_type":"label.change_to_full")))
                .setVisible(!userLimitService.isEmployeeUsersAtMax())
                .setEnabled(!userModel.getObject().isFullUser()));

        add(new ExternalImage("adminUserWarning", "/fieldid/images/warning-icon.png")
                .add(new TipsyBehavior(new FIDLabelModel("label.full_user_limit_reached"), TipsyBehavior.Gravity.N))
                .setVisible(userLimitService.isEmployeeUsersAtMax()));

        add(new Link<Void>("changeToInspectionUser") {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                if(userModel.getObject().isLiteUser()) {
                    add(new AttributeAppender("class", Model.of("disabled"), " "));
                }
            }

            @Override
            public void onClick() {
                upgrade(UserType.LITE);
            }
        }.add(new FlatLabel("label", new FIDLabelModel(userModel.getObject().isLiteUser() ? "label.current_user_type":"label.change_to_lite")))
                .setVisible(!userLimitService.isLiteUsersAtMax())
                .setEnabled(!userModel.getObject().isLiteUser()));

        add(new ExternalImage("inspectionUserWarning", "/fieldid/images/warning-icon.png")
                .add(new TipsyBehavior(new FIDLabelModel("label.lite_user_limit_reached"), TipsyBehavior.Gravity.N))
                .setVisible(userLimitService.isLiteUsersAtMax()));

        add(new Link<Void>("changeToReportingUser") {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                if(userModel.getObject().isReadOnly()) {
                    add(new AttributeAppender("class", Model.of("disabled"), " "));
                }
            }
            @Override
            public void onClick() {
                upgrade(UserType.READONLY);
            }
        }.add(new FlatLabel("label", new FIDLabelModel(userModel.getObject().isReadOnly() ? "label.current_user_type":"label.change_to_readonly")))
                .setVisible(!userLimitService.isReadOnlyUsersAtMax())
                .setEnabled(!userModel.getObject().isReadOnly()));

        add(new ExternalImage("reportingUserWarning", "/fieldid/images/warning-icon.png")
                .add(new TipsyBehavior(new FIDLabelModel("label.readonly_user_limit_reached"), TipsyBehavior.Gravity.N))
                .setVisible(userLimitService.isReadOnlyUsersAtMax()));

        add(new Link<Void>("changeToPersonUser") {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                if(userModel.getObject().isPerson()) {
                    add(new AttributeAppender("class", Model.of("disabled"), " "));
                }
            }
            @Override
            public void onClick() {
                upgrade(UserType.PERSON);
            }
        }.add(new FlatLabel("label", new FIDLabelModel(userModel.getObject().isPerson() ? "label.current_user_type":"label.change_to_person")))
                .setEnabled(!userModel.getObject().isPerson()));

        add(new Link<Void>("changeToUsageBasedUser") {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                if(userModel.getObject().isUsageBasedUser()) {
                    add(new AttributeAppender("class", Model.of("disabled"), " "));
                }
            }
            @Override
            public void onClick() {
                upgrade(UserType.USAGE_BASED);
            }
        }.add(new FlatLabel("label", new FIDLabelModel(userModel.getObject().isUsageBasedUser() ? "label.current_user_type":"label.change_to_usage_based")))
                .setVisible(userLimitService.isUsageBasedUsersEnabled())
                .setEnabled(!userModel.getObject().isUsageBasedUser()));
    }

    private void upgrade(UserType userType) {
        userModel.getObject().setUserType(userType);
        if(userType.equals(UserType.PERSON))
            setResponsePage(new EditPersonPage(userModel));
        else
            setResponsePage(new EditUserPage(userModel));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(UsersListPage.class).build(),
                aNavItem().label("nav.view_all_archived").page(ArchivedUsersListPage.class).build(),
                aNavItem().label("nav.add").page(this.getClass()).onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()
        ));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.change_account_type"));
    }
}
