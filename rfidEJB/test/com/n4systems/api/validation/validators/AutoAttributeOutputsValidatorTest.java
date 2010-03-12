package com.n4systems.api.validation.validators;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.builders.InfoFieldBeanBuilder;
import com.n4systems.model.builders.InfoOptionBeanBuilder;

public class AutoAttributeOutputsValidatorTest {
	private AutoAttributeOutputsValidator validator = new AutoAttributeOutputsValidator();
	
	@Test
	public void validation_passes_when_all_static_only_fields_are_resolvable() {
		Map<String, String> outputs = new HashMap<String, String>();
		outputs.put("combo", "c-1");
		outputs.put("select", "s-2");
		outputs.put("text", "mklasd");
		
		assertTrue(validator.validate(outputs, null, "", null, createCriteria()).isPassed());
	}
	
	@Test
	public void validation_allows_missing_fields() {
		Map<String, String> outputs = new HashMap<String, String>();
		outputs.put("combo", "c-1");
		outputs.put("text", "mklasd");
		
		assertTrue(validator.validate(outputs, null, "", null, createCriteria()).isPassed());
	}
	
	@Test
	public void validation_allows_no_fields() {
		assertTrue(validator.validate(new HashMap<String, String>(), null, "", null, createCriteria()).isPassed());
	}
	
	@Test
	public void validation_allows_dynamic_combo_boxes() {
		Map<String, String> outputs = new HashMap<String, String>();
		outputs.put("combo", "nasdnuwuhqw");
		
		assertTrue(validator.validate(outputs, null, "", null, createCriteria()).isPassed());
	}
	
	@Test
	public void validation_fails_when_select_option_cannot_be_resolved() {
		Map<String, String> outputs = new HashMap<String, String>();
		outputs.put("select", "nasdnuwuhqw");
		
		assertTrue(validator.validate(outputs, null, "", null, createCriteria()).isFailed());
	}
	
	private AutoAttributeCriteria createCriteria() {
		AutoAttributeCriteria criteria = new AutoAttributeCriteria();
		criteria.getOutputs().addAll(Arrays.asList(
			InfoFieldBeanBuilder.aComboBox().withName("combo").withOptions(
				InfoOptionBeanBuilder.aStaticInfoOption().withName("c-1").build(),
				InfoOptionBeanBuilder.aStaticInfoOption().withName("c-2").build()
			).build(),
			
			InfoFieldBeanBuilder.aSelectBox().withName("select").withOptions(
					InfoOptionBeanBuilder.aStaticInfoOption().withName("s-1").build(),
					InfoOptionBeanBuilder.aStaticInfoOption().withName("s-2").build()
			).build(),
			
			InfoFieldBeanBuilder.aTextField().withName("text").build()
		));
		
		
		return criteria;
	}
}
