package com.n4systems.fieldid.minimaldata.idenitfy;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.Test;
import rfid.ejb.entity.InfoFieldBean;

import static org.junit.Assert.assertEquals;

public class AssetAttributeFillInTest extends PageNavigatingTestCase<IdentifyPage> {

    private static final String TEST_SERIAL_NUMBER = "TEST-424299";

    @Override
    public void setupScenario(Scenario scenario) {
        InfoFieldBean textField =  scenario.anInfoField()
                .type(InfoFieldBean.TEXTFIELD_FIELD_TYPE)
                .withName("Text field")
                .build();

        InfoFieldBean selectField =  scenario.anInfoField()
                .type(InfoFieldBean.SELECTBOX_FIELD_TYPE)
                .withOptions(scenario.anInfoOption().withName("option 1").build(), scenario.anInfoOption().withName("option 2").build())
                .withName("select field")
                .build();

        InfoFieldBean comboField =  scenario.anInfoField()
                .type(InfoFieldBean.COMBOBOX_FIELD_TYPE)
                .withOptions(scenario.anInfoOption().withName("combo option 1").build(), scenario.anInfoOption().withName("combo option 2").build())
                .withName("combo field")
                .build();

        scenario.anAssetType()
                .withFields(textField, selectField, comboField)
                .named("Simple Multi Attribute Asset")
                .build();
    }

    @Override
    protected IdentifyPage navigateToPage() {
        return startAsCompany("test1").login().clickIdentifyLink();
    }
	
	@Test
	public void should_store_a_value_in_a_text_field_during_asset_identify() throws Exception {
        page.enterSerialNumber(TEST_SERIAL_NUMBER);
        page.selectAssetType("Simple Multi Attribute Asset");
		page.fillInTextAttribute("Text field", "Filling in text field value");
		page.saveNewAsset();

        AssetPage assetPage = page.search(TEST_SERIAL_NUMBER);

        assertEquals(TEST_SERIAL_NUMBER, assetPage.getSerialNumber());

        assertEquals("Simple Multi Attribute Asset", assetPage.getAssetType());
        assertEquals("Filling in text field value", assetPage.getValueForAttribute("Text field"));
	}
	
	@Test
	public void should_store_a_value_in_a_select_field_during_asset_identify() throws Exception {
        page.enterSerialNumber(TEST_SERIAL_NUMBER);
        page.selectAssetType("Simple Multi Attribute Asset");
		page.fillInSelectAttribute("select field", "option 1");
		page.saveNewAsset();
	
        AssetPage assetPage = page.search(TEST_SERIAL_NUMBER);

        assertEquals(TEST_SERIAL_NUMBER, assetPage.getSerialNumber());

        assertEquals("Simple Multi Attribute Asset", assetPage.getAssetType());
        assertEquals("option 1", assetPage.getValueForAttribute("select field"));
	}
	
	@Test
	public void should_store_a_value_in_a_combo_field_with_a_static_value_during_asset_identify() throws Exception {
        page.enterSerialNumber(TEST_SERIAL_NUMBER);
        page.selectAssetType("Simple Multi Attribute Asset");

		page.fillInComboAttribute("combo field", "combo option 1");
		page.saveNewAsset();

        AssetPage assetPage = page.search(TEST_SERIAL_NUMBER);

        assertEquals("Simple Multi Attribute Asset", assetPage.getAssetType());
        assertEquals("combo option 1", assetPage.getValueForAttribute("combo field"));
	}

}
