package com.n4systems.fieldid.selenium.testcase.setup;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.EventBook;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventBooksPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Event;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ManageEventBooksTest_DbSetup extends FieldIDTestCase {

    ManageEventBooksPage manageEventBooksPage;

    @Before
    public void setup() {
        manageEventBooksPage = startAsCompany("test1").systemLogin().clickSetupLink().clickManageEventBooks();
    }

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.anEventBook()
                .withName("The Open Book")
                .open(true).build();

        scenario.anEventBook()
                .withName("The Closed Book")
                .open(false).build();

        Event event = scenario.aSimpleEvent().build();

        scenario.anEventBook()
                .withName("The In Use Book")
                .withEvents(event)
                .open(true).build();
    }

    @Test
    public void test_close_an_open_book() throws Exception {
        String status = manageEventBooksPage.getStatusForBookNamed("The Open Book");

        assertEquals("Open", status);

        manageEventBooksPage.clickCloseForBookNamed("The Open Book");
        manageEventBooksPage.verifyEventBookSaved();

        assertEquals("Closed", manageEventBooksPage.getStatusForBookNamed("The Closed Book"));
    }

    @Test
    public void test_open_a_closed_book() throws Exception {
        String status = manageEventBooksPage.getStatusForBookNamed("The Closed Book");

        assertEquals("Closed", status);

        manageEventBooksPage.clickOpenForBookNamed("The Closed Book");
        manageEventBooksPage.verifyEventBookSaved();

        assertEquals("Open", manageEventBooksPage.getStatusForBookNamed("The Closed Book"));
    }

    @Test
    public void test_add_with_error() throws Exception {
        manageEventBooksPage.clickAddTab();
        assertEquals("Add", manageEventBooksPage.getCurrentTab());
        manageEventBooksPage.clickSave();
        assertEquals(2, manageEventBooksPage.getFormErrorMessages().size());
    }

    @Test
    public void test_add_and_delete_book() throws Exception {
        assertFalse(manageEventBooksPage.eventBookExists("Test Selenium"));

        manageEventBooksPage.clickAddTab();
        assertEquals("Add", manageEventBooksPage.getCurrentTab());
        EventBook book = getTestEventBook();
        manageEventBooksPage.enterEventBookFormFields(book);
        manageEventBooksPage.clickSave();
        manageEventBooksPage.verifyEventBookSaved();
        assertEquals("View All", manageEventBooksPage.getCurrentTab());

        assertTrue(manageEventBooksPage.eventBookExists("Test Selenium"));
        manageEventBooksPage.clickDeleteForBookNamed("Test Selenium");
        assertFalse(manageEventBooksPage.eventBookExists("Test Selenium"));
    }

    @Test
    public void test_delete_book_in_use() throws Exception {
        manageEventBooksPage.clickDeleteForBookNamed("The In Use Book");
        assertEquals("Event Book can not be deleted. It is still in use.", manageEventBooksPage.getAlert().trim());
    }

    private EventBook getTestEventBook() {
        return new EventBook("Test Selenium", new Owner("test1", null), true);
    }

}
