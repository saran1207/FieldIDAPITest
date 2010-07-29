package com.n4systems.webservice.predefinedlocation;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.webservice.dto.AbstractBaseServiceDTO;

public class PredefinedLocationServiceDTO extends AbstractBaseServiceDTO {
	private String name;
	private String levelName;
	private Long parentId;
	private List<Long> searchIds = new ArrayList<Long>();
	private boolean deleted;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public List<Long> getSearchIds() {
		return searchIds;
	}

	public void setSearchIds(List<Long> searchIds) {
		this.searchIds = searchIds;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
