package com.n4systems.fieldid.selenium;

import com.n4systems.fieldid.selenium.pages.WebPage;
import org.junit.Before;

public abstract class PageNavigatingTestCase<T extends WebPage> extends FieldIDTestCase {
	
	protected T page;
	
	protected abstract T navigateToPage();
	
	@Before
	public void navigate() {
		page = navigateToPage();
	}

}
