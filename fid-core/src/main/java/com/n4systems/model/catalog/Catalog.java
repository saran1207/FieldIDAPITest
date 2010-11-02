package com.n4systems.model.catalog;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name="catalogs")
public class Catalog extends EntityWithTenant implements Saveable{

	private static final long serialVersionUID = 1L;

	@OneToMany(fetch=FetchType.LAZY)
    @JoinTable(name="catalogs_assettypes", joinColumns = @JoinColumn(name="catalogs_id"), inverseJoinColumns = @JoinColumn(name="publishedassettypes_id"))
	private Set<AssetType> publishedAssetTypes = new HashSet<AssetType>();
	
	@OneToMany(fetch=FetchType.LAZY)
    @JoinTable(name="catalogs_inspectiontypes", joinColumns = @JoinColumn(name="catalogs_id"), inverseJoinColumns = @JoinColumn(name="publishedinspectiontypes_id"))
	private Set<EventType> publishedEventTypes = new HashSet<EventType>();
	
	
	public Catalog() {
	}

	public Catalog(Tenant tenant) {
		super(tenant);
	}

	public Set<AssetType> getPublishedAssetTypes() {
		return publishedAssetTypes;
	}

	public void setPublishedAssetTypes(Set<AssetType> publishedAssetTypes) {
		this.publishedAssetTypes = publishedAssetTypes;
	}

	public Set<EventType> getPublishedInspectionTypes() {
		return publishedEventTypes;
	}

	public void setPublishedInspectionTypes(Set<EventType> publishedEventTypes) {
		this.publishedEventTypes = publishedEventTypes;
	}

	public boolean hasTypesPublished() {
		return (!publishedEventTypes.isEmpty() || !publishedAssetTypes.isEmpty());
	}
}
