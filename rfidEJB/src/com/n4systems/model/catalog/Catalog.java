package com.n4systems.model.catalog;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.TenantOrganization;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name="catalogs")
public class Catalog extends EntityWithTenant implements Saveable{

	private static final long serialVersionUID = 1L;

	@OneToMany(fetch=FetchType.LAZY)
	private Set<ProductType> publishedProductTypes = new HashSet<ProductType>();
	
	@OneToMany(fetch=FetchType.LAZY)
	private Set<InspectionType> publishedInspectionTypes = new HashSet<InspectionType>();
	
	
	public Catalog() {
	}

	public Catalog(TenantOrganization tenant) {
		super(tenant);
	}

	public Set<ProductType> getPublishedProductTypes() {
		return publishedProductTypes;
	}

	public void setPublishedProductTypes(Set<ProductType> publishedProductTypes) {
		this.publishedProductTypes = publishedProductTypes;
	}

	public Set<InspectionType> getPublishedInspectionTypes() {
		return publishedInspectionTypes;
	}

	public void setPublishedInspectionTypes(Set<InspectionType> publishedInspectionTypes) {
		this.publishedInspectionTypes = publishedInspectionTypes;
	}

	public boolean hasTypesPublished() {
		return (!publishedInspectionTypes.isEmpty() || !publishedProductTypes.isEmpty());
	}
}
