package com.n4systems.fieldid.selenium.datatypes;

import static com.n4systems.fieldid.selenium.datatypes.Owner.*;

import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class EventBook {

	public final boolean status;
	public final Owner owner;
	public final String name;

	public EventBook(String name, Owner owner, boolean status) {
		this.name = name;
		this.owner = owner;
		this.status = status;
	}

	public static EventBook aVaildEventBook() {
		return new EventBook(aRandomName(), someOrg(), true);
	}

	private static String aRandomName() {
		return MiscDriver.getRandomString(25);
	}
}
