package com.n4systems.fieldid.selenium.pages;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.components.UnitOfMeasurePicker;
import com.n4systems.fieldid.selenium.datatypes.Identifier;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.datatypes.SafetyNetworkRegistration;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;
import com.thoughtworks.selenium.Selenium;

public class IdentifyPage extends FieldIDPage {
		
	public IdentifyPage(Selenium selenium) {
		super(selenium);
		if(!checkOnIdentifyPage()){
			fail("Expected to be on identify page!");
		}
	}
	
	public boolean checkOnIdentifyPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Identify')]");
	}
	
	public void selectProductType(String productType) {
		selenium.select("//select[@id='productType']", productType);
		selenium.fireEvent("//select[@id='productType']", "change");
		waitForAjax();
	}
	
	public void selectAttributeValue(String attribute, String value) {
		String selectXpath = "//div[@id='infoOptions']//div[@infofieldname='"+attribute+"']//select"; 
		selenium.select(selectXpath, value);
		selenium.fireEvent(selectXpath, "change");
		waitForAjax();
	}
	
	public String getAttributeValue(String attribute) {
		return selenium.getValue("//div[@id='infoOptions']//div[@infofieldname='"+attribute+"']//input[@type='text']");
	}

	public String getAttributeSelectValue(String attribute) {
		return selenium.getSelectedLabel("//div[@id='infoOptions']//div[@infofieldname='"+attribute+"']//select");
	}
	
	public void setOrderNumber(String orderNumber) {
		selenium.type("//form[@id='searchOrder']//input[@name='orderNumber']", orderNumber);
	}
	
	public void clickLoadOrderNumberButton() {
		selenium.click("//form[@id='searchOrder']/input[@id='searchOrder_load']");
		waitForPageToLoad();
	}
	
	public int getNumberOfLineItemsInOrder() {
		return selenium.getXpathCount("//div[@id='resultsTable']/table/tbody/tr/td[1]/..").intValue();
	}
	
	public void clickIdentifyForOrderLineItem(int index) {
		String locator = "//div[@id='resultsTable']/table/tbody/tr[" + index + "]/td/a[contains(text(),'Identify')]";
		if(selenium.isElementPresent(locator)) {
			selenium.click(locator);
			waitForPageToLoad();
		} else {
			fail("Could not find a line item in row '" + index + "'");
		}
	}
	
	public boolean checkIdentifyWithOrderNumberPage(String orderNumber) {
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Identify')][contains(text(),'Order Number " + orderNumber + "')]");
	}
	
	public Product setAddAssetForm(Product p, boolean generate) {
		if(generate) {
			selenium.click("//a[contains(text(),'generate')]");
		}
		if(p.getSerialNumber() != null) {
			selenium.type("//input[@id='serialNumberText']", p.getSerialNumber());
		}
		if(p.getRFIDNumber() != null) {
			selenium.type("//input[@id='rfidNumber']", p.getRFIDNumber());
		}
		if(p.getReferenceNumber() != null) {
			selenium.type("//input[@id='customerRefNumber']", p.getReferenceNumber());
		}
		if(p.getSafetyNetworkRegistration() != null) {
			setRegisterThisAssetOverTheSafetyNetwork(p.getSafetyNetworkRegistration());
		}
		if(p.getPublished()) {
			selenium.select("//select[@id='productCreate_publishedState']", "Publish");
		} else {
			selenium.select("//select[@id='productCreate_publishedState']", "Do Not Publish");
		}
		if(p.getOwner() != null) {
			OrgPicker orgPicker = getOrgPicker();
			orgPicker.clickChooseOwner();
			orgPicker.setOwner(p.getOwner());
			orgPicker.clickSelectOwner();
		}
		if(p.getLocation() != null) {
			selenium.type("//input[@id='location_freeformLocation']", p.getLocation());
		}
		if(p.getProductStatus() != null) {
			selenium.select("//select[@id='productCreate_productStatus']", p.getProductStatus());
		}
		if(p.getPurchaseOrder() != null) {
			selenium.type("//input[@id='productCreate_purchaseOrder']", p.getPurchaseOrder());
		}
		if(p.getIdentified() != null) {
			selenium.type("//input[@id='identified']", p.getIdentified());
		}
		if(p.getProductType() != null) {
			selenium.select("//select[@id='productType']", p.getProductType());
			selenium.fireEvent("//select[@id='productType']", "change");
			waitForAjax();
		}
		setRequiredProductAttributes();
		
		if(p.getComments() != null) {
			selenium.type("//textarea[@id='comments']", p.getComments());
		}
		
		// copy everything back so any auto-filled fields get retrieved
		// for example, generated serial number or identified field.
		p = getAddAssetForm();
		
		return p;
	}
	
	public void setRegisterThisAssetOverTheSafetyNetwork(SafetyNetworkRegistration registration) {
		if(selenium.isElementPresent("//a[@id='showSmartSearchLink']")) {
			selenium.click("//a[@id='showSmartSearchLink']");
			String vendor = registration.getVendor();
			if(isOptionPresent("//select[@id='snSmartSearchVendors']", vendor)) {
				selenium.select("//select[@id='snSmartSearchVendors']", vendor);
			} else {
				fail("Could not find the vendor '" + vendor + "' on the list of vendors");
			}
			String asset = registration.getAssetNumber();
			selenium.type("//input[@id='snSmartSearchText']", asset);
			selenium.click("//input[@id='snSmartSearchSubmit']");
			waitForAjax(MiscDriver.DEFAULT_TIMEOUT);
			assertFalse("Could not find vendor '" + vendor + "', asset '" + asset + "'", selenium.isElementPresent("div[@id='snSmartSearchResults']"));
		} else {
			fail("Could not find a link to register the asset over the safety network");
		}
	}
	
	public void setRequiredProductAttributes() {
		List<String> requiredSelectListsIDs = getRequiredSelectListIDs();
		setRequiredSelectLists(requiredSelectListsIDs);
		List<String> requiredTextFieldIDs = getRequiredTextFieldIDs();
		setRequiredUnitsOfMeasure(requiredTextFieldIDs);
		setRequiredTextFields(requiredTextFieldIDs);
	}
	
	private void setRequiredSelectLists(List<String> requiredSelectListsIDs) {
		for (String id : requiredSelectListsIDs) {
			String locator = "//select[contains(@class,'requiredField') and @id='" + id + "']";
			if(selenium.isElementPresent(locator)) {
				selenium.select(locator, "index=1");
			}
		}
	}
	
	private void setRequiredUnitsOfMeasure(List<String> requiredTextFieldIDs) {
		for (String id : requiredTextFieldIDs) {
			Random r = new Random();
			String value = Integer.toString(Math.abs((r.nextInt(100)+1)));
			UnitOfMeasurePicker picker = new UnitOfMeasurePicker(selenium, id);
			picker.setUnitOfMeasure(id, value);
		}
	}
		
	private List<String> getRequiredSelectListIDs() {
		return getRequiredFieldsByTagTypeUsingXpath("select");
	}
	
	private List<String> getRequiredTextFieldIDs() {
		return getRequiredFieldsByTagTypeUsingXpath("input");
	}

	private List<String> getRequiredFieldsByTagTypeUsingXpath(String tagType) {
		List<String> result = new ArrayList<String>();
		String baseXpath = "//"+tagType+"[contains(@class,'requiredField')]";
		int number = selenium.getXpathCount(baseXpath).intValue();
		
		for (int i = 1; i <= number; i++) {
			String id = selenium.getAttribute("xpath=("+baseXpath+")["+i+"]/@id");
			result.add(id);
		}
		
		return result;
	}
	
	public Product getAddAssetForm() {
		Product p = new Product();
		p.setSerialNumber(selenium.getValue("//input[@id='serialNumberText']"));
		p.setRFIDNumber(selenium.getValue("//input[@id='rfidNumber']"));
		p.setReferenceNumber(selenium.getValue("//input[@id='customerRefNumber']"));
		p.setSafetyNetworkRegistration(null);
		boolean publish = selenium.getSelectedLabel("//select[@id='productCreate_publishedState']").equals("Publish");
		p.setPublished(publish);
		
		OrgPicker orgPicker = getOrgPicker();
		orgPicker.clickChooseOwner();
		Owner owner = orgPicker.getOwner();
		orgPicker.clickCancelOwner();
		
		p.setOwner(owner);
		if (selenium.isElementPresent("//input[@id='location_freeformLocation']")) {
			p.setLocation(selenium.getValue("//input[@id='location_freeformLocation']"));
		}
		p.setProductStatus(selenium.getSelectedLabel("//select[@id='productCreate_productStatus']"));
		p.setPurchaseOrder(selenium.getValue("//input[@id='productCreate_purchaseOrder']"));
		p.setIdentified(selenium.getValue("//input[@id='identified']"));
		p.setProductType(selenium.getSelectedLabel("//select[@id='productType']"));
		p.setComments(selenium.getValue("//textarea[@id='comments']"));
		return p;
	}
	
	private void setRequiredTextFields(List<String> requiredTextFieldIDs) {
		for (String id : requiredTextFieldIDs) {
			String locator = "//input[contains(@class,'requiredField') and not(contains(@class,'unitOfMeasure')) and @id='" + id + "']";
			if(selenium.isElementPresent(locator) && selenium.getValue(locator).equals("")) {
				String value = MiscDriver.getRandomString(8);
				selenium.type(locator, value);
			}
		}
	}
	
	public void saveNewAsset() {
		selenium.click("//input[@id='saveButton']");
		waitForPageToLoad();
	}
	
	public void clickAdd() {
		clickNavOption("Add");
	}

	public void clickMultiAdd() {
		clickNavOption("Multi Add", "60000");
	}
	
	public List<String> getProductStatusesFromMultiAddForm() {
		List<String> results = new ArrayList<String>();
		String s[] = selenium.getSelectOptions("//select[@id='step1form_productStatus']");
		for (String status : s) {
			if (!status.equals("")) {
				results.add(status);
			}
		}
		return results;
	}
	
	public void setMultiAddStep1Form(Product p) {
		if(p.getOwner() != null) {
			OrgPicker orgPicker = getOrgPicker();
			orgPicker.clickChooseOwner();
			orgPicker.setOwner(p.getOwner());
			orgPicker.clickSelectOwner();
		}
		if(p.getLocation() != null) {
			selenium.type("//input[@id='location_freeformLocation']", p.getLocation());
		}
		if(p.getProductStatus() != null) {
			selenium.select("//select[@id='step1form_productStatus']", p.getProductStatus());
		}
		if(p.getPurchaseOrder() != null) {
			selenium.type("//input[@id='step1form_purchaseOrder']", p.getPurchaseOrder());
		}
		if(p.getIdentified() != null) {
			selenium.type("//input[@id='identified']", p.getIdentified());
		}
		if(p.getProductType() != null) {
			selenium.select("//select[@id='productType']", p.getProductType());
			selenium.fireEvent("//select[@id='productType']", "change");
			waitForAjax();
		}
		
		setRequiredProductAttributes();
		if(p.getComments() != null) {
			selenium.type("//textarea[@id='comments']", p.getComments());
		}
		
	}

	public void clickContinueButtonMultiAddStep1() {
		selenium.click("//input[@id='step1form_label_continue']");
		waitForStep1FormClose();
		checkForErrorMessages("MultiAddStep1");
	}
	
	private void waitForStep1FormClose() {
		new ConditionWaiter(new Predicate() {
			@Override
			public boolean evaluate() {
				return selenium.isElementPresent("//div[contains(@class,'stepClosed')]/form[@id='step1form']");
			}
		}).run();
	}
	
	private void waitForStep4FormOpen() {
		new ConditionWaiter(new Predicate() {
			@Override
			public boolean evaluate() {
				return selenium.isElementPresent("//div[@class='step']//form[@id='step4form']");
			}
		}).run();
	}
	
	public void setMultiAddStep2Form(int quantity) {
		selenium.type("//input[@id='quantity']", Integer.toString(quantity));
	}
	
	public void clickContinueButtonMultiAddStep2() {
		selenium.click("//input[@id='step2next']");
		waitForAjax();
		checkForErrorMessages("MultiAddStep2");
	}
	
	public void setMultiAddStep3FormRange(String prefix, String start, String suffix) {
		selenium.check("//input[@id='ident']");
		if(prefix != null) {
			selenium.type("//input[@id='prefix']", prefix);
		}
		if(start != null) {
			selenium.type("//input[@id='start']", start);
		}
		if(suffix != null) {
			selenium.type("//input[@id='suffix']", suffix);
		}
	}

	public void clickContinueButtonMultiAddStep3() {
		selenium.click("//input[@id='step23Form_label_continue']");
		checkForErrorMessages("MultiAddStep3");
		waitForIdentifiersToBeGenerated(30);
	}
	
	public List<Identifier> setMultiAddStep4Form(List<Identifier> identifiers) {
		List<Identifier> results = new ArrayList<Identifier>();
		boolean set = false;
		if(identifiers != null)	set = true;
		
		Number n = selenium.getXpathCount("//div[@class='identifierRow']/div/input[contains(@id,'serial_')]");
		int numRows = n.intValue();
		int currentRow = 0;
		String serialNumberLocator = "//input[@id='serial_" + currentRow + "']";
		String rfidNumberLocator = "//input[@id='step4form_identifiers_" + currentRow + "__rfidNumber']";
		String referenceNumberLocator = "//input[@id='step4form_identifiers_" + currentRow + "__referenceNumber']";

		for(int i = 0; i < numRows; i++, currentRow++) {
			if(set && identifiers.size() > i) {
				Identifier in = identifiers.get(i);
				if(in.getSerialNumber() != null) {
					selenium.type(serialNumberLocator, in.getSerialNumber());
				}
				if(in.getRfidNumber() != null) {
					selenium.type(rfidNumberLocator, in.getRfidNumber());
				}
				if(in.getReferenceNumber() != null) {
					selenium.type(referenceNumberLocator, in.getReferenceNumber());
				}
			}
			String serial = selenium.getValue(serialNumberLocator);
			String rfid = selenium.getValue(rfidNumberLocator);
			String ref = selenium.getValue(referenceNumberLocator);
			Identifier out = new Identifier(serial, rfid, ref);
			results.add(out);
			serialNumberLocator = serialNumberLocator.replaceFirst("_" + currentRow, "_" + (currentRow+1));
			rfidNumberLocator = rfidNumberLocator.replaceFirst("_" + currentRow, "_" + (currentRow+1));
			referenceNumberLocator = referenceNumberLocator.replaceFirst("_" + currentRow, "_" + (currentRow+1));
		}
		return results;
	}

	public void clickSaveAndCreateButtonMultiAddStep4() {
		waitForStep4FormOpen();		
		selenium.click("//input[@id='saveButton']");
		checkForErrorMessages("MultiAddStep4");
		waitForPageToLoad("40000");
	}

	private void waitForIdentifiersToBeGenerated(int timeoutSeconds) {
		new ConditionWaiter(new Predicate() {
			@Override
			public boolean evaluate() {
				return selenium.isElementPresent("//div[@id='step4Loading' and not(contains(@style,'display: none'))]");
			}
		}).run();
	}
}
