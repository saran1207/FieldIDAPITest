package com.n4systems.fieldid.selenium;

import com.n4systems.fieldid.selenium.administration.page.ManageProductStatuses;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class SystemDriverFactory {

	private final FieldIdSelenium selenium;
	private final MiscDriver misc;
	
	public SystemDriverFactory(FieldIdSelenium selenium) {
		super();
		this.selenium = selenium;
		this.misc = new MiscDriver(selenium);
	}
	
	
	public ManageProductStatuses createProductStatusDriver() {
		return new ManageProductStatuses(selenium);
	}


	public MiscDriver createMiscDriver() {
		return misc;
	}
}
