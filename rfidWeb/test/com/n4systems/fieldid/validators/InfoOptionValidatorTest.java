package com.n4systems.fieldid.validators;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean.InfoFieldType;

import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.test.helpers.FluentArrayList;


public class InfoOptionValidatorTest {
	private static final String VAILD_INFO_OPTION_NAME = "an option";
	private static final String INVAILD_INFO_OPTION_NAME = "";


	@Test
	public void should_handle_null_list_of_info_option_inputs() throws Exception {
		assertTrue(new InfoOptionValidation(null, null).isValid());
	}
	
	
	@Test
	public void should_ignore_invalid_info_option_if_its_info_field_is_deleted() throws Exception {
		InfoFieldInput infoFieldInput = createInfoFieldWithInfoOptions();
		infoFieldInput.setDeleted(true);
		FluentArrayList<InfoFieldInput> infoFieldInputs = new FluentArrayList<InfoFieldInput>(infoFieldInput);
		
		InfoOptionInput infoOptionInput = new InfoOptionInput();
		infoOptionInput.setInfoFieldIndex(infoFieldInputs.indexOf(infoFieldInput));
		infoOptionInput.setName(INVAILD_INFO_OPTION_NAME);
		
		
		assertTrue(new InfoOptionValidation(new FluentArrayList<InfoOptionInput>(infoOptionInput), infoFieldInputs).isValid());
	}

	
	@Test
	public void should_find_info_option_with_a_blank_name_to_be_invalid() throws Exception {
		InfoFieldInput infoFieldInput = createInfoFieldWithInfoOptions();
		FluentArrayList<InfoFieldInput> infoFieldInputs = new FluentArrayList<InfoFieldInput>(infoFieldInput);
		
		InfoOptionInput infoOptionInput = new InfoOptionInput();
		infoOptionInput.setInfoFieldIndex(infoFieldInputs.indexOf(infoFieldInput));
		infoOptionInput.setName("");
		
		
		assertFalse(new InfoOptionValidation(new FluentArrayList<InfoOptionInput>(infoOptionInput), infoFieldInputs).isValid());
	}
	
	

	
	@Test
	public void should_find_info_option_with_a_blank_name_to_be_invalid_when_it_is_not_the_first_in_the_list() throws Exception {
		InfoFieldInput infoFieldInput = createInfoFieldWithInfoOptions();
		InfoFieldInput infoFieldInput2 = createInfoFieldWithInfoOptions();
		FluentArrayList<InfoFieldInput> infoFieldInputs = new FluentArrayList<InfoFieldInput>(infoFieldInput, infoFieldInput2);
		
		InfoOptionInput validInfoOptionInput = new InfoOptionInput();
		validInfoOptionInput.setInfoFieldIndex(infoFieldInputs.indexOf(infoFieldInput));
		validInfoOptionInput.setName(VAILD_INFO_OPTION_NAME);
		
		
		InfoOptionInput invalidInfoOptionInput = new InfoOptionInput();
		invalidInfoOptionInput.setInfoFieldIndex(infoFieldInputs.indexOf(infoFieldInput2));
		invalidInfoOptionInput.setName(INVAILD_INFO_OPTION_NAME);
		
		
		assertFalse(new InfoOptionValidation(new FluentArrayList<InfoOptionInput>(validInfoOptionInput, invalidInfoOptionInput), infoFieldInputs).isValid());
	}

	private InfoFieldInput createInfoFieldWithInfoOptions() {
		return createInfoFieldInput(InfoFieldType.SelectBox);
	}


	private InfoFieldInput createInfoFieldInput(InfoFieldType type) {
		InfoFieldInput infoFieldInput = new InfoFieldInput();
		infoFieldInput.setUniqueID(new Random().nextLong());
		infoFieldInput.setFieldType(type.toString());
		return infoFieldInput;
	}
	
	@Test
	public void should_find_info_option_with_a_null_name_to_be_valid() throws Exception {
		InfoFieldInput infoFieldInput = createInfoFieldWithInfoOptions();
		FluentArrayList<InfoFieldInput> infoFieldInputs = new FluentArrayList<InfoFieldInput>(infoFieldInput);
		
		InfoOptionInput infoOptionInput = new InfoOptionInput();
		infoOptionInput.setInfoFieldIndex(infoFieldInputs.indexOf(infoFieldInput));
		infoOptionInput.setName(null);
		
		
		assertTrue(new InfoOptionValidation(new FluentArrayList<InfoOptionInput>(infoOptionInput), infoFieldInputs).isValid());
	}
	
	@Test
	public void should_ignore_validating_an_info_option_the_info_option_is_deleted() throws Exception {
		InfoFieldInput infoFieldInput = createInfoFieldWithInfoOptions();
		FluentArrayList<InfoFieldInput> infoFieldInputs = new FluentArrayList<InfoFieldInput>(infoFieldInput);
		
		InfoOptionInput infoOptionInput = new InfoOptionInput();
		infoOptionInput.setInfoFieldIndex(infoFieldInputs.indexOf(infoFieldInput));
		infoOptionInput.setName(INVAILD_INFO_OPTION_NAME);
		infoOptionInput.setDeleted(true);
		
		
		assertTrue(new InfoOptionValidation(new FluentArrayList<InfoOptionInput>(infoOptionInput), infoFieldInputs).isValid());
	}
	
	
	
	
	@Test
	public void should_ignore_info_option_validations_when_the_info_field_does_not_accept_static_info_options() throws Exception {
		InfoFieldInput infoFieldInput = createInfoFieldInput(InfoFieldType.TextField);
		FluentArrayList<InfoFieldInput> infoFieldInputs = new FluentArrayList<InfoFieldInput>(infoFieldInput);
		
		InfoOptionInput infoOptionInput = new InfoOptionInput();
		infoOptionInput.setInfoFieldIndex(infoFieldInputs.indexOf(infoFieldInput));
		infoOptionInput.setName(INVAILD_INFO_OPTION_NAME);
		
		
		assertTrue(new InfoOptionValidation(new FluentArrayList<InfoOptionInput>(infoOptionInput), infoFieldInputs).isValid());
	}
	
	
	
}
