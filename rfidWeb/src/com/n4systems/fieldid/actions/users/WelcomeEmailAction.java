package com.n4systems.fieldid.actions.users;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.user.UserWelcomeNotificationProducer;

public class WelcomeEmailAction extends AbstractCrud {

	private final User userManager;
	private UserBean user;

	public WelcomeEmailAction(PersistenceManager persistenceManager, User userManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		user = userManager.findUser(uniqueId, getTenantId());
	}

	
	protected void testRequiredEntities(boolean existing) {
		if (user == null || (existing && user.getId() == null)) {
			addActionErrorText("error.no_user");
			throw new MissingEntityException("user is required.");
		}
	}

	@SkipValidation
	public String doCreate() {
		testRequiredEntities(true);
		getWelcomeNotifier().sendWelcomeNotificationTo(user);
		return SUCCESS;
	}
	
	private UserWelcomeNotificationProducer getWelcomeNotifier() {
		UserWelcomeNotificationProducer userWelcomeNotificationProducer = new UserWelcomeNotificationProducer(getDefaultNotifier(), createActionUrlBuilder());
		return userWelcomeNotificationProducer;
	}
}
