package com.n4systems.fieldid.validators;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.viewhelpers.TrimmedString;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.validator.ActionValidatorManager;
import com.opensymphony.xwork2.validator.ActionValidatorManagerFactory;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;


public class UniqueInfoFieldValidatorTest {
			
	private ActionValidatorManager avm;
	private List<TrimmedString> inputFields;
	private TestModel crud = new TestModel();

	@SuppressWarnings("unused")
	@Validation
	private class TestModel implements ValidationAware  {
		
		List<TrimmedString> modelFields = new ArrayList<TrimmedString>();
		
		@SuppressWarnings("unchecked")
		private Map fieldErrors = new HashedMap();
		
		@CustomValidator(type = "uniqueInfoFieldValidator", message = "", key = "error.duplicateinfofieldname")
		public void setModelFields(List<TrimmedString> fields){
			modelFields=fields;
		}

		public List<TrimmedString> getModelFields() {
			return modelFields;
		}

		public void addActionError(String arg0) {}

		public void addActionMessage(String arg0) {	}

		@SuppressWarnings("unchecked")
		public void addFieldError(String arg0, String arg1) {
			fieldErrors.put(arg0, arg1);
		}

		@SuppressWarnings("unchecked")
		public Collection getActionErrors() {
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Collection getActionMessages() {
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Map getFieldErrors() {
			return fieldErrors;
		}

		@Override
		public boolean hasActionErrors() {
			return false;
		}

		@Override
		public boolean hasActionMessages() {
			return false;
		}

		@Override
		public boolean hasErrors() {
			return false;
		}

		@Override
		public boolean hasFieldErrors() {
			return false;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setActionErrors(Collection arg0) {
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setActionMessages(Collection arg0) {
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setFieldErrors(Map arg0) {
			this.fieldErrors = arg0;
		}
	}

	@Before
	public void createObjectFactory() {
		ObjectFactory.setObjectFactory(new ObjectFactory());
	}
	
	@After
	public void teardownObjectFactory() {
		ObjectFactory.setObjectFactory(null);
	}
	
	@Before
	public void createValidator() {
		avm = ActionValidatorManagerFactory.getInstance();
		inputFields = new ArrayList<TrimmedString>();
	}

	@Test
	public void should_raise_field_error_on_duplicate_field_names() throws ValidationException {
		
		inputFields.add(new TrimmedString("b"));
		inputFields.add(new TrimmedString("b"));
		crud.modelFields = inputFields;
		avm.validate(crud, "uniqueInfoFieldValidator");
		assertThat(crud.getFieldErrors().size(), equalTo(1));
	}
	
	@Test
	public void should_raise_field_error_on_duplicate_untrimmed_field_names() throws ValidationException{
		
		inputFields.add(new TrimmedString("   d     "));
		inputFields.add(new TrimmedString("   e"));
		inputFields.add(new TrimmedString("ee      "));
		inputFields.add(new TrimmedString("         d                  "));
				
		crud.modelFields = inputFields;
		avm.validate(crud, "uniqueInfoFieldValidator");
		assertThat(crud.getFieldErrors().size(), equalTo(1));
	}
	@Test
	public void should_pass_on_valid_untrimmed_input() throws ValidationException{
		inputFields.add(new TrimmedString("   e     "));
		inputFields.add(new TrimmedString("               f "));
				
		crud.modelFields = inputFields;
		avm.validate(crud, "uniqueInfoFieldValidator");
		assertThat(crud.getFieldErrors().size(), equalTo(0));
	}
}
