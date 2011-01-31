package com.n4systems.fieldid.selenium.components;

import com.n4systems.fieldid.selenium.pages.WebEntity;
import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;
import com.thoughtworks.selenium.Selenium;

public class UnitOfMeasurePicker extends WebEntity {
	
	final static String ID_PREFIX = "unitOfMeasureId_";
    private String id;

    public UnitOfMeasurePicker(Selenium selenium, String pickerId) {
		super(selenium);
		id = pickerId;
	}
	
	private void waitForUnitofMeasurePickerLoadingToFinish() {
		new ConditionWaiter(new Predicate() {
						
			@Override
			public boolean evaluate() {
				String xpath = "//select[contains(@id,'unitOfMeasureId_"+ id +"')]";
				return selenium.isElementPresent(xpath);
			}
		}).run("Unit of Measure picker: " + id + " loading image never went away");
	}

    public void setUnitOfMeasure(String value) {
        setUnitOfMeasure(this.id, value);
	}
	
	public void setUnitOfMeasure(String id, String value) {
		String locator = "//input[contains(@class,'unitOfMeasure') and @id='" + id + "']";
        String anchor = locator + "/../../SPAN[@class='action']/A[contains(@id,'" + id + "')]";
        selenium.click(anchor);
        waitForUnitofMeasurePickerLoadingToFinish();

        String typeOfUnitOfMeasure = getSelectedValueIfPresent("//select[contains(@id,'" + ID_PREFIX + id + "')]");

        String unitOfMeasureInputLocator = "//input[@id='" + typeOfUnitOfMeasure + "_" + id + "']";

        if(getValueIfPresent(unitOfMeasureInputLocator).equals("")) {
            selenium.type(unitOfMeasureInputLocator, value);
        }
        String submitButton = "css=#unitOfMeasureForm_" + id + "_hbutton_submit";
        selenium.click(submitButton);
        waitForAjax();
	}

}
