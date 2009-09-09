package com.n4systems.fieldid.actions.utils;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.persistence.loaders.FilteredIdLoader;

public class OwnerPicker {

	private final FilteredIdLoader<BaseOrg> loader;
	
	private final EntityWithOwner entity;


	public OwnerPicker(FilteredIdLoader<BaseOrg> loader, EntityWithOwner entity) {
		super();
		this.loader = loader;
		this.entity = entity;
	}

	public BaseOrg getOwner() {
		return entity.getOwner();
	}
	
	public Long getOwnerId() {
		return (entity.getOwner() != null) ? entity.getOwner().getId() : null;
	}
	
	public void setOwnerId(Long id) {
		if (id == null) {
			entity.setOwner(null);
		} else if (entity.getOwner() == null || !entity.getOwner().getId().equals(id)) {
			entity.setOwner(loader.setId(id).load());
		}
	}
	
}
