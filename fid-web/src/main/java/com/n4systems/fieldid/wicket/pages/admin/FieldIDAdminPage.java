package com.n4systems.fieldid.wicket.pages.admin;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDWicketPage;
import com.n4systems.model.admin.AdminUser;
import com.n4systems.model.admin.AdminUserType;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class FieldIDAdminPage extends FieldIDWicketPage {
	
	public FieldIDAdminPage() {
		this(null);
	}
	
	public FieldIDAdminPage(PageParameters params) {
        super(params);
        
        if (!FieldIDSession.get().isAdminConsoleAuthenticated()) {
            throw new RedirectToUrlException("/admin/signIn.action");
        }

        setupNavigation();
    }

	private void setupNavigation() {
		AdminUser adminUser = FieldIDSession.get().getAdminUser();
		boolean isSuperUser = adminUser.getType().equals(AdminUserType.SUPER);

		NavigationItemBuilder navItemBuilder = NavigationItemBuilder.aNavItem();
		add(new NavigationBar("navBar",
			navItemBuilder.page("/admin/organizations.action").label("Tenants").build(),
			navItemBuilder.page("/w/admin/users").label("Users").cond(isSuperUser).build(),
			navItemBuilder.page("/admin/eulas.action").label("EULA").cond(isSuperUser).build(),
			navItemBuilder.page("/admin/orderMappingList.action").label("Order Mappings").cond(isSuperUser).build(),
			navItemBuilder.page("/admin/mailTest.action").label("Mail").cond(isSuperUser).build(),
			navItemBuilder.page("/admin/certSelection.action").label("Certs").cond(isSuperUser).build(),
			navItemBuilder.page("/admin/taskCrud.action").label("Tasks").cond(isSuperUser).build(),
			navItemBuilder.page("/w/admin/config").label("Config").cond(isSuperUser).build(),
			navItemBuilder.page("/w/admin/connections").label("Connections").cond(isSuperUser).build(),
            navItemBuilder.page("/w/admin/configureLanguages").label("Languages").cond(isSuperUser).build()
		));
	}

}
