package com.n4systems.model.promocode;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.tenant.TenantLimit;

@Entity
@Table(name = "promocodes")
public class PromoCode extends AbstractEntity implements Saveable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="code", nullable=false)
	private String code;
	
	@CollectionOfElements(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@JoinTable(
            name = "promocode_extendedfeatures",
            joinColumns = @JoinColumn(name="promocode_id")
    )
    @Column(name="feature", nullable=false)
	private Set<ExtendedFeature> extendedFeatures = new HashSet<ExtendedFeature>();

	
	@Embedded
	private TenantLimit limits = new TenantLimit();
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<ExtendedFeature> getExtendedFeatures() {
		return extendedFeatures;
	}

	public void setExtendedFeatures(Set<ExtendedFeature> extendedFeatures) {
		this.extendedFeatures = extendedFeatures;
	}

	public TenantLimit getLimits() {
		return limits;
	}

	public void setLimits(TenantLimit limits) {
		this.limits = limits;
	}
	
	
	
}
