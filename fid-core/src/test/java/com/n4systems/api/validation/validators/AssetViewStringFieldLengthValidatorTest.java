package com.n4systems.api.validation.validators;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;

import javax.persistence.Column;

import org.junit.Test;

import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exceptions.Defect;
import com.n4systems.util.reflection.Reflector;


public class AssetViewStringFieldLengthValidatorTest {

	@SuppressWarnings("unused")
	private class TestModel {
		@Column(length=1)
		public String lengthLimitedValue;
		
		public String valueWithNoColumnAnnotation;
		
		
		public String defaultLengthField;
	}
	
	@Test
	public void should_use_length_from_column_annotation_described_on_the_field_for_max_length_in_the_validation() throws Exception {
		AssetViewStringFieldLengthValidator sut = new AssetViewStringFieldLengthValidator() {
			protected Field getField() {
				return Reflector.findField(TestModel.class, "lengthLimitedValue");
			}
		};
		
		assertTrue(sut.validate("12", null, null, null, null).isFailed());
		assertTrue(sut.validate("1", null, null, null, null).isPassed());
	}
	
	@Test
	public void should_find_the_length_pass_if_it_is_smaller_then_or_equal_to_the_max_length_of_the_for_the_field() throws Exception {
		AssetViewStringFieldLengthValidator sut = new AssetViewStringFieldLengthValidator() {
			protected Field getField() {
				return Reflector.findField(TestModel.class, "defaultLengthField"); 
			}
		};
		
		int defaultLength = 255;
		
		assertTrue(sut.validate(bigString(defaultLength + 1), null, null, null, null).isFailed());
		assertTrue(sut.validate(bigString(defaultLength), null, null, null, null).isPassed());
		assertTrue(sut.validate(bigString(defaultLength - 1), null, null, null, null).isPassed());
	}
	
	@Test
	public void should_use_default_length_of_255_when_there_is_no_column_annotation_on_the_field() throws Exception {
		AssetViewStringFieldLengthValidator sut = new AssetViewStringFieldLengthValidator() {
			protected Field getField() {
				return Reflector.findField(TestModel.class, "valueWithNoColumnAnnotation"); 
			}
		};
		assertTrue(sut.validate(bigString(256), null, null, null, null).isFailed());
		assertTrue(sut.validate(bigString(254), null, null, null, null).isPassed());
	}
	
	@Test
	public void should_use_default_length_of_255_when_a_null_field_is_produced_by_the_implmentation_class() throws Exception {
		AssetViewStringFieldLengthValidator sut = new AssetViewStringFieldLengthValidator() {
			protected Field getField() {
				return null; 
			}
		};
		
		assertTrue(sut.validate(bigString(256), null, null, null, null).isFailed());
		assertTrue(sut.validate(bigString(254), null, null, null, null).isPassed());
	}
	
	@Test(expected=Defect.class)
	public void should_fail_when_field_is_not_a_string() throws Exception {
		AssetViewStringFieldLengthValidator sut = new AssetViewStringFieldLengthValidator() {
			@Override
			protected Field getField() {
				return Reflector.findField(TestModel.class, "valueWithNoColumnAnnotation"); 
			}
		};
		assertTrue(sut.validate(1L, null, null, null, null).isFailed());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_give_an_error_message_with_the_feild_name_and_the_max_length_in_it_when_validation_fails() throws Exception {
		AssetViewStringFieldLengthValidator sut = new AssetViewStringFieldLengthValidator() {
			protected Field getField() {
				return Reflector.findField(TestModel.class, "defaultLengthField"); 
			}
		};
		ValidationResult validationResult = sut.validate(bigString(256), null, "the_field", null, null);
		
		assertThat(validationResult.getMessage(), allOf(containsString("the_field"), containsString("255")));
	}
	
	
	@Test
	public void should_pass_validation_when_the_field_value_is_null() throws Exception {
		AssetViewStringFieldLengthValidator sut = new AssetViewStringFieldLengthValidator() {
			protected Field getField() {
				return Reflector.findField(TestModel.class, "defaultLengthField"); 
			}
		};
		
		String emptyCell = null;
		assertTrue(sut.validate(emptyCell, null, null, null, null).isPassed());
	}
	
	
	private String bigString(int size) {
		String str = "";
		for (int i = 0; i < size; i++) {
			str += "a";
		}
		return str;
	}
	
}
