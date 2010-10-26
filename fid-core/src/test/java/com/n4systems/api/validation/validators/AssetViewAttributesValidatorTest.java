package com.n4systems.api.validation.validators;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.n4systems.model.AssetType;
import org.junit.Test;

import com.n4systems.model.builders.InfoFieldBeanBuilder;
import com.n4systems.model.builders.InfoOptionBeanBuilder;

public class AssetViewAttributesValidatorTest {
	private AssetViewAttributesValidator validator = new AssetViewAttributesValidator();
	
	@Test
	public void validation_passes_when_all_fields_set_and_resolved() {
		Map<String, String> attribs = new HashMap<String, String>();
		attribs.put("combo", "c-1");
		attribs.put("select", "s-2");
		attribs.put("text", "dynamic");
		
		assertTrue(validator.validate(attribs, null, "", null, createProductType()).isPassed());
	}
	
	@Test
	public void validation_passes_when_all_non_required_fields_set_and_resolved() {
		Map<String, String> attribs = new HashMap<String, String>();
		attribs.put("combo", "c-1");
		attribs.put("text", "dynamic");
		
		assertTrue(validator.validate(attribs, null, "", null, createProductType()).isPassed());
	}
	
	@Test
	public void validation_fails_on_missing_required_field() {
		Map<String, String> attribs = new HashMap<String, String>();
		attribs.put("combo", "c-1");
		attribs.put("select", "s-2");
		
		assertTrue(validator.validate(attribs, null, "", null, createProductType()).isFailed());
	}
	
	@Test
	public void validation_fails_unresolved_select_box_options() {
		Map<String, String> attribs = new HashMap<String, String>();
		attribs.put("combo", "c-1");
		attribs.put("select", "unresolvalbe");
		attribs.put("text", "dynamic");
		
		assertTrue(validator.validate(attribs, null, "", null, createProductType()).isFailed());
	}
	
	@Test
	public void validation_allows_combo_box_creation() {
		Map<String, String> attribs = new HashMap<String, String>();
		attribs.put("combo", "dynamic");
		attribs.put("select", "s-1");
		attribs.put("text", "dynamic");
		
		assertTrue(validator.validate(attribs, null, "", null, createProductType()).isPassed());
	}
	
	@Test
	public void validation_allows_empty_select_box() {
		Map<String, String> attribs = new HashMap<String, String>();
		attribs.put("combo", "c-1");
		attribs.put("select", "");
		attribs.put("text", "dynamic");
		
		assertTrue(validator.validate(attribs, null, "", null, createProductType()).isPassed());
	}
	
	private AssetType createProductType() {
		AssetType type = new AssetType();
		type.setInfoFields(Arrays.asList(
				
				InfoFieldBeanBuilder.aComboBox().setRequired(true).named("combo").withOptions(
						InfoOptionBeanBuilder.aStaticInfoOption().withName("c-1").build(),
						InfoOptionBeanBuilder.aStaticInfoOption().withName("c-2").build()
				).build(),
				
				InfoFieldBeanBuilder.aSelectBox().named("select").withOptions(
						InfoOptionBeanBuilder.aStaticInfoOption().withName("s-1").build(),
						InfoOptionBeanBuilder.aStaticInfoOption().withName("s-2").build()
				).build(),
				
				InfoFieldBeanBuilder.aTextField().setRequired(true).named("text").build()
		));
		
		return type;
	}
	
}
