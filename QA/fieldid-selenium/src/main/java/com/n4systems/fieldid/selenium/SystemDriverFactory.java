package com.n4systems.fieldid.selenium;

import com.n4systems.fieldid.selenium.administration.page.ManageInspectionBookDriver;
import com.n4systems.fieldid.selenium.administration.page.ManageProductStatusDriver;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class SystemDriverFactory {

	private final FieldIdSelenium selenium;
	private final MiscDriver misc;
	
	public SystemDriverFactory(FieldIdSelenium selenium) {
		this.selenium = selenium;
		this.misc = new MiscDriver(selenium);
	}
	
	public MiscDriver createMiscDriver() {
		return misc;
	}

	public ManageProductStatusDriver createProductStatusDriver() {
		return new ManageProductStatusDriver(selenium);
	}

	public ManageInspectionBookDriver createInspectionBookDriver() {
		return new ManageInspectionBookDriver(selenium, misc);
	}
}
