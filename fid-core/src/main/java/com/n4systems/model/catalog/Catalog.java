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
    @JoinTable(name="catalogs_eventtypes", joinColumns = @JoinColumn(name="catalogs_id"), inverseJoinColumns = @JoinColumn(name="publishedeventtypes_id"))
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
		this.publishedAssetTypes.clear();
		this.publishedAssetTypes.addAll(publishedAssetTypes);
	}

	public Set<EventType> getPublishedEventTypes() {
		return publishedEventTypes;
	}

	public void setPublishedEventTypes(Set<EventType> publishedEventTypes) {
		this.publishedEventTypes.clear();
		this.publishedEventTypes.addAll(publishedEventTypes);
	}

	public boolean hasTypesPublished() {
		return (!publishedEventTypes.isEmpty() || !publishedAssetTypes.isEmpty());
	}
}
