package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name = "assetstatus")
public class AssetStatus extends EntityWithTenant implements Listable<Long>, Saveable{
	private static final long serialVersionUID = 1L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return name;
	}	
}
