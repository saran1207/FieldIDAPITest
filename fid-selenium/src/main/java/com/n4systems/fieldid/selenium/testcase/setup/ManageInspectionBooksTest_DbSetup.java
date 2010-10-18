package com.n4systems.fieldid.selenium.testcase.setup;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.InspectionBook;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.setup.ManageInspectionBooksPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ManageInspectionBooksTest_DbSetup extends FieldIDTestCase {

    ManageInspectionBooksPage manageInspBooksPage;

    @Before
    public void setup() {
        manageInspBooksPage = startAsCompany("seafit").systemLogin().clickSetupLink().clickManageInspectionBooks();
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
    public void test_view_all_inpection_books() throws Exception {
        assertEquals("View All", manageInspBooksPage.getCurrentTab());
    }

    @Test
    @Ignore
    public void test_open_and_close_book() throws Exception {
        String status1 = manageInspBooksPage.getFirstListItemStatus();
        String status2, status3;

        if(status1.equals("Open")) {
            manageInspBooksPage.clickFirstListItemClose();
            status2 = manageInspBooksPage.getFirstListItemStatus();
            manageInspBooksPage.verifyInspectionBookSaved();
            manageInspBooksPage.clickFirstListItemOpen();
            status3 = manageInspBooksPage.getFirstListItemStatus();
        }else {
            manageInspBooksPage.clickFirstListItemOpen();
            status2 = manageInspBooksPage.getFirstListItemStatus();
            manageInspBooksPage.verifyInspectionBookSaved();
            manageInspBooksPage.clickFirstListItemClose();
            status3 = manageInspBooksPage.getFirstListItemStatus();
        }

        manageInspBooksPage.verifyInspectionBookSaved();
        assertFalse(status1.equals(status2));
        assertEquals(status1, status3);
    }

    @Test
    @Ignore
    public void test_add_with_error() throws Exception {
        manageInspBooksPage.clickAddTab();
        assertEquals("Add", manageInspBooksPage.getCurrentTab());
        manageInspBooksPage.clickSave();
        assertEquals(2, manageInspBooksPage.getFormErrorMessages().size());
    }

    @Test
    @Ignore
    public void test_add_and_delete_book() throws Exception {
        deleteIfExists("Test Selenium");

        manageInspBooksPage.clickAddTab();
        assertEquals("Add", manageInspBooksPage.getCurrentTab());
        InspectionBook book = getTestInspectionBook();
        manageInspBooksPage.setInspectionBookFormFields(book);
        manageInspBooksPage.clickSave();
        manageInspBooksPage.verifyInspectionBookSaved();
        assertEquals("View All", manageInspBooksPage.getCurrentTab());

        deleteIfExists("Test Selenium");
    }

    private void deleteIfExists(String bookName) {
        if(manageInspBooksPage.listItemExists(bookName)) {
            manageInspBooksPage.clickDelete(bookName);
            manageInspBooksPage.verifyInspectionBookDeleted();
        }
    }

    @Test
    @Ignore
    public void test_delete_book_in_use() throws Exception {
        String bookName = manageInspBooksPage.getFirstListItemName();
        manageInspBooksPage.clickDelete(bookName);
        assertEquals("Inspection Book can not be deleted. It is still in use.", manageInspBooksPage.getAlert().trim());
    }

    private InspectionBook getTestInspectionBook() {
        return new InspectionBook("Test Selenium", new Owner("N4 Systems", "CP"), true);
    }


}
