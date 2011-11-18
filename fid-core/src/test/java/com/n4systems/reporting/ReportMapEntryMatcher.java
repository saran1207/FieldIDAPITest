/**
 * 
 */
package com.n4systems.reporting;

import static org.hamcrest.Matchers.*;

import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ReportMapEntryMatcher extends TypeSafeMatcher<Map<String, Object>> {
	
	public static Matcher<Map<String, Object>> hasReportEntry(Matcher<String> key, Matcher<Object> value) {
		return new ReportMapEntryMatcher(key, value);
	}
	
	private Matcher<Map<String, Object>> matcher;

	public ReportMapEntryMatcher(Matcher<String> key, Matcher<Object> value) {
		matcher = hasEntry(key, value);
	}

	public boolean matchesSafely(Map<String, Object> reportMap) {
        return matcher.matches(reportMap);
    }

    public void describeTo(Description description) {
        matcher.describeTo(description);
    }
}