package com.n4systems.model.builders;

import com.n4systems.model.Contact;

public class ContactBuilder extends BaseBuilder<Contact> {
	private String name;
	private String email;
	
	protected ContactBuilder(String name, String email) {
		this.name = name;
		this.email = email;
	}
	
	public static ContactBuilder aContact() {
		return new ContactBuilder("Joe Blow", "jblow@example.com");
	}
	
	public ContactBuilder withName(String name) {
		return new ContactBuilder(name, email);
	}
	
	public ContactBuilder withEmail(String email) {
		return new ContactBuilder(name, email);
	}
	
	@Override
	public Contact createObject() {
		return new Contact(name, email);
	}

}
