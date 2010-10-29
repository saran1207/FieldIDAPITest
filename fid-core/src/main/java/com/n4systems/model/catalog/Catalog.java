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
import com.n4systems.model.InspectionType;
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
	private Set<InspectionType> publishedInspectionTypes = new HashSet<InspectionType>();
	
	
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

	public Set<InspectionType> getPublishedInspectionTypes() {
		return publishedInspectionTypes;
	}

	public void setPublishedInspectionTypes(Set<InspectionType> publishedInspectionTypes) {
		this.publishedInspectionTypes = publishedInspectionTypes;
	}

	public boolean hasTypesPublished() {
		return (!publishedInspectionTypes.isEmpty() || !publishedAssetTypes.isEmpty());
	}
}
