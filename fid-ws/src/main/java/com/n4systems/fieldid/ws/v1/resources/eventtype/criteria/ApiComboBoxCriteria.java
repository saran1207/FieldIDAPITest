package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import java.util.ArrayList;
import java.util.List;

public class ApiComboBoxCriteria extends ApiCriteria {
	private List<String> options = new ArrayList<String>();

	public ApiComboBoxCriteria(List<String> options) {
		this();
		this.options = options;
	}
	
	public ApiComboBoxCriteria() {
		setCriteriaType("COMBOBOX");
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

}
