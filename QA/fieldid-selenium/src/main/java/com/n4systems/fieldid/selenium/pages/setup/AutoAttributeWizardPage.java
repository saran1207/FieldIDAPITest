package com.n4systems.fieldid.selenium.pages.setup;

import java.util.HashMap;
import java.util.Map;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;
import com.thoughtworks.selenium.Selenium;

public class AutoAttributeWizardPage extends FieldIDPage {
	
	private static final String INPUT_FIELDS_DIV_XPATH = "//div[@id='inputlist']";
	private static final String OUTPUT_FIELDS_DIV_XPATH = "//div[@id='outputlist']";
	private static final String AVAILABLE_FIELDS_DIV_XPATH = "//div[@id='availablelist']";

	public AutoAttributeWizardPage(Selenium selenium) {
		super(selenium);
	}

	public void clickProductType(String productType) {
		selenium.click("//div[@id='pageContent']//a[.=' "+productType+" ']");
		waitForPageToLoad();
	}
	
	public void dragAvailableFieldToInputFields(String fieldName) {
		dragFieldFromTo(fieldName, AVAILABLE_FIELDS_DIV_XPATH, INPUT_FIELDS_DIV_XPATH);
	}
	
	public void dragAvailableFieldToOutputFields(String fieldName) {
		dragFieldFromTo(fieldName, AVAILABLE_FIELDS_DIV_XPATH, OUTPUT_FIELDS_DIV_XPATH);
	}

	private void dragFieldFromTo(String fieldName, String fromListXpath, final String toListXpath) {
		final String pathOfElementUnderList = "//div[starts-with(@id, 'field_')]/span[position() = 1 and .='"+fieldName+"']/.."; 
		String downXpath = fromListXpath + pathOfElementUnderList;
		selenium.mouseDownAt(downXpath, "1,1");
		selenium.mouseMoveAt(toListXpath, "1,1");
		selenium.mouseOver(toListXpath);
		selenium.mouseUpAt(toListXpath, "1,1");
		new ConditionWaiter(new Predicate() {
			@Override
			public boolean evaluate() {
				String xpath = toListXpath + pathOfElementUnderList;
				if (!selenium.isElementPresent(xpath)) return false;
				String attr = selenium.getAttribute(xpath+"/@style");
				return attr.indexOf("opacity") < 0;
			}
		}).run("Field never arrived under target list when being dragged in auto attributes");
	}
	
	public Map<String,String> getAvailableFields() {
		return getFieldsUnder(AVAILABLE_FIELDS_DIV_XPATH);
	}
	
	public Map<String, String> getInputFields() {
		return getFieldsUnder(INPUT_FIELDS_DIV_XPATH);
	}
	
	public Map<String, String> getOutputFields() {
		return getFieldsUnder(OUTPUT_FIELDS_DIV_XPATH);
	}

	private Map<String, String> getFieldsUnder(String listXpath) {
		Map<String,String> fields = new HashMap<String,String>();
		String elementXpath = listXpath + "/div[starts-with(@id,'field_')]";
		int numFields = selenium.getXpathCount(elementXpath).intValue();
		for (int i = 1; i <= numFields; i++) {
			String fieldNameStr = selenium.getText("xpath=("+elementXpath+")["+i+"]/span[1]");
			String fieldTypeStr = selenium.getText("xpath=("+elementXpath+")["+i+"]/span[2]").replaceAll("\\W","");
			
			fields.put(fieldNameStr, fieldTypeStr);
		}
		return fields;
	}
	
	public void clickSave() {
		selenium.click("//input[@type = 'submit' and @value='Save']");
		waitForPageToLoad();
	}
	
	public void clickAddDefinition() {
		selenium.click("//a[.='Add Definition']");
		waitForPageToLoad();
	}
	
	public boolean isOnDefinitionsTab() {
		return "Definitions".equals(getCurrentTab());
	}
	
	public void clickEditTab() {
		clickNavOption("Edit");
	}

	public void clickDeleteButtonAndConfirm() {
		selenium.chooseOkOnNextConfirmation();
		selenium.click("//input[@type = 'button' and @value='Delete']");
		selenium.getConfirmation();
		waitForPageToLoad();
	}

	public void selectChoiceInputField(String fieldName, String value) {
		String path = "//div[@id='inputlist']//div[@infofieldname='"+fieldName+"']//select";
		selenium.select(path, value);
	}

	public void enterOutputTextField(String fieldName, String value) {
		String path = "//div[@id='outputlist']//div[@infofieldname='"+fieldName+"']//input[@type='text']";
		selenium.type(path, value);
	}

	public void selectChoiceOutputField(String fieldName, String value) {
		String path = "//div[@id='outputlist']//div[@infofieldname='"+fieldName+"']//select";
		selenium.select(path, value);
	}

}
