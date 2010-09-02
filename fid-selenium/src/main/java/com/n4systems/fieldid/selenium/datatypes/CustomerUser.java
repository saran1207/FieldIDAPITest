package com.n4systems.fieldid.selenium.datatypes;

public class CustomerUser extends SystemUser {

	public CustomerUser(String userid, String email, String password, String verifyPassword, Owner owner, String firstName, String lastName) {
		super(userid, email, password, verifyPassword, owner, firstName, lastName);
	}
}
