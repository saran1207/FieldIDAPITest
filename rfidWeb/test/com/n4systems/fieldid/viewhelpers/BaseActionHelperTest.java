package com.n4systems.fieldid.viewhelpers;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseActionHelperTest {

	@Test
	public void should_make_no_changes_to_string_shorter_than_the_max_number_of_characters() {
		BaseActionHelper sut = new BaseActionHelper();
		String shorterThanMaxNumberOfCharacters = "this is a string";
		
		String actualString = sut.trimString(shorterThanMaxNumberOfCharacters, shorterThanMaxNumberOfCharacters.length() + 1);
		assertEquals(shorterThanMaxNumberOfCharacters, actualString);
	}
	
	@Test
	public void should_make_no_changes_to_string_the_same_length_as_the_max_number_of_characters() {
		BaseActionHelper sut = new BaseActionHelper();
		String shorterThanMaxNumberOfCharacters = "this is a string";
		
		String actualString = sut.trimString(shorterThanMaxNumberOfCharacters, shorterThanMaxNumberOfCharacters.length());
		assertEquals(shorterThanMaxNumberOfCharacters, actualString);
	}
	
	@Test
	public void should_make_trim_the_string_and_add_3_dots() {
		BaseActionHelper sut = new BaseActionHelper();
		String shorterThanMaxNumberOfCharacters = "this is a string";
		String expectedString = "this is a st...";
		
		String actualString = sut.trimString(shorterThanMaxNumberOfCharacters, expectedString.length());
		assertEquals(expectedString, actualString);
	}
	
	

}
