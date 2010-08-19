package com.n4systems.fieldid.actions.utils;

import com.n4systems.model.api.HasOwner;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.persistence.loaders.FilteredIdLoader;

public class OwnerPicker {

	private final FilteredIdLoader<BaseOrg> loader;
	
	private final HasOwner entity;


	public OwnerPicker(FilteredIdLoader<BaseOrg> loader, HasOwner entity) {
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

	public void updateOwner(BaseOrg owner) {
		entity.setOwner(owner);
	}
	
	
}
