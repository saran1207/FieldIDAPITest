package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.pages.setup.*;
import com.n4systems.fieldid.selenium.pages.setup.eventtypes.EventTypeViewAllPage;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.fail;

public class SetupPage extends FieldIDPage {

	public SetupPage(Selenium selenium) {
		super(selenium);
		if (!selenium.isElementPresent("xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Setup')]")) {
			fail("Expected to be on setup page!");
		}
	}

	public ManageCustomersPage clickManageCustomers() {
        clickOwnersUsersAndLocationsTab();
		selenium.click("//a[.='Customers']");
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
		selenium.click("//a[.='Users']");
		return new ManageUsersPage(selenium);
	}

	public ManageOrganizationsPage clickManageOrganizations() {
        clickSettingsTab();
		selenium.click("//a[.='Organizations']");
		return new ManageOrganizationsPage(selenium);
	}

	public AutoAttributeWizardPage clickAutoAttributeWizard() {
        clickTemplatesTab();
		selenium.click("//a[.='Auto Attribute Wizard']");
		return new AutoAttributeWizardPage(selenium);
	}

	public ManageEventTypeGroupsPage clickManageEventTypeGroups() {
        clickAssetsAndEventsTab();
		selenium.click("//a[.='Event Type Groups & PDF Report Style']");
		return new ManageEventTypeGroupsPage(selenium);
	}
	
	public ManageCommentTemplatesPage clickManageCommentTemplates() {
        clickTemplatesTab();
		selenium.click("//a[.='Comment Templates']");
		return new ManageCommentTemplatesPage(selenium);
	}
	
    public ColumnLayoutPage clickEditAssetColumnLayout() {
        clickTemplatesTab();
        selenium.click("//a[.='Search Column Layout']");
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

    protected ImportPage clickImport() {
        clickNavOption("Import");
        return new ImportPage(selenium);
    }

}
