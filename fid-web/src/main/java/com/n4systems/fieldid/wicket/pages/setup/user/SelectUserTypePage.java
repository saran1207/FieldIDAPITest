package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.security.UserType;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class SelectUserTypePage extends FieldIDTemplatePage {

    @SpringBean
    UserLimitService userLimitService;

    public SelectUserTypePage() {
        add(new Link<Void>("addAdminUser") {
            @Override
            public void onClick() {
                setResponsePage(new AddUserPage(UserType.FULL));
            }
        }.setVisible(!userLimitService.isEmployeeUsersAtMax()));

        add(new ExternalImage("adminUserWarning", "/fieldid/images/warning-icon.png")
                .add(new TipsyBehavior(new FIDLabelModel("label.full_user_limit_reached"), TipsyBehavior.Gravity.N))
                .setVisible(userLimitService.isEmployeeUsersAtMax()));

        add(new Link<Void>("addInspectionUser") {
            @Override
            public void onClick() {
                setResponsePage(new AddUserPage(UserType.LITE));
            }
        }.setVisible(!userLimitService.isLiteUsersAtMax()));

        add(new ExternalImage("inspectionUserWarning", "/fieldid/images/warning-icon.png")
                .add(new TipsyBehavior(new FIDLabelModel("label.lite_user_limit_reached"), TipsyBehavior.Gravity.N))
                .setVisible(userLimitService.isLiteUsersAtMax()));

        add(new Link<Void>("addReportingUser") {
            @Override
            public void onClick() {
                setResponsePage(new AddUserPage(UserType.READONLY));
            }
        }.setVisible(!userLimitService.isReadOnlyUsersAtMax()));

        add(new ExternalImage("reportingUserWarning", "/fieldid/images/warning-icon.png")
                .add(new TipsyBehavior(new FIDLabelModel("label.readonly_user_limit_reached"), TipsyBehavior.Gravity.N))
                .setVisible(userLimitService.isReadOnlyUsersAtMax()));

        add(new Link<Void>("addPersonUser") {
            @Override
            public void onClick() {
                setResponsePage(new AddPersonPage());
            }
        });

        add(new Link<Void>("addUsageBasedUser") {
            @Override
            public void onClick() {
                setResponsePage(new AddUserPage(UserType.USAGE_BASED));
            }
        }.setVisible(userLimitService.isUsageBasedUsersEnabled()));
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
        return new Label(labelId, new FIDLabelModel("label.add_user"));
    }
}
