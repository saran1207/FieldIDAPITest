package com.n4systems.fieldid.api.mobile.resources.assettype.attributes;

import java.util.ArrayList;
import java.util.List;

public class ApiComboBoxAttribute extends ApiSelectBoxAttribute {

	public ApiComboBoxAttribute(List<ApiAttributeOption> options) {
		super("combobox", options);
	}
	
	public ApiComboBoxAttribute() {
		this(new ArrayList<ApiAttributeOption>());
	}
	
}
