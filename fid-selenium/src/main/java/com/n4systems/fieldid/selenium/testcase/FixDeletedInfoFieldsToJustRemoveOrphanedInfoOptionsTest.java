package com.n4systems.fieldid.selenium.testcase;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.components.UnitOfMeasurePicker;
import com.n4systems.fieldid.selenium.pages.SetupPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetCodeMappingsPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypesPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.ExtendedFeature;
import org.junit.Test;
import rfid.ejb.entity.InfoFieldBean;

public class FixDeletedInfoFieldsToJustRemoveOrphanedInfoOptionsTest extends PageNavigatingTestCase<SetupPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.primaryOrgFor("test1").setExtendedFeatures(setOf(ExtendedFeature.Integration));

        InfoFieldBean textField = scenario.anInfoField()
                .type(InfoFieldBean.TEXTFIELD_FIELD_TYPE)
                .withName("text field")
                .build();

        InfoFieldBean selectField = scenario.anInfoField()
                .type(InfoFieldBean.SELECTBOX_FIELD_TYPE)
                .withOptions(scenario.anInfoOption().withName("one").build())
                .withName("select field")
                .build();

        InfoFieldBean comboField = scenario.anInfoField()
                .type(InfoFieldBean.COMBOBOX_FIELD_TYPE)
                .withOptions(scenario.anInfoOption().withName("two").build())
                .withName("combo field")
                .build();

        InfoFieldBean unitOfMeasureField = scenario.anInfoField()
                .type(InfoFieldBean.TEXTFIELD_FIELD_TYPE)
                .usingUnitOfMeasure(true)
                .unitOfMeasure(scenario.unitOfMeasure("Inches"))
                .withName("uom field")
                .build();

        scenario.anAssetType()
                .named("test type")
                .withFields(textField, selectField, comboField, unitOfMeasureField)
                .build();
    }

    @Override
    protected SetupPage navigateToPage() {
        return startAsCompany("test1").login().clickSetupLink();
    }

    @Test
	public void attributesCanBeDeletedFromUnusedAssetType() throws Exception {
        ManageAssetTypesPage assetTypePage = page.clickAssetTypes().clickAssetType("test type");
        assetTypePage.clickEditTab();
        assetTypePage.deleteAttributes("text field", "select field", "combo field", "uom field");
	}

	@Test
	public void attributesCanOnlyBeRetiredIfUsedInAssetCodeMapping() throws Exception {
        ManageAssetCodeMappingsPage mappingsPage = page.clickManageAssetCodeMappings();
        mappingsPage.clickAddTab();
        mappingsPage.enterAssetCode("ABC");
        mappingsPage.selectAssetType("test type");

        UnitOfMeasurePicker uomPicker = mappingsPage.getUnitOfMeasurePickerForAttribute("uom field");
        uomPicker.setUnitOfMeasure("23");

        mappingsPage.enterAttributeValue("text field", "my value");
        mappingsPage.selectAttributeValue("select field", "one");
        mappingsPage.selectAttributeValue("combo field", "two");

        mappingsPage.clickSaveAssetCodeMapping();

        ManageAssetTypesPage assetTypePage = mappingsPage.clickSetupLink().clickAssetTypes().clickAssetType("test type").clickEditTab();
        assetTypePage.retireAttributes("text field", "select field", "combo field", "uom field");
	}

}
