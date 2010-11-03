package com.n4systems.fieldid.actions.asset;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.util.StringListingPair;
import com.opensymphony.xwork2.TextProvider;

public enum PublishedState {
	PUBLISHED		("label.dopublish", "label.dopublished"),
	NOTPUBLISHED	("label.donotpublish", "label.notpublished");
	
	private final String label;
	private final String pastTenseLabel;
	
	PublishedState(String label, String pastTenseLabel) {
		this.label = label;
		this.pastTenseLabel = pastTenseLabel;
	}

	public String getLabel() {
		return label;
	}
	
	public String getPastTenseLabel() {
		return pastTenseLabel;
	}

	public boolean isPublished() {
		return (this == PUBLISHED);
	}
	
	/**
	 * Returns a StringListingPair List of all states for use in select boxes.  Labels are resolved to text using the labelResolver
	 * @param labelResolver	TextProvider to resolve labels via {@link TextProvider#getText(String)}
	 * @return	List of StringListingParis
	 */
	public static List<StringListingPair> getPublishedStates(TextProvider labelResolver) {
		List<StringListingPair> states = new ArrayList<StringListingPair>();
		for (PublishedState state: values()) {
			states.add(new StringListingPair(state.name(), labelResolver.getText(state.getLabel())));
		}
		return states;
	}
	
	public static PublishedState resolvePublishedState(boolean published) {
		return (published) ? PUBLISHED : NOTPUBLISHED;
	}
}
