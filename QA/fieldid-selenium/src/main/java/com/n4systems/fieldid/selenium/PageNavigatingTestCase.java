package com.n4systems.fieldid.selenium;

import org.junit.Before;

import com.n4systems.fieldid.selenium.pages.WebPage;

public abstract class PageNavigatingTestCase<T extends WebPage> extends FieldIDTestCase {
	
	protected T page;
	
	protected abstract T navigateToPage();
	
	@Before
	public void navigate() {
		page = navigateToPage();
	}

}
