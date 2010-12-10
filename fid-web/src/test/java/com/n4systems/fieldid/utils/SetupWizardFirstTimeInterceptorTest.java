package com.n4systems.fieldid.utils;

import static com.n4systems.model.builders.UserBuilder.*;
import static org.junit.Assert.*;


import org.junit.Test;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.ui.seenit.SeenItRegistry;
import com.n4systems.fieldid.ui.seenit.SeenItRegistryEmpyDataSource;
import com.n4systems.fieldid.ui.seenit.SeenItRegistryImpl;
import com.n4systems.fieldid.utils.SetupWizardFirstTimeInterceptor;
import com.n4systems.model.ui.seenit.SeenItItem;
import com.n4systems.model.user.User;
import com.n4systems.util.UserType;

public class SetupWizardFirstTimeInterceptorTest {

	@Test
	public void should_require_set_up_wizard_if_user_is_an_admin_and_has_not_() {
		User user = anAdminUser().build();
		user.setUserType(UserType.ADMIN);
		SessionUser adminSessionUser = new SessionUser(user);
		
		SetupWizardFirstTimeInterceptor sut = new SetupWizardFirstTimeInterceptor();
	
		assertTrue(sut.forceUserToSetupWizard(adminSessionUser, createSeenItRegistryThatHasSeenNothing()));
	}

	
	@Test
	public void should_not_require_set_up_wizard_if_user_is_not_an_admin_no_matter_what_the_seen_it_registry_value_is() {
		SessionUser adminSessionUser = new SessionUser(aUser().build());
		SetupWizardFirstTimeInterceptor sut = new SetupWizardFirstTimeInterceptor();
		
		assertTrue(!sut.forceUserToSetupWizard(adminSessionUser, null));
	}
	
	
	@Test
	public void should_not_require_set_up_wizard_if_user_is_an_admin_but_has_already_visited_the_setup_wizard() {
		SeenItRegistry seenItRegistry = createSeenItRegistryThatHasSeen(SeenItItem.SetupWizard);
		
		User user = anAdminUser().build();
		user.setUserType(UserType.ADMIN);
		SessionUser adminSessionUser = new SessionUser(user);
		
		
		SetupWizardFirstTimeInterceptor sut = new SetupWizardFirstTimeInterceptor();
		
		assertTrue(!sut.forceUserToSetupWizard(adminSessionUser, seenItRegistry));
	}



	private SeenItRegistry createSeenItRegistryThatHasSeen(SeenItItem item) {
		SeenItRegistry seenItRegistry = createSeenItRegistryThatHasSeenNothing();
		seenItRegistry.iHaveSeen(item);
		return seenItRegistry;
	}

	private SeenItRegistry createSeenItRegistryThatHasSeenNothing() {
		return new SeenItRegistryImpl(new SeenItRegistryEmpyDataSource());
	}

}
