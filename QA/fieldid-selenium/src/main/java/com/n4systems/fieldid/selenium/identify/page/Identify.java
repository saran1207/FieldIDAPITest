package com.n4systems.fieldid.selenium.identify.page;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.datatypes.SafetyNetworkRegistration;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;

public class Identify {

	private FieldIdSelenium selenium;
	private Misc misc;
	
	// Locators
	private String identifyAssetsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Assets')]";
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
	private String identifyAddPublishOverSafetyNetworkSelectListLocator = "xpath=//SELECT[@id='productCreate_publishedState']";
	private String identifyAddCommentsTextFieldLocator = "xpath=//TEXTAREA[@id='comments']";
	private String productTypeAttributesUpdatingLocator = "xpath=//SPAN[id='productTypeIndicator' and not(contains(@style,'visibility: hidden'))]";
	private CharSequence classStringIdentifyingRequiredFields = "requiredField";
	private String classStringIdentifyingUnitOfMeasureFields = "unitOfMeasure";

	public Identify(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void verifyAssetsPageHeader() {
		assertTrue("Could not find the header for the Assets page", selenium.isElementPresent(identifyAssetsPageHeaderLocator));
		misc.checkForErrorMessages(null);
	}
	
	public void verifyAssetsAddPage() {
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
	
	public boolean isAddWithOrder() {
		return selenium.isElementPresent(addWithOrderSelectedLocator);
	}
	
	public boolean isAdd() {
		return selenium.isElementPresent(addSelectedLocator);
	}
	
	public boolean isMultiAdd() {
		return selenium.isElementPresent(addMultiSelectedLocator);
	}

	public void gotoAdd() {
		misc.info("Click the 'Add' link to add a single asset");
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
		verifyAssetsAddPage();
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
			waitForProductTypeAttributesToChange(Misc.defaultTimeout);
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
	}

	private void setRequiredTextFields(List<String> requiredTextFieldIDs) {
		Iterator<String> i = requiredTextFieldIDs.iterator();
		while(i.hasNext()) {
			String id = i.next();
			String locator = "xpath=//INPUT[contains(@class,'" + classStringIdentifyingRequiredFields
				+ "') and not(contains(@class,'" 
				+ classStringIdentifyingUnitOfMeasureFields + "')) and @id='" 
				+ id + "']";
			if(selenium.isElementPresent(locator)) {
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
	 * If does not change the unit of measure but uses the
	 * default value.
	 * 
	 * For unit of measures which have more than one input,
	 * this will fill in the first value only.
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
			selenium.type(unitOfMeasureInputLocator, value);
			String submitButton = "//INPUT[contains(@id,'" + id + "') and @value='Submit' and @type='submit']";
			selenium.click(submitButton);
		}
	}

	private void waitForUnitOfMeasureDialogToOpen(String id) {
		// TODO should wait for the loading image to disappear
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

	public void setRegisterThisAssetOverTheSafetyNetwork(SafetyNetworkRegistration registration) {
		// TODO what if there is no link to register products over safety network?
		
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
		misc.info("Click Save on Add Asset");
		if(selenium.isElementPresent(identifyAddSaveButtonLocator)) {
			selenium.click(identifyAddSaveButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the Save button on Add Assets");
		}
	}
}
