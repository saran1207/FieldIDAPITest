package com.n4systems.fieldid.ws.v2.resources.setupdata.unit;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel2;

public class ApiUnit extends ApiReadOnlyModel2 {
	private String name;
	private String shortName;
	private boolean selectable;
	private ApiUnit child;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public ApiUnit getChild() {
		return child;
	}

	public void setChild(ApiUnit child) {
		this.child = child;
	}

}
