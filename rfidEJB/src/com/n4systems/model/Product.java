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

import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductSerialExtensionValueBean;
import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.api.Exportable;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithOwner;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SafetyNetworkSecurityCache;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;


@Entity
@Table(name = "products")
public class Product extends ArchivableEntityWithOwner implements Listable<Long>, NetworkEntity<Product>, Exportable {
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
	private String location;
	
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
	private ProductType type;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "productserial_infooption", joinColumns = @JoinColumn(name = "r_productserial", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "r_infooption", referencedColumnName = "uniqueid"))
	private Set<InfoOptionBean> infoOptions = new HashSet<InfoOptionBean>();

	@ManyToOne(optional = true)
	private ProductStatusBean productStatus;

	@OneToMany(mappedBy = "productSerial", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
	private Set<ProductSerialExtensionValueBean> productSerialExtensionValues = new HashSet<ProductSerialExtensionValueBean>();

	@ManyToOne(optional = true)
	private User identifiedBy;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "assigneduser_id")
    private User assignedUser;
    
    @Transient
    private List<SubProduct> subProducts = new ArrayList<SubProduct>();
    
    @ManyToMany( fetch= FetchType.LAZY )
    @JoinTable(name = "projects_products" )
    private List<Project> projects = new ArrayList<Project>();
    
    @Column(name="published", nullable=false)
    private boolean published = false;
    
    @JoinColumn(name = "linked_id", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Product linkedProduct;

    @Column(insertable=false, updatable=false)
    private Long linked_id;
    
    @Column(name="countstowardslimit", nullable=false)
    private boolean countsTowardsLimit = true;
    
    private Location advancedLocation = new Location();
    
    @Transient
    private Long last_linked_id;
    
	public Product() {
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
		adjustProductForSave();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		adjustProductForSave();
	}

	private void adjustProductForSave() {
		trimSerialNumber();
		trimRfidNumber();
		removeBlankInfoOptions();
		synchronizeNetworkId();
	}
	
	private void synchronizeNetworkId() {
		if (linkedProduct != null) {
			networkId = linkedProduct.getNetworkId();
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
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Long getUniqueID() {
		return getId();
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public LineItem getShopOrder() {
		return shopOrder;
	}

	public void setShopOrder(LineItem orderMaster) {
		this.shopOrder = orderMaster;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}
	
	@Deprecated
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public ProductType getProductType() {
		return getType();
	}

	@Deprecated
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public ProductType getProductInfo() {
		return getType();
	}

	@Deprecated
	public void setProductType(ProductType productType) {
		setType( productType );
	}
	
	public void setInfoOptions(Set<InfoOptionBean> infoOptions) {
		this.infoOptions = infoOptions;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
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

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getDescription() {
		return type.prepareDescription(infoOptions);
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public ProductStatusBean getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ProductStatusBean productStatusBean) {
		this.productStatus = productStatusBean;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public String getMobileGUID() {
		return mobileGUID;
	}

	public void setMobileGUID(String mobileGUID) {
		this.mobileGUID = mobileGUID;
	}

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public Set<ProductSerialExtensionValueBean> getProductSerialExtensionValues() {
		return productSerialExtensionValues;
	}

	public void setProductSerialExtensionValues(
			Set<ProductSerialExtensionValueBean> productSerialExtensionValues) {
		this.productSerialExtensionValues = productSerialExtensionValues;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getCustomerRefNumber() {
		return customerRefNumber;
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public User getIdentifiedBy() {
		return identifiedBy;
	}

	public void setIdentifiedBy(User identifiedBy) {
		this.identifiedBy = identifiedBy;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Order getCustomerOrder() {
		return customerOrder;
	}

	public void setCustomerOrder(Order customerOrder) {
		this.customerOrder = customerOrder;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public List<InfoOptionBean> getOrderedInfoOptionList() {
		ArrayList<InfoOptionBean> orderedList = new ArrayList<InfoOptionBean>();
		orderedList.addAll(this.infoOptions);
		Collections.sort(orderedList);

		return orderedList;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public Date getLastInspectionDate() {
		return lastInspectionDate;
	}

	public void setLastInspectionDate(Date lastInspectionDate) {
		this.lastInspectionDate = lastInspectionDate;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public String getProductExtensionValue( String name ) {
		if ( productSerialExtensionValues != null) {
			for (ProductSerialExtensionValueBean productSerialExtensionValue : productSerialExtensionValues ) {
				if (productSerialExtensionValue.getProductSerialExtension().getExtensionKey().equals(name)) {
					return productSerialExtensionValue.getExtensionValue();
				}
			}
		}
		return null;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public PlainDate getIdentified() {
		return (identified != null) ? new PlainDate(identified) : null;
	}
	
	public void setIdentified(PlainDate identified) {
		this.identified = identified;
	}
	
	public void setIdentified(Date identified) {
		this.identified = new PlainDate(identified);
	}
	
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=true)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
    public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedTo) {
		this.assignedUser = assignedTo;
	}
	
	public boolean isAssigned() {
		return assignedUser != null;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isMasterProduct( ) {
		return !subProducts.isEmpty();
	}
	
	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getDisplayName() {
		return getSerialNumber();
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
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

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public String getArchivedSerialNumber() {
		return archivedSerialNumber;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public List<Project> getProjects() {
		return projects;
	}

	@Override
    public String toString() {
		String tenantName = (getTenant() != null) ? getTenant().getName() : "null";
	    return String.format("%s (%d) tenant %s", getSerialNumber(), getId(), tenantName);
    }

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public List<SubProduct> getSubProducts() {
		return subProducts;
	}

	public void setSubProducts(List<SubProduct> subProducts) {
		this.subProducts = subProducts;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Product getLinkedProduct() {
		return linkedProduct;
	}

	public void setLinkedProduct(Product linkedProduct) {
		this.linkedProduct = linkedProduct;
		this.linked_id = (linkedProduct != null) ? linkedProduct.getId() : null;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isLinked() {
		return (linkedProduct != null);
	}

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public boolean isCountsTowardsLimit() {
		return countsTowardsLimit;
	}

	public void setCountsTowardsLimit(boolean countsTowardsLimit) {
		this.countsTowardsLimit = countsTowardsLimit;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Long getNetworkId() {
		return networkId;
	}
	
	public boolean linkedProductHasChanged() {
		return (last_linked_id != linked_id);
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
		return SafetyNetworkSecurityCache.getSecurityLevel(fromOrg, getOwner());
	}
	
	public Product enhance(SecurityLevel level) {
		Product enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
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

	public Location getAdvancedLocation() {
		return advancedLocation;
	}

	public void setAdvancedLocation(Location advancedLocation) {
		this.advancedLocation = advancedLocation;
	}
	
}
