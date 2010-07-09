package com.n4systems.webservice.predefinedlocation;

import com.n4systems.webservice.dto.AbstractBaseServiceDTO;

public class PredefinedLocationServiceDTO extends AbstractBaseServiceDTO {
	private String name;
	private String levelName;
	private Long parentId;

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
}
