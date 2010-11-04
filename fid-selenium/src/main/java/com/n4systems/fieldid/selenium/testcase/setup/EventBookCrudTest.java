package com.n4systems.fieldid.selenium.testcase.setup;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.administration.page.ManageEventBookDriver;
import com.n4systems.fieldid.selenium.datatypes.EventBook;
import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;

public class EventBookCrudTest extends LoggedInTestCase {
	
	private ManageEventBookDriver driver;
	
	@Before
	public void createDrivers() {
		driver = systemDriverFactory.createEventBookDriver();
	}

	@Test
	public void should_allow_the_creation_editing_and_removal_of_an_event_book() throws Exception {
		EventBook book = EventBook.aVaildEventBook();
		
		driver.gotoAddEventBook();
		
		driver.createBook(book);

		driver.assertBookWasCreated(book);
		
		driver.removeBook(book);
		
		driver.assertBookWasRemoved(book);
	}

}
