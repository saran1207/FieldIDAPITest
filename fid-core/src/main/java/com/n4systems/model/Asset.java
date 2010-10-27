package com.n4systems.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import rfid.ejb.entity.AssetSerialExtensionValue;
import rfid.ejb.entity.AssetStatus;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.api.Exportable;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.LocationContainer;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithOwner;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;

@Entity
@Table(name = "products")
public class Asset extends ArchivableEntityWithOwner implements Listable<Long>, NetworkEntity<Asset>, Exportable, LocationContainer {
	private static final long serialVersionUID = 1L;
	public static final String[] POST_FETCH_ALL_PATHS = { "infoOptions", "type.infoFields", "type.inspectionTypes", "type.attachments", "type.subTypes", "projects", "modifiedBy.displayName" };
	
	@Column(name="network_id", nullable=true)
	private Long networkId;
	
	@Column(nullable=false, length=50)
	private String serialNumber;
	
	@Column(length=50)
	private String archivedSerialNumber;
	
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

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastInspectionDate;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "shoporder_id")
	private LineItem shopOrder;	// was orderMaster

	@ManyToOne(optional = true)
	@JoinColumn(name = "customerorder_id")
	private Order customerOrder;

	@ManyToOne(optional = true)
	private AssetType type;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "productserial_infooption", joinColumns = @JoinColumn(name = "r_productserial", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "r_infooption", referencedColumnName = "uniqueid"))
	private Set<InfoOptionBean> infoOptions = new HashSet<InfoOptionBean>();

	@ManyToOne(optional = true)
    @JoinColumn(name="productstatus_uniqueid")
	private AssetStatus assetStatus;

	@OneToMany(mappedBy = "assetSerial", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
	private Set<AssetSerialExtensionValue> assetSerialExtensionValues = new HashSet<AssetSerialExtensionValue>();

	@ManyToOne(optional = true)
	private User identifiedBy;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "assigneduser_id")
    private User assignedUser;
    
    @Transient
    private List<SubAsset> subAssets = new ArrayList<SubAsset>();
    
    @ManyToMany( fetch= FetchType.LAZY )
    @JoinTable(name = "projects_products", joinColumns = @JoinColumn(name="products_id"), inverseJoinColumns = @JoinColumn(name="projects_id"))
    private List<Project> projects = new ArrayList<Project>();
    
    @Column(name="published", nullable=false)
    private boolean published = false;
    
    @JoinColumn(name = "linked_id", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Asset linkedAsset;

    @Column(insertable=false, updatable=false)
    private Long linked_id;
    
    @Column(name="countstowardslimit", nullable=false)
    private boolean countsTowardsLimit = true;
    
    private Location advancedLocation = new Location();
    
    @Transient
    private Long last_linked_id;
    
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
		trimSerialNumber();
		trimRfidNumber();
		removeBlankInfoOptions();
		synchronizeNetworkId();
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
		serialNumber = trimIdentifier(serialNumber);
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
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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
		return type.prepareDescription(infoOptions);
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

	public Set<AssetSerialExtensionValue> getAssetSerialExtensionValues() {
		return assetSerialExtensionValues;
	}

	public void setAssetSerialExtensionValues(
			Set<AssetSerialExtensionValue> assetSerialExtensionValues) {
		this.assetSerialExtensionValues = assetSerialExtensionValues;
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
	public Date getLastInspectionDate() {
		return lastInspectionDate;
	}

	public void setLastInspectionDate(Date lastInspectionDate) {
		this.lastInspectionDate = lastInspectionDate;
	}

	@AllowSafetyNetworkAccess
	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	public String getAssetExtensionValue( String name ) {
		if ( assetSerialExtensionValues != null) {
			for (AssetSerialExtensionValue assetSerialExtensionValue : assetSerialExtensionValues) {
				if (assetSerialExtensionValue.getAssetSerialExtension().getExtensionKey().equals(name)) {
					return assetSerialExtensionValue.getExtensionValue();
				}
			}
		}
		return null;
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
	
	@AllowSafetyNetworkAccess
	public String getDisplayName() {
		return getSerialNumber();
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
	
	public void archiveSerialNumber() {
		archivedSerialNumber = serialNumber;
		serialNumber = UUID.randomUUID().toString();
	}

	public String getArchivedSerialNumber() {
		return archivedSerialNumber;
	}

	@AllowSafetyNetworkAccess
	public List<Project> getProjects() {
		return projects;
	}

	@Override
    public String toString() {
		String tenantName = (getTenant() != null) ? getTenant().getName() : "null";
	    return String.format("%s (%d) tenant %s", getSerialNumber(), getId(), tenantName);
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

	public boolean isCountsTowardsLimit() {
		return countsTowardsLimit;
	}

	public void setCountsTowardsLimit(boolean countsTowardsLimit) {
		this.countsTowardsLimit = countsTowardsLimit;
	}

	@AllowSafetyNetworkAccess
	public Long getNetworkId() {
		return networkId;
	}
	
	public boolean linkedAssetHasChanged() {
		return (last_linked_id != linked_id);
	}
	
	@AllowSafetyNetworkAccess
	public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
		return SecurityLevel.calculateSecurityLevel(fromOrg, getOwner());
	}
	
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
		// Products are exportable but are never edited
		return null;
	}
	
	@Override
	public void setGlobalId(String globalId) {}
	
	@AllowSafetyNetworkAccess
	public Location getAdvancedLocation() {
		return advancedLocation;
	}

	public void setAdvancedLocation(Location advancedLocation) {
		this.advancedLocation = advancedLocation;
	}
	
}
