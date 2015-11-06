package com.n4systems.model;

import com.google.common.base.Joiner;
import com.n4systems.model.api.*;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.LocationContainer;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithOwner;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;
import rfid.ejb.entity.InfoOptionBean;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "assets")
public class Asset extends ArchivableEntityWithOwner implements Listable<Long>, NetworkEntity<Asset>, Exportable, LocationContainer, HasCreatedModifiedPlatform, HasGpsLocation {
	private static final long serialVersionUID = 1L;
	public static final String[] POST_FETCH_ALL_PATHS = { "infoOptions", "type.infoFields", "type.eventTypes", "type.attachments", "type.subTypes", "projects", "modifiedBy.displayName" };

	@Column(name="network_id", nullable=true)
	private Long networkId;
	
	@Column(nullable=false, length=50)
	private String identifier;
	
	@Column(length=50, name="archivedIdentifier")
	private String archivedIdentifier;
	
	@Column(length=46)
	private String rfidNumber;
	private String customerRefNumber;
	private String purchaseOrder;	
	
	@Column(length=2047)
	private String comments;
	private String mobileGUID;
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date identified;

	@ManyToOne(optional = true)
	@JoinColumn(name = "shoporder_id")
	private LineItem shopOrder;	// was orderMaster

	@ManyToOne(optional = true)
	@JoinColumn(name = "customerorder_id")
	private Order customerOrder;

	@ManyToOne(optional = true)
	private AssetType type;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "asset_infooption", joinColumns = @JoinColumn(name = "r_productserial", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "r_infooption", referencedColumnName = "uniqueid"))
	private Set<InfoOptionBean> infoOptions = new HashSet<InfoOptionBean>();

	@ManyToOne(optional = true)
    @JoinColumn(name="assetstatus_id")
	private AssetStatus assetStatus;

	@ManyToOne(optional = true)
	private User identifiedBy;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "assigneduser_id")
    private User assignedUser;
    
    @Transient
    private List<SubAsset> subAssets = new ArrayList<SubAsset>();
    
    @ManyToMany( fetch= FetchType.LAZY )
    @JoinTable(name = "projects_assets", joinColumns = @JoinColumn(name="asset_id"), inverseJoinColumns = @JoinColumn(name="projects_id"))
    private List<Project> projects = new ArrayList<Project>();
    
    @Column(name="published", nullable=false)
    private boolean published = false;
    
    @JoinColumn(name = "linked_id", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Asset linkedAsset;

    @Column(insertable=false, updatable=false)
    private Long linked_id;

    @Column(name="modified_platform", length = 200)
    private String modifiedPlatform;

    @Column(name="created_platform", length = 200)
    private String createdPlatform;

    @Embedded
    private GpsLocation gpsLocation = new GpsLocation(); 
    
    private Location advancedLocation = new Location();
    
    @Transient
    private Long last_linked_id;
    
    private String nonIntergrationOrderNumber;
    
    private String imageName;

    @Enumerated(EnumType.STRING)
    @Column(name="modified_platform_type")
    private PlatformType modifiedPlatformType;

    @Enumerated(EnumType.STRING)
    @Column(name="created_platform_type")
    private PlatformType createdPlatformType;

	@Column(name="last_event_completed_date")
	@Temporal(TemporalType.TIMESTAMP)
    private Date lastEventDate;

	@Column(name="active_procedure_definition_count")
	private Long activeProcedureDefinitionCount = new Long(-1);

//	@Column(name="last_event_completed_date")
//	private Date lastEventCompletedDate;

    
	public Asset() {
		this.identified = new PlainDate();
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		synchronizeLastLinkedId();
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		adjustAssetForSave();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		adjustAssetForSave();
	}

	private void adjustAssetForSave() {
		ensureMobileGuidIsSet();
		trimSerialNumber();
		trimRfidNumber();
		removeBlankInfoOptions();
		synchronizeNetworkId();
	}
	
	public void ensureMobileGuidIsSet() {
		if (getMobileGUID() == null || getMobileGUID().length() == 0) {
            setMobileGUID(UUID.randomUUID().toString());
		}
	}
	
	private void synchronizeNetworkId() {
		if (linkedAsset != null) {
			networkId = linkedAsset.getNetworkId();
		} else {
			networkId = id;
		}
		synchronizeLastLinkedId();
	}
	
	private void synchronizeLastLinkedId() {
		last_linked_id = linked_id;
	}
	
	private void trimSerialNumber() {
		identifier = trimIdentifier(identifier);
	}
	
	private void trimRfidNumber() {
		rfidNumber = trimIdentifier(rfidNumber);
	}
	
	private String trimIdentifier(String identifier) {
		if (identifier != null && identifier.trim().length() != 0) {
			return  identifier.trim();
		}
		
		return null;
	}

	@Deprecated
	@AllowSafetyNetworkAccess
	public Long getUniqueID() {
		return getId();
	}
	
	@AllowSafetyNetworkAccess
	public LineItem getShopOrder() {
		return shopOrder;
	}

	public void setShopOrder(LineItem orderMaster) {
		this.shopOrder = orderMaster;
	}

	@AllowSafetyNetworkAccess
	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	@AllowSafetyNetworkAccess
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@AllowSafetyNetworkAccess
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@AllowSafetyNetworkAccess
	public AssetType getType() {
		return type;
	}

	public void setType(AssetType type) {
		this.type = type;
	}
	
	@Deprecated
	@AllowSafetyNetworkAccess
	public AssetType getAssetType() {
		return getType();
	}

	@Deprecated
	@AllowSafetyNetworkAccess
	public AssetType getAssetInfo() {
		return getType();
	}

	@Deprecated
	public void setAssetType(AssetType assetType) {
		setType(assetType);
	}
	
	public void setInfoOptions(Set<InfoOptionBean> infoOptions) {
		this.infoOptions = infoOptions;
	}

	@AllowSafetyNetworkAccess
	public Set<InfoOptionBean> getInfoOptions() {
		return infoOptions;
	}
	
	public void removeBlankInfoOptions() {
		if( infoOptions != null ) {
			List<InfoOptionBean> removeList = new ArrayList<InfoOptionBean>();
			for (InfoOptionBean infoOption : infoOptions) {
				if( "".equals( infoOption.getName() ) ) {
					removeList.add( infoOption );
				}
			}
			infoOptions.removeAll(removeList);
		}
	}

	@AllowSafetyNetworkAccess
	public String getDescription() {
		return type.prepareDescription(this, infoOptions);
	}

	@AllowSafetyNetworkAccess
	public AssetStatus getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(AssetStatus assetStatus) {
		this.assetStatus = assetStatus;
	}

	@AllowSafetyNetworkAccess
	public String getMobileGUID() {
		return mobileGUID;
	}

	public void setMobileGUID(String mobileGUID) {
		this.mobileGUID = mobileGUID;
	}

	public String getCustomerRefNumber() {
		return customerRefNumber;
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}

	public User getIdentifiedBy() {
		return identifiedBy;
	}

	public void setIdentifiedBy(User identifiedBy) {
		this.identifiedBy = identifiedBy;
	}

	public Order getCustomerOrder() {
		return customerOrder;
	}

	public void setCustomerOrder(Order customerOrder) {
		this.customerOrder = customerOrder;
	}

	@AllowSafetyNetworkAccess
	public List<InfoOptionBean> getOrderedInfoOptionList() {
		ArrayList<InfoOptionBean> orderedList = new ArrayList<InfoOptionBean>();
		orderedList.addAll(this.infoOptions);
		Collections.sort(orderedList);

		return orderedList;
	}

	@AllowSafetyNetworkAccess
	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	@AllowSafetyNetworkAccess
	public PlainDate getIdentified() {
		return (identified != null) ? new PlainDate(identified) : null;
	}
	
	public void setIdentified(PlainDate identified) {
		this.identified = identified;
	}
	
	public void setIdentified(Date identified) {
		this.identified = new PlainDate(identified);
	}
	

	
	@AllowSafetyNetworkAccess
    public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedTo) {
		this.assignedUser = assignedTo;
	}
	
	public boolean isAssigned() {
		return assignedUser != null;
	}

	@AllowSafetyNetworkAccess
	public boolean isMasterAsset( ) {
		return !subAssets.isEmpty();
	}
	
	@Override
	@AllowSafetyNetworkAccess
	public String getDisplayName() {
		return getIdentifier();
	}
	
	@AllowSafetyNetworkAccess
	public long getNextInfoOptionWeight() {
		long highestWeight = -1L;
		
		if(infoOptions != null) {
			for(InfoOptionBean opt: infoOptions) {
				if(highestWeight < opt.getWeight()) {
					highestWeight = opt.getWeight();
				}
			}
		}
		
		return highestWeight + 1;
	}
	
	public void archiveIdentifier() {
		archivedIdentifier = identifier;
		identifier = UUID.randomUUID().toString();
	}

	public String getArchivedIdentifier() {
		return archivedIdentifier;
	}

	@AllowSafetyNetworkAccess
	public List<Project> getProjects() {
		return projects;
	}

	@Override
    public String toString() {
		String tenantName = (getTenant() != null) ? getTenant().getName() : "null";
	    return String.format("%s (%d) tenant %s", getIdentifier(), getId(), tenantName);
    }

	@AllowSafetyNetworkAccess
	public List<SubAsset> getSubAssets() {
		return subAssets;
	}

	public void setSubAssets(List<SubAsset> subAssets) {
		this.subAssets = subAssets;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	@AllowSafetyNetworkAccess
	public Asset getLinkedAsset() {
		return linkedAsset;
	}

	public void setLinkedAsset(Asset linkedAsset) {
		this.linkedAsset = linkedAsset;
		this.linked_id = (linkedAsset != null) ? linkedAsset.getId() : null;
	}
	
	@AllowSafetyNetworkAccess
	public boolean isLinked() {
		return (linkedAsset != null);
	}

	@AllowSafetyNetworkAccess
	public Long getNetworkId() {
		return networkId;
	}

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }
	
	public boolean linkedAssetHasChanged() {
		return (last_linked_id != linked_id);
	}
	
	@Override
	@AllowSafetyNetworkAccess
	public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
		return SecurityLevel.calculateSecurityLevel(fromOrg, getOwner());
	}
	
	@Override
	public Asset enhance(SecurityLevel level) {
		Asset enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setType(enhance(type, level));
		enhanced.setAssignedUser(enhance(assignedUser, level));
		enhanced.setIdentifiedBy(enhance(identifiedBy, level));
		enhanced.setCustomerOrder(enhance(customerOrder, level));
		enhanced.setShopOrder(enhance(shopOrder, level));
		return enhanced;
	}

	@Override
	public String getGlobalId() {
		// Assets are exportable but are never edited
		return null;
	}
	
	@Override
	public void setGlobalId(String globalId) {}
	
	@Override
	@AllowSafetyNetworkAccess
	public Location getAdvancedLocation() {
		return advancedLocation;
	}

	@Override
	public void setAdvancedLocation(Location advancedLocation) {
		this.advancedLocation = advancedLocation;
	}

	public String getNonIntergrationOrderNumber() {
		return nonIntergrationOrderNumber;
	}

	public void setNonIntergrationOrderNumber(String nonIntegrationOrderNumber) {
		this.nonIntergrationOrderNumber = nonIntegrationOrderNumber;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}	
	
	public GpsLocation getGpsLocation() {
		return gpsLocation;
	}
	
	public void setGpsLocation(GpsLocation gpsLocation) {
		this.gpsLocation = gpsLocation;
	}

    public String getModifiedPlatform() {
        return modifiedPlatform;
    }

    public void setModifiedPlatform(String modifiedPlatform) {
        this.modifiedPlatform = modifiedPlatform;
    }

    public String getCreatedPlatform() {
        return createdPlatform;
    }

    public void setCreatedPlatform(String createdPlatform) {
        this.createdPlatform = createdPlatform;
    }

    public String getOrderNumber() {
		if (getShopOrder() != null) {
			return getShopOrder().getOrder().getOrderNumber();
		} else if (getNonIntergrationOrderNumber() != null) {
			return getNonIntergrationOrderNumber();
		} else
			return "";
	}

    public String getVerboseDisplayName() {
        Joiner joiner = Joiner.on(" / ").skipNulls();
        String result = joiner.join(getType().getName(), getIdentifier(), getAssetStatus());
        return result;
    }

    public PlatformType getModifiedPlatformType() {
        return modifiedPlatformType;
    }

    public void setModifiedPlatformType(PlatformType modifiedPlatformType) {
        this.modifiedPlatformType = modifiedPlatformType;
    }

    public PlatformType getCreatedPlatformType() {
        return createdPlatformType;
    }

    public void setCreatedPlatformType(PlatformType createdPlatformType) {
        this.createdPlatformType = createdPlatformType;
    }

    public Date getLastEventDate() {
        return lastEventDate;
    }

    public void setLastEventDate(Date lastEventDate) {
        this.lastEventDate = lastEventDate;
    }

	public Long getActiveProcedureDefinitionCount() {
			return activeProcedureDefinitionCount;
	}

	public void setActiveProcedureDefinitionCount(Long activeProcedureDefinitionCount) {
		this.activeProcedureDefinitionCount = activeProcedureDefinitionCount;
	}
}
