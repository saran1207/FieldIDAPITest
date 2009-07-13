package com.n4systems.fieldid.actions.user;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;

public class MyAccount extends AbstractAction {

	private static final long serialVersionUID = 1L;

	//private static final Logger logger = Logger.getLogger( MyAccount.class );
	
	private User userManager;
	
	private UserBean currentUser;

	public MyAccount( User userManager, PersistenceManager persistenceManager ) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	public String doShow() {
		currentUser = userManager.getUser( getSessionUser().getUniqueID() );
		return SUCCESS;
	}
	
	public UserBean getCurrentUser() {
		return currentUser;
	}
	
}
