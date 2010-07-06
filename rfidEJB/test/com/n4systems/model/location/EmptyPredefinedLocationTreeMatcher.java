/**
 * 
 */
package com.n4systems.model.location;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

public class EmptyPredefinedLocationTreeMatcher extends TypeSafeMatcher<PredefinedLocationTree> {
	public static Matcher<PredefinedLocationTree> anEmptyLocationTree() {
		return new EmptyPredefinedLocationTreeMatcher();
		
		
	}
	
	@Override
	public boolean matchesSafely(PredefinedLocationTree item) {
		return item.isEmpty();
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("an empty tree");
		
	}
}