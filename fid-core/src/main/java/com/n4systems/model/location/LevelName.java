package com.n4systems.model.location;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LevelName {
	
	@Column(nullable=false, length=255)
	private String name;
	
	
	public LevelName() {
		this(null);
	}
	
	public LevelName(String name) {
		this.name = name != null ? name : "";
	}

	public String getName() {
		return name;
	}

	
}
