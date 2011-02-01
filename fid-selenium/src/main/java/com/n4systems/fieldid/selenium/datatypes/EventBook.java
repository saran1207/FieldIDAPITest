package com.n4systems.fieldid.selenium.datatypes;

public class EventBook {

	public boolean status;
	public Owner owner;
	public String name;

	public EventBook(String name, Owner owner, boolean status) {
		this.name = name;
		this.owner = owner;
		this.status = status;
	}

}
