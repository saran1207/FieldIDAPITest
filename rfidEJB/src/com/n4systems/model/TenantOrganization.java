package com.n4systems.model;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;

import org.hibernate.annotations.CollectionOfElements;


@SuppressWarnings("serial")
@Entity
public abstract class TenantOrganization extends Organization {
	
	@Column(nullable=false)
	private String dateFormat;
	
	@Column(length=2056)
	private String webSite;
	
	@CollectionOfElements(fetch= FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Set<ExtendedFeature> extendedFeatures = new HashSet<ExtendedFeature>();
	
	public Set<ExtendedFeature> getExtendedFeatures() {
		return extendedFeatures;
	}

	public void setExtendedFeatures(Set<ExtendedFeature> extendedFeatures) {
		this.extendedFeatures = extendedFeatures;
	}
	
	public boolean hasExtendedFeature( ExtendedFeature feature ) {
		return extendedFeatures.contains(feature);
	}
	
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public abstract List<? extends TenantOrganization> getLinkedTenants() ;

	@Override
    public String toString() {
	    return getName() + " (" + getId() + ")";
    }

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
}
