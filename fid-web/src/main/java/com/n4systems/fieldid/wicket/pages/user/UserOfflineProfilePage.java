package com.n4systems.fieldid.wicket.pages.user;

import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.useraccount.OfflineProfilePanel;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class UserOfflineProfilePage extends FieldIDFrontEndPage {

    @SpringBean
    protected UserService userService;

    @SpringBean
    private OfflineProfileService offlineProfileService;

    private Long uniqueId;
    private User user;

    public UserOfflineProfilePage(PageParameters parameters) {
        this.uniqueId = parameters.get("uniqueID").toLong();
        this.user = userService.getUser(uniqueId);
        if(offlineProfileService.hasOfflineProfile(user))
            add(new OfflineProfilePanel("offlineProfilePanel", user));
        else
            add(new WebMarkupContainer("offlineProfilePanel"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page("userList.action").build(),
                aNavItem().label("nav.view_all_archived").page("archivedUserList.action").params(param("currentPage", 1)).build(),
                aNavItem().label("nav.view").page("viewUser.action").params(uniqueId(uniqueId)).build(),
                aNavItem().label("nav.edit").page("employeeUserEdit.action").params(uniqueId(uniqueId)).cond(user.isFullUser()).build(),
                aNavItem().label("nav.edit").page("liteUserEdit.action").params(uniqueId(uniqueId)).cond(user.isLiteUser()).build(),
                aNavItem().label("nav.edit").page("readOnlyUserEdit.action").params(uniqueId(uniqueId)).cond(user.isReadOnly()).build(),
                aNavItem().label("nav.change_password").page("adminEditPassword.action").params(uniqueId(uniqueId)).build(),
                aNavItem().label("nav.mobile_passcode").page("adminMobilePasscode.action").params(uniqueId(uniqueId)).build(),
                aNavItem().label("nav.mobile_profile").page(UserOfflineProfilePage.class).params(uniqueId(uniqueId)).build(),
                aNavItem().label("nav.add").page("addUser.action").onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()

        ));
    }
}
