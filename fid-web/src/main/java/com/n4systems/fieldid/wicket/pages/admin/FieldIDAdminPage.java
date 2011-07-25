package com.n4systems.fieldid.wicket.pages.admin;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RedirectToUrlException;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDWicketPage;

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
		NavigationItemBuilder navItemBuilder = NavigationItemBuilder.aNavItem();
		add(new NavigationBar("navBar",
			navItemBuilder.page("/admin/organizations.action").label("Tenants").build(),
			navItemBuilder.page("/admin/eulas.action").label("EULA").build(),
			navItemBuilder.page("/admin/releaseNotes.action").label("Release Notes").build(),
			navItemBuilder.page("/admin/instructionalVideos.action").label("Instructional Videos").build(),
			navItemBuilder.page("/admin/unitOfMeasureList.action").label("Units of Measure").build(),
			navItemBuilder.page("/admin/orderMappingList.action").label("Order Mappings").build(),
			navItemBuilder.page("/admin/importObservations.action").label("Observation Importer").build(),
			navItemBuilder.page("/admin/mailTest.action").label("Mail").build(),
			navItemBuilder.page("/admin/configCrud.action").label("Config").build(),
			navItemBuilder.page("/admin/certSelection.action").label("Certs").build(),
			navItemBuilder.page("/admin/taskCrud.action").label("Tasks").build(),
			navItemBuilder.page("/admin/changeAdminPass.action").label("System Pass").build()
		));
	}

}
