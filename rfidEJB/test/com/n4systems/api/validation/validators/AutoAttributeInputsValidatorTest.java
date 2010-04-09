package com.n4systems.api.validation.validators;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.builders.InfoFieldBeanBuilder;
import com.n4systems.model.builders.InfoOptionBeanBuilder;

public class AutoAttributeInputsValidatorTest {
	private AutoAttributeInputsValidator validator = new AutoAttributeInputsValidator();
	private AutoAttributeCriteria crit;

	@Before
	public void setup_converter() {
		InfoOptionBeanBuilder optionBuilder = InfoOptionBeanBuilder.aStaticInfoOption();
		
		InfoFieldBean in1 = InfoFieldBeanBuilder.aComboBox().named("in1").build();
		in1.setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>(Arrays.asList(
				optionBuilder.forField(in1).withName("in1-1").build(),
				optionBuilder.forField(in1).withName("in1-2").build()
		)));
		
		InfoFieldBean in2 = InfoFieldBeanBuilder.aSelectBox().named("in2").build();
		in2.setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>(Arrays.asList(
				optionBuilder.forField(in2).withName("in2-1").build(),
				optionBuilder.forField(in2).withName("in2-2").build()
		)));
		
		crit = new AutoAttributeCriteria();
		crit.getInputs().add(in1);
		crit.getInputs().add(in2);
	}
	
	@Test
	public void validation_when_all_options_resolve() {
		Map<String, String> options = new HashMap<String, String>();
		options.put("in1", "in1-2");
		options.put("in2", "in2-1");
		
		ValidationResult result = validator.validate(options, null, "", null, crit);
		assertTrue(result.isPassed());
	}
	
	@Test
	public void validation_fails_on_missing_field() {
		Map<String, String> options = new HashMap<String, String>();
		options.put("in1", "in1-2");
		
		ValidationResult result = validator.validate(options, null, "", null, crit);
		assertTrue(result.isFailed());
	}
	
	@Test
	public void validation_fails_on_unresolvable_option() {
		Map<String, String> options = new HashMap<String, String>();
		options.put("in1", "in1-2");
		options.put("in2", "unresolveable");
		
		ValidationResult result = validator.validate(options, null, "", null, crit);
		assertTrue(result.isFailed());
	}
}
