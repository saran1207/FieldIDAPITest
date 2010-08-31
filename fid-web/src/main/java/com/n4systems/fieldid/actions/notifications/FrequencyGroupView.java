package com.n4systems.fieldid.actions.notifications;

import com.n4systems.model.api.Listable;

import java.util.ArrayList;
import java.util.List;

public class FrequencyGroupView {
	private String groupName;
	private List<Listable<String>> frequencies = new ArrayList<Listable<String>>();
	
	public FrequencyGroupView(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupName() {
    	return groupName;
    }

	public List<Listable<String>> getFrequencies() {
    	return frequencies;
    }
	
}
