package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.customers.CustomerActionsPage;
import com.n4systems.fieldid.wicket.pages.setup.user.UserGroupsPage;
import com.n4systems.fieldid.wicket.pages.setup.user.UsersListPage;
import com.n4systems.fieldid.wicket.pages.setup.userregistration.UserRequestListPage;
import com.n4systems.model.ExtendedFeature;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class OwnersUsersLocationsPage extends SetupPage {
	
	@SpringBean
	private UserLimitService userLimitService;

	public OwnersUsersLocationsPage() {
	    boolean canManageSystemUsers = getSessionUser().hasAccess("managesystemusers");
        boolean advancedLocationEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.AdvancedLocation);

	    add(new BookmarkablePageLink<CustomerActionsPage>("manageCustomersLink", CustomerActionsPage.class,
                PageParametersBuilder.param(CustomerActionsPage.INITIAL_TAB_SELECTION_KEY, CustomerActionsPage.SHOW_CUSTOMER_LIST_PAGE)).
                setVisible(getSessionUser().hasAccess("manageendusers")));
        add(new BookmarkablePageLink<UsersListPage>("manageUsersLink", UsersListPage.class).setVisible(canManageSystemUsers));
        add(new BookmarkablePageLink<UserGroupsPage>("userGroupsLink", UserGroupsPage.class).setVisible(canManageSystemUsers));
	    add(new BookmarkablePageLink<UserRequestListPage>("userRegistrationsLink", UserRequestListPage.class).setVisible(canManageSystemUsers && userLimitService.isReadOnlyUsersEnabled()));
        add(new WebMarkupContainer("managePredefinedLocationsContainer").setVisible(getSessionUser().hasAccess("manageendusers") && advancedLocationEnabled));
	}
}

