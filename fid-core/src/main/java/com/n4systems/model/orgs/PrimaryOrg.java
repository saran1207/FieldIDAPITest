package com.n4systems.model.orgs;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.api.ExternalCredentialProvider;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.DenyReadOnlyUsersAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.services.limiters.LimitType;

@Entity
@Table(name = "org_primary")
@PrimaryKeyJoinColumn(name="org_id")
public class PrimaryOrg extends InternalOrg implements ExternalCredentialProvider {
	private static final long serialVersionUID = 1L;

	@Embedded
	private TenantLimit limits = new TenantLimit();

	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@JoinTable(
            name = "org_extendedfeatures",
            joinColumns = @JoinColumn(name="org_id")
    )
    @Column(name="feature", nullable=false)
	private Set<ExtendedFeature> extendedFeatures = new HashSet<ExtendedFeature>();

	@Column(name="usingserialnumber", nullable=false)
	private boolean usingSerialNumber = true;
	
	@Column(name="serialnumberformat", nullable=false)
	private String serialNumberFormat = "NSA%y-%g";

	@Column(name="website", length = 2056)
	private String webSite;

	@Column(name="dateformat", nullable = false)
	private String dateFormat = "MM/dd/yy";
	
	@Column(name="externalid", nullable = true)
	private Long externalId;
	
	@Column(name="externalpassword")
	private String externalPassword;	

	@Column(name="externalusername")
	private String externalUserName;
	
	@Column(name="autopublish", nullable=false)
	private boolean autoPublish;
	
	@Column(name="autoaccept", nullable=false)
	private boolean autoAcceptConnections;
	
	@Column(nullable=false)
	private boolean plansAndPricingAvailable;

    @Column(name="searchable_on_safety_network", nullable=false)
    private boolean searchableOnSafetyNetwork;
    
	public PrimaryOrg() {}
	
	@Override
	@AllowSafetyNetworkAccess
    @DenyReadOnlyUsersAccess
	public PrimaryOrg getPrimaryOrg() {
		return this;
	}
	
	@Override
    @DenyReadOnlyUsersAccess
	public InternalOrg getInternalOrg() {
		return this;
	}

	@Override
    @DenyReadOnlyUsersAccess
	public SecondaryOrg getSecondaryOrg() {
		return null;
	}
	
	@Override
    @DenyReadOnlyUsersAccess
	public CustomerOrg getCustomerOrg() {
		return null;
	}

	@Override
    @DenyReadOnlyUsersAccess
	public DivisionOrg getDivisionOrg() {
		return null;
	}
	
	public String getFilterPath() {
		return null;
	}

	@Override
	public BaseOrg getParent() {
		return null;
	}
	
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
	
	public boolean hasExtendedFeature(ExtendedFeature feature) {
		return extendedFeatures.contains(feature);
	}

	public boolean isUsingSerialNumber() {
		return usingSerialNumber;
	}

	public void setUsingSerialNumber(boolean usingSerialNumber) {
		this.usingSerialNumber = usingSerialNumber;
	}

	public String getSerialNumberFormat() {
		return serialNumberFormat;
	}

	public void setSerialNumberFormat(String serialNumberFormat) {
		this.serialNumberFormat = serialNumberFormat;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public SimpleDateFormat getDateFormatter() {
		return new SimpleDateFormat(dateFormat);
	}
	
	public Long getExternalId() {
		return externalId;
	}

	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}

	public String getExternalPassword() {
		return externalPassword;
	}

	public void setExternalPassword(String externalPassword) {
		this.externalPassword = externalPassword;
	}

	public String getExternalUserName() {
		return externalUserName;
	}

	public void setExternalUserName(String externalUserName) {
		this.externalUserName = externalUserName;
	}

	public boolean isAutoPublish() {
		return autoPublish;
	}

	public void setAutoPublish(boolean autoPublish) {
		this.autoPublish = autoPublish;
	}

	public boolean isAutoAcceptConnections() {
		return autoAcceptConnections;
	}

	public void setAutoAcceptConnections(boolean autoAcceptConnections) {
		this.autoAcceptConnections = autoAcceptConnections;
	}

	public PrimaryOrg enhance(SecurityLevel level) {
		PrimaryOrg enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		return enhanced;
	}

	public boolean isPlansAndPricingAvailable() {
		return plansAndPricingAvailable;
	}

	public void setPlansAndPricingAvailable(boolean plansAndPricingAvailable) {
		this.plansAndPricingAvailable = plansAndPricingAvailable;
	}

    public boolean isSearchableOnSafetyNetwork() {
        return searchableOnSafetyNetwork;
    }

    public void setSearchableOnSafetyNetwork(boolean searchableOnSafetyNetwork) {
        this.searchableOnSafetyNetwork = searchableOnSafetyNetwork;
    }
    
    public boolean isLiteUsersEnabled(){
    	return getLimits().getLimitForType(LimitType.LITE_USERS) != 0;
    }
    
    public boolean isReadOnlyUsersEnabled(){
    	return getLimits().getLimitForType(LimitType.READONLY_USERS) != 0;
    }
}
