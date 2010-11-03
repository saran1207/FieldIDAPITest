package com.n4systems.fieldid.selenium.testcase.setup;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.administration.page.ManageInspectionBookDriver;
import com.n4systems.fieldid.selenium.datatypes.EventBook;
import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;

public class EventBookCrudTest extends LoggedInTestCase {
	
	private ManageInspectionBookDriver driver;
	
	@Before
	public void createDrivers() {
		driver = systemDriverFactory.createInspectionBookDriver();
	}

	@Test
	public void should_allow_the_creation_editing_and_removal_of_a_inspection_book() throws Exception {
		EventBook book = EventBook.aVaildInspectionBook();
		
		driver.gotoAddInspectionBook();
		
		driver.createBook(book);

		driver.assertBookWasCreated(book);
		
		driver.removeBook(book);
		
		driver.assertBookWasRemoved(book);
	}

}
