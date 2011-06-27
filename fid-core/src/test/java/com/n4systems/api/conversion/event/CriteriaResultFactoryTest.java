package com.n4systems.api.conversion.event;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaType;
import com.n4systems.model.DateFieldCriteriaResult;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.State;
import com.n4systems.model.Status;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteriaResult;


public class CriteriaResultFactoryTest {

	private final int IMAGE_LENGTH = 123;
	private long TEST_TIMESTAMP = 123456789L;	

	
	@Test
	public void test_create_criteria() { 
		CriteriaResultFactory fixture = createFixture();
		
		ComboBoxCriteriaResult combo = (ComboBoxCriteriaResult) fixture.createCriteriaResult(CriteriaType.COMBO_BOX);		
		assertEquals(combo.getClass().getSimpleName(), combo.getValue());
		
		SignatureCriteriaResult signature = (SignatureCriteriaResult) fixture.createCriteriaResult(CriteriaType.SIGNATURE);
		assertEquals(IMAGE_LENGTH, signature.getImage().length);
		
		TextFieldCriteriaResult textField = (TextFieldCriteriaResult) fixture.createCriteriaResult(CriteriaType.TEXT_FIELD);
		assertEquals(textField.getClass().getSimpleName(), textField.getValue());
		
		SelectCriteriaResult select = (SelectCriteriaResult) fixture.createCriteriaResult(CriteriaType.SELECT);
		assertEquals(select.getClass().getSimpleName(), select.getValue());
		
		DateFieldCriteriaResult date = (DateFieldCriteriaResult) fixture.createCriteriaResult(CriteriaType.DATE_FIELD);
		assertEquals(TEST_TIMESTAMP, date.getValue().getTime());
		
		OneClickCriteriaResult oneClick = (OneClickCriteriaResult) fixture.createCriteriaResult(CriteriaType.ONE_CLICK);
		assertEquals(Status.PASS, oneClick.getState().getStatus());
		
		UnitOfMeasureCriteriaResult um = (UnitOfMeasureCriteriaResult) fixture.createCriteriaResult(CriteriaType.UNIT_OF_MEASURE);
		assertEquals("primary", um.getPrimaryValue());
		assertEquals("secondary", um.getSecondaryValue());
	}
	
	private CriteriaResultFactory createFixture() {
		return new CriteriaResultFactory(new CriteriaResultPopulatorAdaptor() {
			
			@Override
			public CriteriaResult populate(SignatureCriteriaResult result) {
				result.setImage(new byte[123]);
				return result;
			}
			
			@Override
			public CriteriaResult populate(TextFieldCriteriaResult result) {
				result.setValue(result.getClass().getSimpleName());
				return result;
			}
			
			@Override
			public CriteriaResult populate(UnitOfMeasureCriteriaResult result) {
				result.setPrimaryValue("primary");
				result.setSecondaryValue("secondary");
				return result;
			}
			
			@Override
			public CriteriaResult populate(SelectCriteriaResult result) {
				result.setValue(result.getClass().getSimpleName());
				return result;
			}
			
			@Override
			public CriteriaResult populate(DateFieldCriteriaResult result) {
				result.setValue(new Date(TEST_TIMESTAMP));
				return result;
			}
			
			@Override
			public CriteriaResult populate(ComboBoxCriteriaResult result) {
				result.setValue(result.getClass().getSimpleName());
				return result;
			}
			
			@Override
			public CriteriaResult populate(OneClickCriteriaResult result) {
				result.setState(new State("Pass", Status.PASS, "pass"));
				return result;
			}
			
		});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_null_populator() { 
		CriteriaResultFactory criteriaResultFactory = new CriteriaResultFactory(null);
	}
}
