/**
 * 
 */
package com.n4systems.model;

import com.google.common.collect.Lists;
import com.n4systems.model.api.Listable;
import com.n4systems.util.EnumUtils.LabelledEnumSet;

import java.util.EnumSet;
import java.util.List;


public enum EventResult implements Listable<String> {

	PASS("Pass", "label.pass"), FAIL("Fail", "label.fail"), NA("N/A", "label.na"), VOID("","label.void");

    private static EnumSet<EventResult> validStates = EnumSet.of(PASS,FAIL,NA);

	public static final LabelledEnumSet<EventResult> ALL = new LabelledEnumSet("All", EnumSet.allOf(EventResult.class));
	
	private String displayName;
	private String label;
	
	EventResult(String displayName, String label) {
		this.displayName = displayName;
		this.label = label;
	}
	
	@Override
	public String getId() {
		return name();
	} 
	
	@Override
	public String getDisplayName() {
		return displayName;
	}

	public String getLabel() {
		return label;
	}

    public static List<EventResult> getValidEventResults() {
        // exclude VOID.  that only applies when an event is scheduled and nothing has been done to it.  it's inital state.
        return Lists.newArrayList(validStates.iterator());
    }
	
}