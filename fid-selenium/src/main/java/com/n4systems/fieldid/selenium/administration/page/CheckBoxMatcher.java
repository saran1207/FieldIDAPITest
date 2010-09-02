/**
 * 
 */
package com.n4systems.fieldid.selenium.administration.page;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class CheckBoxMatcher extends TypeSafeMatcher<String> {
	public static Matcher<String> aCheckBoxValueOf(boolean status) {
		return new CheckBoxMatcher(status);
	}
	
	private final boolean status;

	public CheckBoxMatcher(boolean status) {
		this.status = status;
	}

	@Override
	public boolean matchesSafely(String item) {
		if (status) {
			return item.equalsIgnoreCase("on");
		} 
		return item.equalsIgnoreCase("off");
	}

	@Override
	public void describeTo(Description arg0) {
	}
}