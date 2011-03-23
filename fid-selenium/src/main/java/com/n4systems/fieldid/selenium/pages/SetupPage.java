package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.pages.setup.AutoAttributeWizardPage;
import com.n4systems.fieldid.selenium.pages.setup.BrandingPage;
import com.n4systems.fieldid.selenium.pages.setup.ColumnLayoutPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetCodeMappingsPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetStatusPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypeGroupsPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypesPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageCommentTemplatesPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageCustomersPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventBooksPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageOrganizationsPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;
import com.n4systems.fieldid.selenium.pages.setup.MangageEventTypeGroupsPage;
import com.n4systems.fieldid.selenium.pages.setup.SystemSettingsPage;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeViewAllPage;
import com.thoughtworks.selenium.Selenium;

public class SetupPage extends FieldIDPage {

	public SetupPage(Selenium selenium) {
		super(selenium);
		if (!selenium.isElementPresent("xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Setup')]")) {
			fail("Expected to be on setup page!");
		}
	}

	public ManageCustomersPage clickManageCustomers() {
        clickOwnersUsersAndLocationsTab();
		selenium.click("//a[.='Manage Customers']");
		return new ManageCustomersPage(selenium);
	}
	
	public SystemSettingsPage clickSystemSettings() {
        clickSettingsTab();
		selenium.click("//a[.='System Settings']");
		return new SystemSettingsPage(selenium);
	}

	public BrandingPage clickBranding() {
        clickSettingsTab();
		selenium.click("//a[.='Branding']");
		return new BrandingPage(selenium);
	}

	public AccountSetupWizardPage clickSetupWizard() {
        clickSettingsTab();
		selenium.click("//a[.='Setup Wizard']");
		return new AccountSetupWizardPage(selenium);
	}

	public ManageUsersPage clickManageUsers() {
        clickOwnersUsersAndLocationsTab();
		selenium.click("//a[.='Manage Users']");
		return new ManageUsersPage(selenium);
	}

	public ManageOrganizationsPage clickManageOrganizations() {
        clickSettingsTab();
		selenium.click("//a[.='Manage Organizations']");
		return new ManageOrganizationsPage(selenium);
	}

	public AutoAttributeWizardPage clickAutoAttributeWizard() {
        clickTemplatesTab();
		selenium.click("//a[.='Auto Attribute Wizard']");
		return new AutoAttributeWizardPage(selenium);
	}

	public MangageEventTypeGroupsPage clickManageEventTypeGroups() {
        clickAssetsAndEventsTab();
		selenium.click("//a[.='Event Type Groups & PDF Report Style']");
		return new MangageEventTypeGroupsPage(selenium);
	}
	
	public ManageCommentTemplatesPage clickManageCommentTemplates() {
        clickTemplatesTab();
		selenium.click("//a[.='Manage Comment Templates']");
		return new ManageCommentTemplatesPage(selenium);
	}
	
    public ColumnLayoutPage clickEditAssetColumnLayout() {
        clickTemplatesTab();
        selenium.click("//a[.='Asset Search Column Layout']");
        return new ColumnLayoutPage(selenium);
    }

    public ColumnLayoutPage clickEditReportingColumnLayout() {
        clickTemplatesTab();
        selenium.click("//a[.='Reporting Column Layout']");
        return new ColumnLayoutPage(selenium);
    }

    public ColumnLayoutPage clickEditScheduleColumnLayout() {
        clickTemplatesTab();
        selenium.click("//a[.='Schedule Column Layout']");
        return new ColumnLayoutPage(selenium);
    }

	public ManageAssetTypeGroupsPage clickAssetTypeGroups() {
        clickAssetsAndEventsTab();
		selenium.click("//a[.='Asset Type Groups']");
		return new ManageAssetTypeGroupsPage(selenium);
	}
	
	public ManageAssetTypesPage clickAssetTypes() {
        clickAssetsAndEventsTab();
		selenium.click("//a[.='Asset Types']");
		return new ManageAssetTypesPage(selenium);
	}

	public ManageAssetStatusPage clickManageAssetStatuses() {
        clickAssetsAndEventsTab();
		selenium.click("//a[.='Asset Statuses']");
		return new ManageAssetStatusPage(selenium);
	}

	public EventTypeViewAllPage clickManageEventTypes() {
        clickAssetsAndEventsTab();
		selenium.click("//a[.='Event Types']");
		return new EventTypeViewAllPage(selenium);
	}

	public ManageEventBooksPage clickManageEventBooks() {
        clickAssetsAndEventsTab();
		selenium.click("//a[.='Event Books']");
		return new ManageEventBooksPage(selenium);
	}

    public ManageAssetCodeMappingsPage clickManageAssetCodeMappings() {
        clickTemplatesTab();
        selenium.click("//a[.='Manage Asset Code Mappings']");
        return new ManageAssetCodeMappingsPage(selenium);
    }
    
	
	public RegistrationRequestPage clickRegistrationRequests(){
		clickOwnersUsersAndLocationsTab();
		selenium.click("link=User Registrations");
		return new RegistrationRequestPage(selenium);
	}

    protected void clickOwnersUsersAndLocationsTab() {
        clickNavOption("Owners, Users & Locations");
    }

    protected void clickSettingsTab() {
        clickNavOption("Settings");
    }

    protected void clickTemplatesTab() {
        clickNavOption("Templates");
    }

    protected void clickAssetsAndEventsTab() {
        clickNavOption("Assets & Events");
    }

}
