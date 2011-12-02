package com.n4systems.fieldid.ws.v1.resources.location;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiPredefinedLocation extends ApiReadonlyModel {
	private String name;
	private String levelName;
	private Long parentId;
	private List<Long> searchIds = new ArrayList<Long>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public List<Long> getSearchIds() {
		return searchIds;
	}

	public void setSearchIds(List<Long> searchIds) {
		this.searchIds = searchIds;
	}

}
