package com.n4systems.fieldid.actions.location;

import com.n4systems.fieldid.viewhelpers.TrimmedString;

public class LevelNameWebModel {
	private Integer index;
	private TrimmedString name;

	public LevelNameWebModel() {
	}

	public void setName(TrimmedString name) {
		this.name = name;
	}

	public TrimmedString getName() {
		return name;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getIndex() {
		return index;
	}
}