package com.n4systems.fieldid.selenium.identify.page;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.n4systems.fieldid.selenium.datatypes.Asset;
import com.n4systems.fieldid.selenium.datatypes.Identifier;
import com.n4systems.fieldid.selenium.datatypes.LineItem;
import com.n4systems.fieldid.selenium.datatypes.Order;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.datatypes.SafetyNetworkRegistration;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class IdentifyPageDriver {

	private FieldIdSelenium selenium;
	private MiscDriver misc;
	
	// Locators
	private String identifyPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Identify')]";
	private String identifyAddSerialNumberTextFieldLocator = "xpath=//INPUT[@id='serialNumberText']";
	private String identifyAddGenerateLinkLocator = "xpath=//A[contains(text(),'generate')]";
	private String identifyAddRFIDNumberTextFieldLocator = "xpath=//INPUT[@id='rfidNumber']";
	private String identifyAddReferenceNumberTextFieldLocator = "xpath=//INPUT[@id='customerRefNumber']";
	private String identifyAddLocationTextFieldLocator = "xpath=//INPUT[@id='location_freeformLocation']";
	private String identifyAddAssetStatusSelectListLocator = "xpath=//SELECT[@id='assetCreate_assetStatus']";
	private String identifyAddPurchaseOrderTextFieldLocator = "xpath=//INPUT[@id='assetCreate_purchaseOrder']";
	private String identifyAddIdentifiedTextFieldLocator = "xpath=//INPUT[@id='identified']";
	private String identifyAddAssetTypeTextFieldLocator = "xpath=//SELECT[@id='assetType']";
	private String identifyAddOwnerTextFieldLocator = "xpath=//INPUT[@id='assetCreate_owner_orgName']";
	private String identifyAddChooseLinkLocator = "xpath=//A[contains(text(),'Choose')]";
	private String identifyAddAttachAFileButtonLocator = "xpath=//BUTTON[contains(text(),'Attach A File')]";
	private String identifyAddSaveButtonLocator = "xpath=//INPUT[@id='saveButton']";
	private String identifyAddSaveAndInspectButtonLocator = "xpath=//INPUT[@id='saveAndInspButton']";
	private String identifyAddSaveAndPrintButtonLocator = "xpath=//INPUT[@id='saveAndPrintButton']";
	private String identifyAddSaveAndScheduleButtonLocator = "xpath=//INPUT[@id='saveAndScheduleButton']";
	private String addWithOrderSelectedLocator = "xpath=//UL[contains(@class,'options')]/LI[contains(@class,'add') and contains(@class,'selected') and contains(text(),'Add with Order')]";
	private String addMultiSelectedLocator = "xpath=//UL[contains(@class,'options')]/LI[contains(@class,'add') and contains(@class,'selected') and contains(text(),'Multi Add')]";
	private String addSelectedLocator = "xpath=//UL[contains(@class,'options')]/LI[contains(@class,'add') and contains(@class,'selected') and contains(text(),'Add') and not(contains(text(),'Multi')) and not(contains(text(),'with Order'))]";
	private String addLinkLocator = "xpath=//UL[contains(@class,'options')]/LI[contains(@class,'add')]/A[contains(text(),'Add') and not(contains(text(),'Multi')) and not(contains(text(),'with Order'))]";
	private String multiAddLinkLocator = "xpath=//UL[contains(@class,'options')]/LI[contains(@class,'add')]/A[contains(text(),'Multi Add')]";
	private String identifyAddPublishOverSafetyNetworkSelectListLocator = "xpath=//SELECT[@id='assetCreate_publishedState']";
	private String identifyAddCommentsTextFieldLocator = "xpath=//TEXTAREA[@id='comments']";
	private String assetTypeAttributesUpdatingLocator = "xpath=//SPAN[id='assetTypeIndicator' and not(contains(@style,'visibility: hidden'))]";
	private String classStringIdentifyingRequiredFields = "requiredField";
	private String classStringIdentifyingUnitOfMeasureFields = "unitOfMeasure";
	private String registerThisAssetOverTheSafetyNetworkLinkLocator = "xpath=//A[@id='showSmartSearchLink']";
	private String registerAssetVendorSelectListLocator = "xpath=//select[@id='snSmartSearchVendors']";
	private String registerAssetSerialRFIDReferenceNumberTextFieldLocator = "xpath=//input[@id='snSmartSearchText']";
	private String registerAssetLoadButtonLocator = "xpath=//input[@id='snSmartSearchSubmit']";
	private String couldNotFindAnyPublishedAssetTextLocator = "xpath=//div[@id='snSmartSearchResults']";
	private String identifySelectedTabLocator = "xpath=//ul[contains(@class,'options')]/li[contains(@class,'selected')]";
	private String addWithOrderFormLocator = "xpath=//form[@id='searchOrder']";
	private String addWithOrderLabelLocator = addWithOrderFormLocator + "/label[contains(text(),'Order Number')]";
	private String addWithOrderTextFieldLocator = addWithOrderFormLocator + "/span/input[@id='searchOrder_orderNumber']";
	private String addWithOrderLoadButtonLocator = addWithOrderFormLocator + "/input[@id='searchOrder_load']";
	private String orderDetailsOrderNumberLocator = "xpath=//LABEL[contains(text(),'Order Number')]/../span";
	private String orderDetailsOrderDateLocator = "xpath=//LABEL[contains(text(),'Order Date')]/../span";
	private String orderDetailsDescriptionLocator = "xpath=//LABEL[contains(text(),'Description')]/../span";
	private String orderDetailsPurchaseOrderLocator = "xpath=//LABEL[contains(text(),'Purchase Order')]/../span";
	private String orderDetailsCustomerLocator = "xpath=//LABEL[contains(text(),'Customer')]/../span";
	private String orderDetailsDivisionLocator = "xpath=//LABEL[contains(text(),'Division')]/../span";
	private String orderDetailsLineItemsTableXpath = "//div[@id='resultsTable']/table";
	private String orderDetailsLineItemsXpathCount = orderDetailsLineItemsTableXpath + "/tbody/tr/td[1]/..";
	private String multiAddAssetStatusSelectLocator = "xpath=//select[@id='step1form_assetStatus']";
	private String multiAddLocationTextFieldLocator = "xpath=//input[@id='location_freeformLocation']";
	private String multiAddPurchaseOrderTextFieldLocator = "xpath=//input[@id='step1form_purchaseOrder']";
	private String multiAddIdentifiedTextFieldLocator = "xpath=//input[@id='identified']";
	private String multiAddAssetTypeSelectListLocator = "xpath=//select[@id='assetType']";
	private String multiAddCommentTextFieldLocator = "xpath=//TEXTAREA[@id='comments']";
	private String multiAddAttachAFileButtonLocator = "xpath=//button[contains(text(),'Attach A File')]";
	private String multiAddStep1ContinueButtonLocator = "xpath=//input[@id='step1form_label_continue']";
	private String multiAddCancelButtonLocator = "xpath=//input[@id='cancel']";
	private String multiAddQuantityTextFieldLocator = "xpath=//input[@id='quantity']";
	private String multiAddStep2ContinueButtonLocator = "xpath=//input[@id='step2next']";
	private String multiAddBackToStep1LinkLocator = "xpath=//A[contains(text(),'Back To Step 1')]";
	private String multiAddPrefixTextFieldLocator = "xpath=//input[@id='prefix']";
	private String multiAddStartTextFieldLocator = "xpath=//input[@id='start']";
	private String multiAddSuffixTextFieldLocator = "xpath=//input[@id='suffix']";
	private String multiAddIdentifierTextFieldLocator = "xpath=//input[@id='ident']";
	private String multiAddRangeRadioButtonLocator = "xpath=//input[@id='ident']";
	private String multiAddNonSerializedRadioButtonLocator = "xpath=//input[@id='snAuto']";
	private String multiAddBatchNumberRadioButtonLocator = "xpath=//input[@id='snBatch']";
	private String multiAddManualRadioButtonLocator = "xpath=//input[@id='snManual']";
	private String multiAddStep3ContinueButtonLocator = "xpath=//input[@id='step23Form_label_continue']";
	private String multiAddBackToStep2LinkLocator = "xpath=//a[contains(text(),'Back To Step 2')]";
	private String multiAddStep4NumberOfRowsXpath = "//div[@class='identifierRow']/div/input[contains(@id,'serial_')]";
	private String multiAddSerialNumber1TextFieldLocator = "xpath=//input[@id='serial_0']";
	private String multiAddSerialNumber2TextFieldLocator = "xpath=//input[@id='serial_1']";
	private String multiAddRFIDNumber1TextFieldLocator = "xpath=//input[@id='step4form_identifiers_0__rfidNumber']";
	private String multiAddRFIDNumber2TextFieldLocator = "xpath=//input[@id='step4form_identifiers_0__rfidNumber']";
	private String multiAddReferenceNumber1TextFieldLocator = "xpath=//input[@id='step4form_identifiers_0__referenceNumber']";
	private String multiAddReferenceNumber2TextFieldLocator = "xpath=//input[@id='step4form_identifiers_0__referenceNumber']";
	private String multiAddSaveAndCreateButtonLocator = "css=#saveButton";
	private String multiAddBackToStep3LinkLocator = "xpath=//a[contains(text(),'Back To Step 3')]";

	public IdentifyPageDriver(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void assertIdentifyPageHeader() {
		assertTrue("Could not find the header for the Identify page", selenium.isElementPresent(identifyPageHeaderLocator));
		misc.checkForErrorMessages(null);
	}
	
	public void verifyIdentifyAddPage() {
		assertTrue("Could not find the text field for Serial Number on Assets Add", selenium.isElementPresent(identifyAddSerialNumberTextFieldLocator));
		assertTrue("Could not find the link for generate on Assets Add", selenium.isElementPresent(identifyAddGenerateLinkLocator));
		assertTrue("Could not find the text field for RFID Number on Assets Add", selenium.isElementPresent(identifyAddRFIDNumberTextFieldLocator));
		assertTrue("Could not find the text field for Reference Number on Assets Add", selenium.isElementPresent(identifyAddReferenceNumberTextFieldLocator));
		assertTrue("Could not find the select list to Register Asset on the Safety Network", selenium.isElementPresent(identifyAddPublishOverSafetyNetworkSelectListLocator));
		assertTrue("Could not find the text field for Owner on Assets Add", selenium.isElementPresent(identifyAddOwnerTextFieldLocator));
		assertTrue("Could not find the link for Choose Owner on Assets Add", selenium.isElementPresent(identifyAddChooseLinkLocator));
		assertTrue("Could not find the select list for Asset Status on Assets Add", selenium.isElementPresent(identifyAddAssetStatusSelectListLocator));
		assertTrue("Could not find the text field for Purchase Order on Assets Add", selenium.isElementPresent(identifyAddPurchaseOrderTextFieldLocator));
		assertTrue("Could not find the text field for Identified on Assets Add", selenium.isElementPresent(identifyAddIdentifiedTextFieldLocator));
		assertTrue("Could not find the text field for Asset Type on Assets Add", selenium.isElementPresent(identifyAddAssetTypeTextFieldLocator));
		assertTrue("Could not find the text area for Comments", selenium.isElementPresent(identifyAddCommentsTextFieldLocator));
		assertTrue("Could not find the button for Attach A File on Assets Add", selenium.isElementPresent(identifyAddAttachAFileButtonLocator));
		assertTrue("Could not find the button for Save on Assets Add", selenium.isElementPresent(identifyAddSaveButtonLocator));
		assertTrue("Could not find the button for Save And Inspect on Assets Add", selenium.isElementPresent(identifyAddSaveAndInspectButtonLocator));
		assertTrue("Could not find the button for Save And Print on Assets Add", selenium.isElementPresent(identifyAddSaveAndPrintButtonLocator));
		assertTrue("Could not find the button for Save And Schedule on Assets Add", selenium.isElementPresent(identifyAddSaveAndScheduleButtonLocator));
	}
	
	/**
	 * Returns true if there is a link to Add With Order.
	 * If you are on the Add With Order page, this will
	 * return false.
	 * 
	 * @return
	 */
	public boolean isAddWithOrder() {
		return selenium.isElementPresent(addWithOrderSelectedLocator);
	}
	
	/**
	 * Returns true if there is a link to Add.
	 * If you are on the Add page, this will
	 * return false.
	 * 
	 * @return
	 */
	public boolean isAdd() {
		return selenium.isElementPresent(addSelectedLocator);
	}
	
	/**
	 * Returns true if there is a link to Multi Add.
	 * If you are on the Multi Add page, this will
	 * return false.
	 * 
	 * @return
	 */
	public boolean isMultiAdd() {
		return selenium.isElementPresent(addMultiSelectedLocator);
	}

	/**
	 * Click the Add tab to go to the Add page.
	 */
	public void gotoAdd() {
		if(selenium.isElementPresent(addLinkLocator)) {
			selenium.click(addLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Will fill in the Add Assets page (single asset creation)
	 * with the fields of the Asset p. Currently, the implementation
	 * of Asset does not include asset type attributes. So for any
	 * required asset type attributes, this will fill them in with
	 * random values.
	 * 
	 *  If any other required fields are blank, this will not attempt
	 *  to fill them in, i.e. serial number and identified date.
	 *  
	 *  If the generate parameter is true, this will use the generate
	 *  link to create a new serial number and save that serial number
	 *  in the provided Asset. If there is a serial number and the
	 *  generate parameter is true, a new serial number will be generated
	 *  then the input serial number will replace with the generated
	 *  serial number, i.e. click generate then type the provided
	 *  serial number over top of the generated serial number.
	 *  
	 *  Typically, if you want to create an asset with no inputs from
	 *  the Asset, you should be able to pass in a:
	 *  
	 *   	setAddAssetForm(new Asset(), true);
	 *   
	 *  and it will return the filled in asset information.
	 *  
	 * @param p
	 * @param generate
	 * @return
	 * @throws InterruptedException 
	 */
	public Asset setAddAssetForm(Asset p, boolean generate) throws InterruptedException {
		assertNotNull(p);
		verifyIdentifyAddPage();
		if(generate) {
			selenium.click(identifyAddGenerateLinkLocator);
		}
		if(p.getSerialNumber() != null) {
			selenium.type(identifyAddSerialNumberTextFieldLocator, p.getSerialNumber());
		}
		if(p.getRFIDNumber() != null) {
			selenium.type(identifyAddRFIDNumberTextFieldLocator, p.getRFIDNumber());
		}
		if(p.getReferenceNumber() != null) {
			selenium.type(identifyAddReferenceNumberTextFieldLocator, p.getReferenceNumber());
		}
		if(p.getSafetyNetworkRegistration() != null) {
			setRegisterThisAssetOverTheSafetyNetwork(p.getSafetyNetworkRegistration());
		}
		if(p.getPublished()) {
			selenium.select(identifyAddPublishOverSafetyNetworkSelectListLocator, "Publish");
		} else {
			selenium.select(identifyAddPublishOverSafetyNetworkSelectListLocator, "Do Not Publish");
		}
		if(p.getOwner() != null) {
			misc.gotoChooseOwner();
			misc.setOwner(p.getOwner());
			misc.gotoSelectOwner();
		}
		if(p.getLocation() != null) {
			selenium.type(identifyAddLocationTextFieldLocator, p.getLocation());
		}
		if(p.getAssetStatus() != null) {
			selenium.select(identifyAddAssetStatusSelectListLocator, p.getAssetStatus());
		}
		if(p.getPurchaseOrder() != null) {
			selenium.type(identifyAddPurchaseOrderTextFieldLocator, p.getPurchaseOrder());
		}
		if(p.getIdentified() != null) {
			selenium.type(identifyAddIdentifiedTextFieldLocator, p.getIdentified());
		}
		if(p.getAssetType() != null) {
			selenium.select(identifyAddAssetTypeTextFieldLocator, p.getAssetType());
			waitForAssetTypeAttributesToChange(MiscDriver.DEFAULT_TIMEOUT);
		}
		setRequiredAssetAttributes();
		
		if(p.getComments() != null) {
			selenium.type(identifyAddCommentsTextFieldLocator, p.getComments());
		}
		
		// copy everything back so any auto-filled fields get retrieved
		// for example, generated serial number or identified field.
		p = getAddAssetForm();
		
		return p;
	}

	private void waitForAssetTypeAttributesToChange(String defaultTimeout) {
		int maxSeconds = Integer.parseInt(defaultTimeout);
		boolean updating = true;
		int secondsLeft = maxSeconds;
		do {
			updating = selenium.isElementPresent(assetTypeAttributesUpdatingLocator);
			misc.sleep(1000);
			secondsLeft--;
		} while(updating && secondsLeft > 0);
		misc.sleep(3000);
	}

	public void setRequiredAssetAttributes() {
		String source = selenium.getHtmlSource();
		List<String> requiredSelectListsIDs = getRequiredSelectListIDs(source);
		setRequiredSelectLists(requiredSelectListsIDs);
		List<String> requiredTextFieldIDs = getRequiredTextFieldIDs(source);
		setRequiredUnitsOfMeasure(requiredTextFieldIDs);
		setRequiredTextFields(requiredTextFieldIDs);
		misc.sleep(2000);
	}

	private void setRequiredTextFields(List<String> requiredTextFieldIDs) {
		for (String id : requiredTextFieldIDs) {
			String locator = "xpath=//INPUT[contains(@class,'" + classStringIdentifyingRequiredFields
				+ "') and not(contains(@class,'" 
				+ classStringIdentifyingUnitOfMeasureFields + "')) and @id='" 
				+ id + "']";
			if(selenium.isElementPresent(locator) && selenium.getValue(locator).equals("")) {
				String value = MiscDriver.getRandomString(8);
				selenium.type(locator, value);
			}
		}
	}

	private void setRequiredUnitsOfMeasure(List<String> requiredTextFieldIDs) {
		for (String id : requiredTextFieldIDs) {
			Random r = new Random();
			String value = Integer.toString(Math.abs((r.nextInt(100)+1)));
			setUnitOfMeasure(id, value);
		}
	}
	
	/**
	 * Given the id attribute of the Unit Of Measure INPUT tag,
	 * this will open the input dialog and fill in the dialog
	 * with the given value.
	 * 
	 * It does not change the unit of measure type but uses the
	 * default type.
	 * 
	 * For unit of measures which have more than one input,
	 * this will fill in the first value only.
	 * 
	 * If the field already has a value this will not change it.
	 * 
	 * @param id
	 * @param value
	 */
	public void setUnitOfMeasure(String id, String value) {
		String locator = "xpath=//INPUT[contains(@class,'" + classStringIdentifyingUnitOfMeasureFields + "') and @id='" + id + "']";
		if(selenium.isElementPresent(locator)) {
			String anchor = locator + "/../../SPAN[@class='action']/A[contains(@id,'" + id + "')]";
			selenium.click(anchor);
			waitForUnitOfMeasureDialogToOpen(id);
			String unitOfMeasureIDPrefix = "unitOfMeasureId_";
			String typeOfUnitOfMeasureSelectListLocator = "//SELECT[contains(@id,'" + unitOfMeasureIDPrefix + id + "')]";
			String typeOfUnitOfMeasure = selenium.getSelectedValue(typeOfUnitOfMeasureSelectListLocator);
			String unitOfMeasureInputLocator = "//INPUT[@id='" + typeOfUnitOfMeasure + "_" + id + "']";
			if(selenium.getValue(unitOfMeasureInputLocator).equals("")) {
				selenium.type(unitOfMeasureInputLocator, value);
			}
			String submitButton = "css=#unitOfMeasureForm_" + id + "_hbutton_submit";
			selenium.click(submitButton);
			selenium.waitForAjax();
		}
	}

	private void waitForUnitOfMeasureDialogToOpen(String id) {
		misc.sleep(3000);
	}

	private void setRequiredSelectLists(List<String> requiredSelectListsIDs) {
		for (String id : requiredSelectListsIDs) {
			String locator = "xpath=//SELECT[contains(@class,'" + classStringIdentifyingRequiredFields + "') and "
				+ "@id='" + id + "']";
			if(selenium.isElementPresent(locator)) {
				selenium.select(locator, "index=1");
			}
		}
	}

	private List<String> getRequiredTextFieldIDs(String source) {
		return getRequiredFieldsByTagTypeUsingXpath(source, "input");
	}

	private List<String> getRequiredSelectListIDs(String source) {
		return getRequiredFieldsByTagTypeUsingXpath(source, "select");
	}
	
	private List<String> getRequiredFieldsByTagTypeUsingXpath(String source, String tagType) {
		List<String> result = new ArrayList<String>();
		String baseXpath = "//"+tagType+"[contains(@class,'"+classStringIdentifyingRequiredFields+"')]";
		int number = selenium.getXpathCount(baseXpath).intValue();
		
		for (int i = 1; i <= number; i++) {
			String id = selenium.getAttribute("xpath=(//"+tagType+"[contains(@class,'"+classStringIdentifyingRequiredFields+"')])["+i+"]/@id");
			result.add(id);
		}
		
		return result;
	}
	
	public void setRegisterThisAssetOverTheSafetyNetwork(SafetyNetworkRegistration registration) throws InterruptedException {
		if(selenium.isElementPresent(registerThisAssetOverTheSafetyNetworkLinkLocator)) {
			selenium.click(registerThisAssetOverTheSafetyNetworkLinkLocator);
			misc.sleep(2000);
			String vendor = registration.getVendor();
			if(misc.isOptionPresent(registerAssetVendorSelectListLocator, vendor)) {
				selenium.select(registerAssetVendorSelectListLocator, vendor);
			} else {
				fail("Could not find the vendor '" + vendor + "' on the list of vendors");
			}
			String asset = registration.getAssetNumber();
			selenium.type(registerAssetSerialRFIDReferenceNumberTextFieldLocator, asset);
			selenium.click(registerAssetLoadButtonLocator);
			selenium.waitForAjax(MiscDriver.DEFAULT_TIMEOUT);
			assertFalse("Could not find vendor '" + vendor + "', asset '" + asset + "'", selenium.isElementPresent(couldNotFindAnyPublishedAssetTextLocator));
		} else {
			fail("Could not find a link to register the asset over the safety network");
		}
	}

	public Asset getAddAssetForm() {
		Asset p = new Asset();
		p.setSerialNumber(selenium.getValue(identifyAddSerialNumberTextFieldLocator));
		p.setRFIDNumber(selenium.getValue(identifyAddRFIDNumberTextFieldLocator));
		p.setReferenceNumber(selenium.getValue(identifyAddReferenceNumberTextFieldLocator));
		p.setSafetyNetworkRegistration(null);
		boolean publish = selenium.getSelectedLabel(identifyAddPublishOverSafetyNetworkSelectListLocator).equals("Publish");
		p.setPublished(publish);
		misc.gotoChooseOwner();
		Owner owner = misc.getOwner();
		misc.gotoCancelOwner();
		p.setOwner(owner);
		if (selenium.isElementPresent(identifyAddLocationTextFieldLocator)) {
			p.setLocation(selenium.getValue(identifyAddLocationTextFieldLocator));
		}
		p.setAssetStatus(selenium.getSelectedLabel(identifyAddAssetStatusSelectListLocator));
		p.setPurchaseOrder(selenium.getValue(identifyAddPurchaseOrderTextFieldLocator));
		p.setIdentified(selenium.getValue(identifyAddIdentifiedTextFieldLocator));
		p.setAssetType(selenium.getSelectedLabel(identifyAddAssetTypeTextFieldLocator));
		p.setComments(selenium.getValue(identifyAddCommentsTextFieldLocator));
		return p;
	}

	public void saveNewAsset() {
		if(selenium.isElementPresent(identifyAddSaveButtonLocator)) {
			selenium.click(identifyAddSaveButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Save button on Add Assets");
		}
	}

	public void gotoSaveAddAndInspectAssetForm() {
		
	}

	public String getSelectedTab() {
		String result = selenium.getText(identifySelectedTabLocator);
		return result;
	}

	public void assertIdentifyAddWithOrderPage() {
		assertTrue(selenium.isElementPresent(addWithOrderLabelLocator));
		assertTrue(selenium.isElementPresent(addWithOrderTextFieldLocator));
		assertTrue(selenium.isElementPresent(addWithOrderLoadButtonLocator));
	}

	public void setOrderNumber(String orderNumber) {
		assertIdentifyAddWithOrderPage();
		selenium.type(addWithOrderTextFieldLocator, orderNumber);
	}

	public void clickLoadOrderNumberButton() {
		selenium.click(addWithOrderLoadButtonLocator);
		misc.waitForPageToLoadAndCheckForOopsPage();
	}

	public Order getOrderDetails(String orderNumber) {
		Order o = new Order(orderNumber);
		boolean b = selenium.isElementPresent(orderDetailsOrderNumberLocator + "[contains(text(),'" + orderNumber +"')]");
		assertTrue("The order number given and the order number in the details don't match", b);
		String s;
		s = selenium.getText(orderDetailsOrderDateLocator);
		o.setOrderDate(s);
		s = selenium.getText(orderDetailsDescriptionLocator);
		o.setDescription(s);
		s = selenium.getText(orderDetailsPurchaseOrderLocator);
		o.setPurchaseOrder(s);
		s = selenium.getText(orderDetailsCustomerLocator);
		o.setCustomer(s);
		s = selenium.getText(orderDetailsDivisionLocator);
		o.setDivision(s);
		List<LineItem> lineItems = getLineItems();
		o.setLineItems(lineItems );
		return o;
	}

	public List<LineItem> getLineItems() {
		List<LineItem> lineItems = new ArrayList<LineItem>();
		int numRows = getNumberOfLineItemsInOrder();
		int currentRow = 1;
		String assetCodeColumn = "1";
		String assetCodeLocator = "xpath=" + orderDetailsLineItemsTableXpath + "." + currentRow + "." + assetCodeColumn;
		String descriptionColumn = "2";
		String descriptionLocator = "xpath=" + orderDetailsLineItemsTableXpath + "." + currentRow + "." + descriptionColumn;
		String quantityColumn = "3";
		String quantityLocator = "xpath=" + orderDetailsLineItemsTableXpath + "." + currentRow + "." + quantityColumn;
		String identifiedColumn = "4";
		String identifiedLocator = "xpath=" + orderDetailsLineItemsTableXpath + "." + currentRow + "." + identifiedColumn;
		for(int i = 0; i < numRows; i++, currentRow++) {
			String assetCode = selenium.getTable(assetCodeLocator);
			String description = selenium.getTable(descriptionLocator);
			String quantity = selenium.getTable(quantityLocator);
			String identified = selenium.getTable(identifiedLocator);
			LineItem li = new LineItem(assetCode, description, quantity, identified);
			lineItems.add(li);
			assetCodeLocator = assetCodeLocator.replaceFirst("\\." + currentRow, "." + (currentRow+1));
			descriptionLocator = descriptionLocator.replaceFirst("\\." + currentRow, "." + (currentRow+1));
			quantityLocator = quantityLocator.replaceFirst("\\." + currentRow, "." + (currentRow+1));
			identifiedLocator = identifiedLocator.replaceFirst("\\." + currentRow, "." + (currentRow+1));
		}
		return lineItems;
	}
	
	public int getNumberOfLineItemsInOrder() {
		Number n = selenium.getXpathCount(orderDetailsLineItemsXpathCount);
		int numRows = n.intValue();
		return numRows;
	}

	public void clickIdentifyForOrderLineItem(int index) {
		String locator = "xpath=" + orderDetailsLineItemsTableXpath + "/tbody/tr[" + (index+1) + "]/td/a[contains(text(),'Identify')]";
		if(selenium.isElementPresent(locator)) {
			selenium.click(locator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find a line item in row '" + index + "'");
		}
	}

	public void assertIdentifyWithOrderNumberPage(String orderNumber) {
		String locator = identifyPageHeaderLocator + "[contains(text(),'Order Number " + orderNumber + "')]";
		assertTrue(selenium.isElementPresent(locator));
	}

	public void gotoMultiAdd() {
		if(selenium.isElementPresent(multiAddLinkLocator)) {
			selenium.click(multiAddLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	public List<String> getAssetStatusesFromMultiAddForm() {
		List<String> results = new ArrayList<String>();
		if(selenium.isElementPresent(multiAddAssetStatusSelectLocator)) {
			String s[] = selenium.getSelectOptions(multiAddAssetStatusSelectLocator);
			for(String status : s) {
				if(!status.equals("")) {
					results.add(status);
				}
			}
		} else {
			fail("Could not find the Asset Status select list on step 1 of Multi Add");
		}
		return results;
	}

	public void setMultiAddStep1Form(Asset p) {
		assertMultiAddStep1Form();
		if(p.getOwner() != null) {
			misc.gotoChooseOwner();
			misc.setOwner(p.getOwner());
			misc.gotoSelectOwner();
		}
		if(p.getLocation() != null) {
			selenium.type(multiAddLocationTextFieldLocator, p.getLocation());
		}
		if(p.getAssetStatus() != null) {
			selenium.select(multiAddAssetStatusSelectLocator, p.getAssetStatus());
		}
		if(p.getPurchaseOrder() != null) {
			selenium.type(multiAddPurchaseOrderTextFieldLocator, p.getPurchaseOrder());
		}
		if(p.getIdentified() != null) {
			selenium.type(multiAddIdentifiedTextFieldLocator, p.getIdentified());
		}
		if(p.getAssetType() != null) {
			selenium.select(multiAddAssetTypeSelectListLocator, p.getAssetType());
			waitForAssetTypeAttributesToChange(MiscDriver.DEFAULT_TIMEOUT);
		}
		setRequiredAssetAttributes();
		if(p.getComments() != null) {
			selenium.type(multiAddCommentTextFieldLocator, p.getComments());
		}
		
	}

	private void assertMultiAddStep1Form() {
		assertTrue(selenium.isElementPresent(multiAddLocationTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddAssetStatusSelectLocator));
		assertTrue(selenium.isElementPresent(multiAddPurchaseOrderTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddIdentifiedTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddAssetTypeSelectListLocator));
		assertTrue(selenium.isElementPresent(multiAddCommentTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddAttachAFileButtonLocator));
		assertTrue(selenium.isElementPresent(multiAddStep1ContinueButtonLocator));
		assertTrue(selenium.isElementPresent(multiAddCancelButtonLocator));
	}

	public void clickContinueButtonMultiAddStep1() {
		if(selenium.isElementPresent(multiAddStep1ContinueButtonLocator)) {
			selenium.click(multiAddStep1ContinueButtonLocator);
			misc.checkForErrorMessages("MultiAddStep1");
		} else {
			fail("Could not find the Continue button on step 1 of Multi Add");
		}
	}

	public void setMultiAddStep2Form(int quantity) {
		assertMultiAddStep2Form();
		selenium.type(multiAddQuantityTextFieldLocator, Integer.toString(quantity));
	}

	private void assertMultiAddStep2Form() {
		assertTrue(selenium.isElementPresent(multiAddQuantityTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddStep2ContinueButtonLocator));
		assertTrue(selenium.isElementPresent(multiAddBackToStep1LinkLocator));
	}

	public void clickContinueButtonMultiAddStep2() {
		if(selenium.isElementPresent(multiAddStep2ContinueButtonLocator)) {
			selenium.click(multiAddStep2ContinueButtonLocator);
			selenium.waitForAjax();
			misc.checkForErrorMessages("MultiAddStep2");
			
		} else {
			fail("Could not find the Continue button on step 2 of Multi Add");
		}
	}

	public void setMultiAddStep3FormRange(String prefix, String start, String suffix) {
		assertMultiAddStep3Form();
		selenium.check(multiAddRangeRadioButtonLocator);
		if(prefix != null) {
			selenium.type(multiAddPrefixTextFieldLocator, prefix);
		}
		if(start != null) {
			selenium.type(multiAddStartTextFieldLocator, start);
		}
		if(suffix != null) {
			selenium.type(multiAddSuffixTextFieldLocator, suffix);
		}
	}

	private void assertMultiAddStep3Form() {
		assertTrue(selenium.isElementPresent(multiAddPrefixTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddStartTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddSuffixTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddIdentifierTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddRangeRadioButtonLocator));
		assertTrue(selenium.isElementPresent(multiAddNonSerializedRadioButtonLocator));
		assertTrue(selenium.isElementPresent(multiAddBatchNumberRadioButtonLocator));
		assertTrue(selenium.isElementPresent(multiAddManualRadioButtonLocator));
		assertTrue(selenium.isElementPresent(multiAddStep3ContinueButtonLocator));
		assertTrue(selenium.isElementPresent(multiAddBackToStep2LinkLocator));
	}

	public void setMultiAddStep3FormNonSerializedAssets() {
		assertMultiAddStep3Form();
		selenium.check(multiAddNonSerializedRadioButtonLocator);
	}

	public void setMultiAddStep3FormBatchNumber(String identifier) {
		assertMultiAddStep3Form();
		selenium.check(multiAddBatchNumberRadioButtonLocator);
		if(identifier != null) {
			selenium.type(multiAddIdentifierTextFieldLocator, identifier);
		}
	}

	public void setMultiAddStep3FormManual() {
		assertMultiAddStep3Form();
		selenium.check(multiAddManualRadioButtonLocator);
	}

	public void clickContinueButtonMultiAddStep3() {
		if(selenium.isElementPresent(multiAddStep3ContinueButtonLocator)) {
			selenium.click(multiAddStep3ContinueButtonLocator);
			misc.checkForErrorMessages("MultiAddStep3");
			waitForIdentifiersToBeGenerated(30);
		} else {
			fail("Could not find the Continue button on step 3 of Multi Add");
		}
	}

	private void waitForIdentifiersToBeGenerated(int timeoutSeconds) {
		boolean generating;
		int elapsedSeconds = 0;
		do {
			misc.sleep(1000);
			generating = selenium.isElementPresent("xpath=//DIV[@id='step4Loading' and not(contains(@style,'display: none'))]");
			elapsedSeconds++;
		} while(generating && elapsedSeconds < timeoutSeconds);
		
		if(generating) {
			fail("Waited " + timeoutSeconds + " for step 4 to load and it timed out");
		}
	}

	public List<Identifier> setMultiAddStep4Form(List<Identifier> identifiers) {
		assertMultiAddStep4Form();
		List<Identifier> results = new ArrayList<Identifier>();
		boolean set = false;
		if(identifiers != null)	set = true;
		
		Number n = selenium.getXpathCount(multiAddStep4NumberOfRowsXpath);
		int numRows = n.intValue();
		int currentRow = 0;
		String serialNumberLocator = "xpath=//input[@id='serial_" + currentRow + "']";
		String rfidNumberLocator = "xpath=//input[@id='step4form_identifiers_" + currentRow + "__rfidNumber']";
		String referenceNumberLocator = "xpath=//input[@id='step4form_identifiers_" + currentRow + "__referenceNumber']";

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

	private void assertMultiAddStep4Form() {
		// there is always at least two rows
		assertTrue(selenium.isElementPresent(multiAddSerialNumber1TextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddSerialNumber2TextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddRFIDNumber1TextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddRFIDNumber2TextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddReferenceNumber1TextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddReferenceNumber2TextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddSaveAndCreateButtonLocator));
		assertTrue("back to step 3 link is available", selenium.isElementPresent(multiAddBackToStep3LinkLocator));
	}

	public void clickSaveAndCreateButtonMultiAddStep4() {
		assertMultiAddStep4Form();
		selenium.click(multiAddSaveAndCreateButtonLocator);
		misc.checkForErrorMessages("MultiAddStep4");
		misc.waitForPageToLoadAndCheckForOopsPage();
	}

	public void gotoAddSingleAsset() {
		selenium.open("/fieldid/productAdd.action");
	}

	public void fillInTextAttribute(String fieldName, String value) {
		selenium.type("css=.infoSet[infoFieldName='" + fieldName.replace("'", "/'") + "'] .attribute", value);
		
	}

	public void fillInSelectAttribute(String fieldName, String value) {
		selenium.select("css=.infoSet[infoFieldName='" + fieldName.replace("'", "/'") + "'] .attribute", value);
		
	}
	
	public void fillInComboAttribute(String fieldName, String value) {
		selenium.select("css=.infoSet[infoFieldName='" + fieldName.replace("'", "/'") + "'] .attribute", value);
		
	}
}
