package com.n4systems.fieldid.selenium;

import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class SystemDriverFactory {

	private final MiscDriver misc;
	
	public SystemDriverFactory(FieldIdSelenium selenium) {
		this.misc = new MiscDriver(selenium);
	}
	
	public MiscDriver createMiscDriver() {
		return misc;
	}

}
