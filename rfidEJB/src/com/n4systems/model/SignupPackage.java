package com.n4systems.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.tenant.TenantLimit;

@Entity
@Table(name = "signuppackage")
public class SignupPackage extends AbstractEntity implements Saveable {
	private static final long serialVersionUID = 1L;
	
	private String syncId;
	private String name;
	
	@Embedded
	private TenantLimit limits = new TenantLimit();
	
	@CollectionOfElements(fetch= FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Set<ExtendedFeature> extendedFeatures = new HashSet<ExtendedFeature>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "signupPackage", cascade=CascadeType.ALL)
	private List<ContractPricing> contractPricing = new ArrayList<ContractPricing>();
	
	public TenantLimit getLimits() {
		return limits;
	}

	public void setLimits(TenantLimit limits) {
		this.limits = limits;
	}

	public Set<ExtendedFeature> getExtendedFeatures() {
		return extendedFeatures;
	}

	public void setExtendedFeatures(Set<ExtendedFeature> extendedFeatures) {
		this.extendedFeatures = extendedFeatures;
	}

	public List<ContractPricing> getContractPricing() {
		return contractPricing;
	}

	public void setContractPricing(List<ContractPricing> contractPricing) {
		this.contractPricing = contractPricing;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getSyncId() {
		return syncId;
	}

	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}	
	
}
