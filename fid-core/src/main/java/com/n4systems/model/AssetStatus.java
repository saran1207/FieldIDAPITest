package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.ArchivableEntityWithTenant;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "assetstatus")
public class AssetStatus extends ArchivableEntityWithTenant implements Listable<Long>, Saveable, NamedEntity {
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

    @Override
    public String toString() {
        return name;
    }


}
