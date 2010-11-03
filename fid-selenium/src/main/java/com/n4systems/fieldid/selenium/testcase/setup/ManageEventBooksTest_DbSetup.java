package com.n4systems.fieldid.selenium.testcase.setup;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.EventBook;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventBooksPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ManageEventBooksTest_DbSetup extends FieldIDTestCase {

    ManageEventBooksPage manageEventBooksPage;

    @Before
    public void setup() {
        manageEventBooksPage = startAsCompany("seafit").systemLogin().clickSetupLink().clickManageEventBooks();
    }

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.anInspectionBook()
                .forTenant(scenario.tenant("seafit"))
                .withName("The Open Book")
                .withOwner(scenario.primaryOrgFor("seafit"))
                .open(true).build();

        scenario.anInspectionBook()
                .forTenant(scenario.tenant("seafit"))
                .withName("The Closed Book")
                .withOwner(scenario.primaryOrgFor("seafit"))
                .open(false).build();
    }

    @Test
    @Ignore
    public void test_view_all_event_books() throws Exception {
        assertEquals("View All", manageEventBooksPage.getCurrentTab());
    }

    @Test
    @Ignore
    public void test_open_and_close_book() throws Exception {
        String status1 = manageEventBooksPage.getFirstListItemStatus();
        String status2, status3;

        if(status1.equals("Open")) {
            manageEventBooksPage.clickFirstListItemClose();
            status2 = manageEventBooksPage.getFirstListItemStatus();
            manageEventBooksPage.verifyInspectionBookSaved();
            manageEventBooksPage.clickFirstListItemOpen();
            status3 = manageEventBooksPage.getFirstListItemStatus();
        }else {
            manageEventBooksPage.clickFirstListItemOpen();
            status2 = manageEventBooksPage.getFirstListItemStatus();
            manageEventBooksPage.verifyInspectionBookSaved();
            manageEventBooksPage.clickFirstListItemClose();
            status3 = manageEventBooksPage.getFirstListItemStatus();
        }

        manageEventBooksPage.verifyInspectionBookSaved();
        assertFalse(status1.equals(status2));
        assertEquals(status1, status3);
    }

    @Test
    @Ignore
    public void test_add_with_error() throws Exception {
        manageEventBooksPage.clickAddTab();
        assertEquals("Add", manageEventBooksPage.getCurrentTab());
        manageEventBooksPage.clickSave();
        assertEquals(2, manageEventBooksPage.getFormErrorMessages().size());
    }

    @Test
    @Ignore
    public void test_add_and_delete_book() throws Exception {
        deleteIfExists("Test Selenium");

        manageEventBooksPage.clickAddTab();
        assertEquals("Add", manageEventBooksPage.getCurrentTab());
        EventBook book = getTestEventBook();
        manageEventBooksPage.setInspectionBookFormFields(book);
        manageEventBooksPage.clickSave();
        manageEventBooksPage.verifyInspectionBookSaved();
        assertEquals("View All", manageEventBooksPage.getCurrentTab());

        deleteIfExists("Test Selenium");
    }

    private void deleteIfExists(String bookName) {
        if(manageEventBooksPage.listItemExists(bookName)) {
            manageEventBooksPage.clickDelete(bookName);
            manageEventBooksPage.verifyInspectionBookDeleted();
        }
    }

    @Test
    @Ignore
    public void test_delete_book_in_use() throws Exception {
        String bookName = manageEventBooksPage.getFirstListItemName();
        manageEventBooksPage.clickDelete(bookName);
        assertEquals("Event Book can not be deleted. It is still in use.", manageEventBooksPage.getAlert().trim());
    }

    private EventBook getTestEventBook() {
        return new EventBook("Test Selenium", new Owner("N4 Systems", "CP"), true);
    }


}
