package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.ExtendedFeature;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.user.UserLimitService;

public class OwnersUsersLocationsPage extends SetupPage {
	
	@SpringBean
	private UserLimitService userLimitService;

	public OwnersUsersLocationsPage() {
	    boolean canManageSystemUsers = getSessionUser().hasAccess("managesystemusers");
        boolean advancedLocationEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.AdvancedLocation);

	    add(new WebMarkupContainer("manageCustomersContainer").setVisible(getSessionUser().hasAccess("manageendusers")));
	    add(new WebMarkupContainer("manageUsersContainer").setVisible(canManageSystemUsers));
	    add(new WebMarkupContainer("manageUserRegistrationsContainer").setVisible(canManageSystemUsers && userLimitService.isReadOnlyUsersEnabled()));
        add(new WebMarkupContainer("managePredefinedLocationsContainer").setVisible(getSessionUser().hasAccess("managesystemconfig") && advancedLocationEnabled));
	}
}

