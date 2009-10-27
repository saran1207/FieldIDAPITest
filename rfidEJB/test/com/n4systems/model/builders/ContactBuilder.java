package com.n4systems.model.builders;

import com.n4systems.model.Contact;
import com.n4systems.testutils.TestHelper;

public class ContactBuilder extends BaseBuilder<Contact> {

	public ContactBuilder() {
		super();
	}
	
	public static ContactBuilder aContact() {
		return new ContactBuilder();
	}
	
	@Override
	public Contact build() {
		return new Contact(TestHelper.randomString(), TestHelper.randomString());
	}

}
