package com.n4systems.fieldid.selenium.identify.page;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import com.n4systems.fieldid.selenium.datatypes.Identifier;
import com.n4systems.fieldid.selenium.datatypes.LineItem;
import com.n4systems.fieldid.selenium.datatypes.Order;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.datatypes.SafetyNetworkRegistration;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;

public class Identify {

	private FieldIdSelenium selenium;
	private Misc misc;
	
	// Locators
	private String identifyPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Identify')]";
	private String identifyAddSerialNumberTextFieldLocator = "xpath=//INPUT[@id='serialNumberText']";
	private String identifyAddGenerateLinkLocator = "xpath=//A[contains(text(),'generate')]";
	private String identifyAddRFIDNumberTextFieldLocator = "xpath=//INPUT[@id='rfidNumber']";
	private String identifyAddReferenceNumberTextFieldLocator = "xpath=//INPUT[@id='customerRefNumber']";
	private String identifyAddLocationTextFieldLocator = "xpath=//INPUT[@id='location']";
	private String identifyAddProductStatusSelectListLocator = "xpath=//SELECT[@id='productCreate_productStatus']";
	private String identifyAddPurchaseOrderTextFieldLocator = "xpath=//INPUT[@id='productCreate_purchaseOrder']";
	private String identifyAddIdentifiedTextFieldLocator = "xpath=//INPUT[@id='identified']";
	private String identifyAddProductTypeTextFieldLocator = "xpath=//SELECT[@id='productType']";
	private String identifyAddOwnerTextFieldLocator = "xpath=//INPUT[@id='productCreate_owner_orgName']";
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
	private String identifyAddPublishOverSafetyNetworkSelectListLocator = "xpath=//SELECT[@id='productCreate_publishedState']";
	private String identifyAddCommentsTextFieldLocator = "xpath=//TEXTAREA[@id='comments']";
	private String productTypeAttributesUpdatingLocator = "xpath=//SPAN[id='productTypeIndicator' and not(contains(@style,'visibility: hidden'))]";
	private CharSequence classStringIdentifyingRequiredFields = "requiredField";
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
	private String multiAddProductStatusSelectLocator = "xpath=//select[@id='step1form_productStatus']";
	private String multiAddLocationTextFieldLocator = "xpath=//input[@id='location']";
	private String multiAddPurchaseOrderTextFieldLocator = "xpath=//input[@id='step1form_purchaseOrder']";
	private String multiAddIdentifiedTextFieldLocator = "xpath=//input[@id='identified']";
	private String multiAddProductTypeSelectListLocator = "xpath=//select[@id='productType']";
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
	private String multiAddSaveAndCreateButtonLocator = "xpath=//input[@id='masterForm_label_save_and_create']";
	private String multiAddBackToStep3LinkLocator = "xpath=//a[contains(text(),'Back To Step 3')]";

	public Identify(FieldIdSelenium selenium, Misc misc) {
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
		assertTrue("Could not find the select list to Register Product on the Safety Network", selenium.isElementPresent(identifyAddPublishOverSafetyNetworkSelectListLocator));
		assertTrue("Could not find the text field for Owner on Assets Add", selenium.isElementPresent(identifyAddOwnerTextFieldLocator));
		assertTrue("Could not find the link for Choose Owner on Assets Add", selenium.isElementPresent(identifyAddChooseLinkLocator));
		assertTrue("Could not find the text field for Location on Assets Add", selenium.isElementPresent(identifyAddLocationTextFieldLocator));
		assertTrue("Could not find the select list for Product Status on Assets Add", selenium.isElementPresent(identifyAddProductStatusSelectListLocator));
		assertTrue("Could not find the text field for Purchase Order on Assets Add", selenium.isElementPresent(identifyAddPurchaseOrderTextFieldLocator));
		assertTrue("Could not find the text field for Identified on Assets Add", selenium.isElementPresent(identifyAddIdentifiedTextFieldLocator));
		assertTrue("Could not find the text field for Product Type on Assets Add", selenium.isElementPresent(identifyAddProductTypeTextFieldLocator));
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
	 * with the fields of the Product p. Currently, the implementation
	 * of Product does not include product type attributes. So for any
	 * required product type attributes, this will fill them in with
	 * random values.
	 * 
	 *  If any other required fields are blank, this will not attempt
	 *  to fill them in, i.e. serial number and identified date.
	 *  
	 *  If the generate parameter is true, this will use the generate
	 *  link to create a new serial number and save that serial number
	 *  in the provided Product. If there is a serial number and the
	 *  generate parameter is true, a new serial number will be generated
	 *  then the input serial number will replace with the generated
	 *  serial number, i.e. click generate then type the provided
	 *  serial number over top of the generated serial number.
	 *  
	 *  Typically, if you want to create a product with no inputs from
	 *  the Product, you should be able to pass in a:
	 *  
	 *   	setAddAssetForm(new Product(), true);
	 *   
	 *  and it will return the filled in product information. 
	 *  
	 * @param p
	 * @param generate
	 * @return
	 * @throws InterruptedException 
	 */
	public Product setAddAssetForm(Product p, boolean generate) throws InterruptedException {
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
		if(p.getProductStatus() != null) {
			selenium.select(identifyAddProductStatusSelectListLocator, p.getProductStatus());
		}
		if(p.getPurchaseOrder() != null) {
			selenium.type(identifyAddPurchaseOrderTextFieldLocator, p.getPurchaseOrder());
		}
		if(p.getIdentified() != null) {
			selenium.type(identifyAddIdentifiedTextFieldLocator, p.getIdentified());
		}
		if(p.getProductType() != null) {
			selenium.select(identifyAddProductTypeTextFieldLocator, p.getProductType());
			waitForProductTypeAttributesToChange(Misc.DEFAULT_TIMEOUT);
		}
		setRequiredProductAttributes();
		
		if(p.getComments() != null) {
			selenium.type(identifyAddCommentsTextFieldLocator, p.getComments());
		}
		
		// copy everything back so any auto-filled fields get retrieved
		// for example, generated serial number or identified field.
		p = getAddAssetForm();
		
		return p;
	}

	private void waitForProductTypeAttributesToChange(String defaultTimeout) {
		int maxSeconds = Integer.parseInt(defaultTimeout);
		boolean updating = true;
		int secondsLeft = maxSeconds;
		do {
			updating = selenium.isElementPresent(productTypeAttributesUpdatingLocator);
			misc.sleep(1000);
			secondsLeft--;
		} while(updating && secondsLeft > 0);
		misc.sleep(3000);
	}

	public void setRequiredProductAttributes() {
		String source = selenium.getHtmlSource();
		List<String> requiredSelectListsIDs = getRequiredSelectListIDs(source);
		setRequiredSelectLists(requiredSelectListsIDs);
		List<String> requiredTextFieldIDs = getRequiredTextFieldIDs(source);
		setRequiredUnitsOfMeasure(requiredTextFieldIDs);
		setRequiredTextFields(requiredTextFieldIDs);
		misc.sleep(2000);
	}

	private void setRequiredTextFields(List<String> requiredTextFieldIDs) {
		Iterator<String> i = requiredTextFieldIDs.iterator();
		while(i.hasNext()) {
			String id = i.next();
			String locator = "xpath=//INPUT[contains(@class,'" + classStringIdentifyingRequiredFields
				+ "') and not(contains(@class,'" 
				+ classStringIdentifyingUnitOfMeasureFields + "')) and @id='" 
				+ id + "']";
			if(selenium.isElementPresent(locator) && selenium.getValue(locator).equals("")) {
				String value = misc.getRandomString(8);
				selenium.type(locator, value);
			}
		}
	}

	private void setRequiredUnitsOfMeasure(List<String> requiredTextFieldIDs) {
		Iterator<String> i = requiredTextFieldIDs.iterator();
		while(i.hasNext()) {
			String id = i.next();
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
			String submitButton = "//INPUT[contains(@id,'" + id + "') and @value='Submit' and @type='submit']";
			selenium.click(submitButton);
		}
	}

	private void waitForUnitOfMeasureDialogToOpen(String id) {
		misc.sleep(3000);
	}

	private void setRequiredSelectLists(List<String> requiredSelectListsIDs) {
		Iterator<String> i = requiredSelectListsIDs.iterator();
		while(i.hasNext()) {
			String id = i.next();
			String locator = "xpath=//SELECT[contains(@class,'" + classStringIdentifyingRequiredFields + "') and "
				+ "@id='" + id + "']";
			if(selenium.isElementPresent(locator)) {
				selenium.select(locator, "index=1");
			}
		}
	}

	private List<String> getRequiredTextFieldIDs(String source) {
		return getRequiredFieldsByTagType(source, "input");
	}

	public List<String> getRequiredSelectListIDs(String source) {
		return getRequiredFieldsByTagType(source, "select");
	}
	
	List<String> getRequiredFieldsByTagType(String source, String tagType) {
		List<String> result = new ArrayList<String>();
		
		StringReader sr = new StringReader(source);
		BufferedReader br = new BufferedReader(sr);
		Tidy tidy = new Tidy();
		boolean required = false;
		String id = "";

		// hide all the output from JTidy
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		tidy.setShowErrors(0);

		Document htmlDoc = tidy.parseDOM(br, new OutputStreamWriter(System.out));
		NodeList tags = htmlDoc.getElementsByTagName(tagType);
		for(int i = 0; i < tags.getLength(); i++) {
			Node tag = tags.item(i);
			NamedNodeMap attributes = tag.getAttributes();
			for(int j = 0; j < attributes.getLength(); j++) {
				Node attribute = attributes.item(j);
				String attributeText = attribute.getNodeName();
				if(attributeText.equals("class")) {
					String attributeValue = attribute.getNodeValue();
					if(attributeValue.contains(classStringIdentifyingRequiredFields)) {
						required = true;
					}
				} else if(attributeText.equals("id")) {
					id = attribute.getNodeValue();
				}
			}
			if(required) {
				result.add(id);
				required = false;
			}
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
			selenium.waitForAjax(Misc.DEFAULT_TIMEOUT);
			assertFalse("Could not find vendor '" + vendor + "', asset '" + asset + "'", selenium.isElementPresent(couldNotFindAnyPublishedAssetTextLocator));
		} else {
			fail("Could not find a link to register the asset over the safety network");
		}
	}

	public Product getAddAssetForm() {
		Product p = new Product();
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
		p.setLocation(selenium.getValue(identifyAddLocationTextFieldLocator));
		p.setProductStatus(selenium.getSelectedLabel(identifyAddProductStatusSelectListLocator));
		p.setPurchaseOrder(selenium.getValue(identifyAddPurchaseOrderTextFieldLocator));
		p.setIdentified(selenium.getValue(identifyAddIdentifiedTextFieldLocator));
		p.setProductType(selenium.getSelectedLabel(identifyAddProductTypeTextFieldLocator));
		p.setComments(selenium.getValue(identifyAddCommentsTextFieldLocator));
		return p;
	}

	public void gotoSaveAddAssetForm() {
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
		String productCodeColumn = "1";
		String productCodeLocator = "xpath=" + orderDetailsLineItemsTableXpath + "." + currentRow + "." + productCodeColumn;
		String descriptionColumn = "2";
		String descriptionLocator = "xpath=" + orderDetailsLineItemsTableXpath + "." + currentRow + "." + descriptionColumn;
		String quantityColumn = "3";
		String quantityLocator = "xpath=" + orderDetailsLineItemsTableXpath + "." + currentRow + "." + quantityColumn;
		String identifiedColumn = "4";
		String identifiedLocator = "xpath=" + orderDetailsLineItemsTableXpath + "." + currentRow + "." + identifiedColumn;
		for(int i = 0; i < numRows; i++, currentRow++) {
			String productCode = selenium.getTable(productCodeLocator);
			String description = selenium.getTable(descriptionLocator);
			String quantity = selenium.getTable(quantityLocator);
			String identified = selenium.getTable(identifiedLocator);
			LineItem li = new LineItem(productCode, description, quantity, identified);
			lineItems.add(li);
			productCodeLocator = productCodeLocator.replaceFirst("\\." + currentRow, "." + (currentRow+1));
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

	public List<String> getProductStatusesFromMultiAddForm() {
		List<String> results = new ArrayList<String>();
		if(selenium.isElementPresent(multiAddProductStatusSelectLocator)) {
			String s[] = selenium.getSelectOptions(multiAddProductStatusSelectLocator);
			for(String status : s) {
				if(!status.equals("")) {
					results.add(status);
				}
			}
		} else {
			fail("Could not find the Product Status select list on step 1 of Multi Add");
		}
		return results;
	}

	public void setMultiAddStep1Form(Product p) {
		assertMultiAddStep1Form();
		if(p.getOwner() != null) {
			misc.gotoChooseOwner();
			misc.setOwner(p.getOwner());
			misc.gotoSelectOwner();
		}
		if(p.getLocation() != null) {
			selenium.type(multiAddLocationTextFieldLocator, p.getLocation());
		}
		if(p.getProductStatus() != null) {
			selenium.select(multiAddProductStatusSelectLocator, p.getProductStatus());
		}
		if(p.getPurchaseOrder() != null) {
			selenium.type(multiAddPurchaseOrderTextFieldLocator, p.getPurchaseOrder());
		}
		if(p.getIdentified() != null) {
			selenium.type(multiAddIdentifiedTextFieldLocator, p.getIdentified());
		}
		if(p.getProductType() != null) {
			selenium.select(multiAddProductTypeSelectListLocator, p.getProductType());
			waitForProductTypeAttributesToChange(Misc.DEFAULT_TIMEOUT);
		}
		setRequiredProductAttributes();
		if(p.getComments() != null) {
			selenium.type(multiAddCommentTextFieldLocator, p.getComments());
		}
		if(p.getAttachments() != null && p.getAttachments().size() > 0) {
			// TODO
		}
	}

	private void assertMultiAddStep1Form() {
		assertTrue(selenium.isElementPresent(multiAddLocationTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddProductStatusSelectLocator));
		assertTrue(selenium.isElementPresent(multiAddPurchaseOrderTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddIdentifiedTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddProductTypeSelectListLocator));
		assertTrue(selenium.isElementPresent(multiAddCommentTextFieldLocator));
		assertTrue(selenium.isElementPresent(multiAddAttachAFileButtonLocator));
		assertTrue(selenium.isElementPresent(multiAddStep1ContinueButtonLocator));
		assertTrue(selenium.isElementPresent(multiAddCancelButtonLocator));
	}

	public void clickContinueButtonMultiAddStep1() {
		if(selenium.isElementPresent(multiAddStep1ContinueButtonLocator)) {
			selenium.click(multiAddStep1ContinueButtonLocator);
			misc.checkForErrorMessages("MultiAddStep1");
			// TODO: do I need a wait for AJAX here?
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
			misc.checkForErrorMessages("MultiAddStep2");
			// TODO: do I need a wait for AJAX here?
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
		assertTrue(selenium.isElementPresent(multiAddBackToStep3LinkLocator));
	}

	public void clickSaveAndCreateButtonMultiAddStep4() {
		assertMultiAddStep4Form();
		selenium.click(multiAddSaveAndCreateButtonLocator);
		misc.checkForErrorMessages("MultiAddStep4");
		misc.waitForPageToLoadAndCheckForOopsPage();
	}
}
