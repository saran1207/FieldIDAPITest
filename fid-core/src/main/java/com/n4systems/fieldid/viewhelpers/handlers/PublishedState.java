package com.n4systems.fieldid.viewhelpers.handlers;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.util.StringListingPair;

public enum PublishedState {
	PUBLISHED		("label.publishedstateselector", "Publish Over Safety Network", "label.publishedoversafetynetwork", "Published Over Safety Network"),
	NOTPUBLISHED	("label.unpublishedstateselector", "Do Not Publish Over Safety Network", "label.unpublishedoversafetynetwork", "Not Published Over Safety Network");
	
	private final String label;
    private final String labelString;
	private final String pastTenseLabel;
    private final String pastTenseString;

    PublishedState(String label, String labelString, String pastTenseLabel, String pastTenseString) {
		this.label = label;
        this.labelString = labelString;
		this.pastTenseLabel = pastTenseLabel;
        this.pastTenseString = pastTenseString;
	}

	public String getLabel() {
		return label;
	}
	
	public String getPastTenseLabel() {
		return pastTenseLabel;
	}

	public String getPastTenseString() {
		return pastTenseString;
	}

	public boolean isPublished() {
		return (this == PUBLISHED);
	}
	
	/**
	 * Returns a StringListingPair List of all states for use in select boxes.  Labels are resolved to text using the labelResolver
	 * @param labelResolver	TextProvider to resolve labels via {@link TextProvider#getText(String)}
	 * @return	List of StringListingParis
	 */
	public static List<StringListingPair> getPublishedStates() {
		List<StringListingPair> states = new ArrayList<StringListingPair>();
		for (PublishedState state: values()) {
			states.add(new StringListingPair(state.name(), state.labelString));
		}
		return states;
	}
	
	public static PublishedState resolvePublishedState(boolean published) {
		return (published) ? PUBLISHED : NOTPUBLISHED;
	}
}
