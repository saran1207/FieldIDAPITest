package com.n4systems.fieldid.selenium.administration.page;

import static com.n4systems.fieldid.selenium.administration.page.CheckBoxMatcher.*;
import static com.n4systems.fieldid.selenium.nav.OptionNavgationDriver.*;
import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.datatypes.EventBook;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ManageEventBookDriver {
	
	private static final String CREATE_FORM_ID = "eventBookCreate";

	private static final String NAME_FIELD_LOCATOR = "_name";
	private static final String SAVE_BUTTON_LOCATOR = "_hbutton_save";
	private static final String OPEN_INPUT_LOCATOR = "_open";

	private static final String UPDATE_FORM_ID = "eventBookUpdate";

	
	private final FieldIdSelenium selenium;
	private final MiscDriver misc;

	public ManageEventBookDriver(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void gotoEventBooks() {
		selenium.open("/fieldid/eventBooks.action");
	}

	public void gotoAddEventBook() {
		gotoEventBooks();
		selenium.clickAndWaitForPageLoad(ADD_ACTION_LINK_LOCATOR);
	}

	public void createBook(EventBook book) {
		selenium.type(createFromLocator(NAME_FIELD_LOCATOR), book.name);
	

		if (book.status) 
			selenium.check(createFromLocator(OPEN_INPUT_LOCATOR));
		else
			selenium.uncheck(createFromLocator(OPEN_INPUT_LOCATOR));
		misc.gotoChooseOwner();
		misc.setOwner(book.owner);
		misc.gotoSelectOwner();
		
		
		selenium.clickAndWaitForPageLoad(createFromLocator(SAVE_BUTTON_LOCATOR));
	}


	public void assertBookWasCreated(EventBook book) {
		selectBookEditPage(book);
		assertEquals(book.name, selenium.getValue(updateFromLocator(NAME_FIELD_LOCATOR)));
		assertThat(selenium.getValue(updateFromLocator(OPEN_INPUT_LOCATOR)), aCheckBoxValueOf(book.status));
	}


	
	private void selectBookEditPage(EventBook book) {
		assertTrue("event book " + book.name + " does not appear in the list", selenium.isTextPresent(book.name));
		selenium.clickAndWaitForPageLoad("css=td a:contains('" + book.name + "')");
	}

	
	private String createFromLocator(String idEnding) {
		return CREATE_FORM_ID + idEnding;
	}
	private String updateFromLocator(String idEnding) {
		return UPDATE_FORM_ID + idEnding;
	}
	
	
	public void removeBook(EventBook book) {
		gotoEventBooks();
		String bookId = getEventBookId(book);
		selenium.click("css=#book_" + bookId + " a:contains('Delete')");
		selenium.waitForAjax();
		
	}


	private String getEventBookId(EventBook book) {
		gotoEventBooks();
		return selenium.getAttribute("css=td a:contains('" + book.name + "')@bookid");
	}


	public void assertBookWasRemoved(EventBook book) {
		assertNotInList(book);
			
	}

	private void assertNotInList(EventBook book) {
		assertFalse("book " + book + " still appears in the list", selenium.isTextPresent(book.name));
		
	}
}
