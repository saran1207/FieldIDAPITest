package com.n4systems.fieldid.ws.v1.resources.assettype.attributes;

import java.util.ArrayList;
import java.util.List;

public class ApiSelectBoxAttribute extends ApiAttribute {
	private List<ApiAttributeOption> options;
	
	protected ApiSelectBoxAttribute(String type, List<ApiAttributeOption> options) {
		super(type);
		this.options = options;
	}
	
	public ApiSelectBoxAttribute(List<ApiAttributeOption> options) {
		this("selectbox", options);
	}
	
	public ApiSelectBoxAttribute() {
		this(new ArrayList<ApiAttributeOption>());
	}

	public List<ApiAttributeOption> getOptions() {
		return options;
	}

	public void setOptions(List<ApiAttributeOption> options) {
		this.options = options;
	}

}
