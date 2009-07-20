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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import rfid.ejb.entity.UserBean;

import com.n4systems.model.api.Archivable;
import com.n4systems.model.api.Listable;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.EntityStateField;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.SecurityFilter;


@Entity
@Table(name = "products")
public class Product extends EntityWithTenant implements Listable<Long>, Archivable, FilteredEntity {
	private static final long serialVersionUID = 1L;
	
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
	
	private String uuid;
	private String linkedUuid;
	private String mobileGUID;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Organization organization;
	
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
	private UserBean identifiedBy;
	
	@ManyToOne(optional = true)
	private Customer owner;
    
    @ManyToOne(optional = true)  
    private Division division;
    
    @ManyToOne(optional = true)
    private JobSite	jobSite;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "assigneduser_id")
    private UserBean assignedUser;
    
    @Transient
    private List<SubProduct> subProducts = new ArrayList<SubProduct>();
    
    @EntityStateField
    @Enumerated(EnumType.STRING)
	private EntityState state = EntityState.ACTIVE;
    
    @ManyToMany( fetch= FetchType.LAZY )
    @JoinTable(name = "projects_products" )
    private List<Project> projects = new ArrayList<Project>();
    
	public Product() {
		this.identified = new PlainDate();
	}
	
	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets(TENANT_ID_FIELD, "owner.id", "division.id", null, "state");
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
	public Long getUniqueID() {
		return getId();
	}
	
	public LineItem getShopOrder() {
		return shopOrder;
	}

	public void setShopOrder(LineItem orderMaster) {
		this.shopOrder = orderMaster;
	}

	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	


	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Division getDivision() {
		return division;
	}
	
	public void setDivision(Division division) {
		this.division = division;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}
	
	@Deprecated
	public ProductType getProductType() {
		return getType();
	}

	@Deprecated
	public ProductType getProductInfo() {
		return getType();
	}

	@Deprecated
	public void setProductType(ProductType productType) {
		setType( productType );
	}
	
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	public void setInfoOptions(Set<InfoOptionBean> infoOptions) {
		this.infoOptions = infoOptions;
	}

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

	public String getDescription() {
		return type.prepareDescription(infoOptions);
	}

	public ProductStatusBean getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ProductStatusBean productStatusBean) {
		this.productStatus = productStatusBean;
	}

	public String getMobileGUID() {
		return mobileGUID;
	}

	public void setMobileGUID(String mobileGUID) {
		this.mobileGUID = mobileGUID;
	}

	public Set<ProductSerialExtensionValueBean> getProductSerialExtensionValues() {
		return productSerialExtensionValues;
	}

	public void setProductSerialExtensionValues(
			Set<ProductSerialExtensionValueBean> productSerialExtensionValues) {
		this.productSerialExtensionValues = productSerialExtensionValues;
	}

	public String getCustomerRefNumber() {
		return customerRefNumber;
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}

	public UserBean getIdentifiedBy() {
		return identifiedBy;
	}

	public void setIdentifiedBy(UserBean identifiedBy) {
		this.identifiedBy = identifiedBy;
	}

	public Customer getOwner() {
		return owner;
	}

	public void setOwner(Customer owner) {
		this.owner = owner;
	}

	public Order getCustomerOrder() {
		return customerOrder;
	}

	public void setCustomerOrder(Order customerOrder) {
		this.customerOrder = customerOrder;
	}

	public List<InfoOptionBean> getOrderedInfoOptionList() {
		ArrayList<InfoOptionBean> orderedList = new ArrayList<InfoOptionBean>();
		orderedList.addAll(this.infoOptions);
		Collections.sort(orderedList);

		return orderedList;
	}

	public Date getLastInspectionDate() {
		return lastInspectionDate;
	}

	public void setLastInspectionDate(Date lastInspectionDate) {
		this.lastInspectionDate = lastInspectionDate;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getLinkedUuid() {
		return linkedUuid;
	}

	public void setLinkedUuid(String linkedUuid) {
		this.linkedUuid = linkedUuid;
	}
	/**
	 * Determines if this product is linked to a product on a manufacturer account
	 * @return
	 */
	public boolean isLinkedToManufacturer() {
		return ( linkedUuid != null );
	}
	
	/**
	 * determines if there is a possible link to this product from an inspector tenant.
	 * @return
	 */
	public boolean hasPossibleInspectorProductLinked() {
		return ( uuid != null );
	}

	public PlainDate getIdentified() {
		return (identified != null) ? new PlainDate(identified) : null;
	}
	public void setIdentified(PlainDate identified) {
		this.identified = identified;
	}
	
	public void setIdentified(Date identified) {
		this.identified = new PlainDate(identified);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public JobSite getJobSite() {
		return jobSite;
	}

	public void setJobSite( JobSite jobSite ) {
		this.jobSite = jobSite;
	}
	
    public UserBean getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(UserBean assignedTo) {
		this.assignedUser = assignedTo;
	}

		
	public boolean isMasterProduct( ) {
		return !subProducts.isEmpty();
	}
	
	public String getDisplayName() {
		return getSerialNumber();
	}
	
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


	public void activateEntity() {
		state = EntityState.ACTIVE;
	}

	public void archiveEntity() {
		state = EntityState.ARCHIVED;
	}

	public EntityState getEntityState() {
		return state;
	}

	public void retireEntity() {
		state = EntityState.RETIRED;
	}
	
	public void setRetired( boolean retired ) {
		if( retired ) {
			retireEntity();
		} else  {
			activateEntity();
		}
	}
	
	public boolean isRetired() {
		return state == EntityState.RETIRED;
	}

	public boolean isActive() {
		return state == EntityState.ACTIVE;
	}
	
	public boolean isArchived() {
		return state == EntityState.ARCHIVED;
	}
	
	
	public void archiveSerialNumber() {
		archivedSerialNumber = serialNumber;
		serialNumber = UUID.randomUUID().toString();
	}

	public String getArchivedSerialNumber() {
		return archivedSerialNumber;
	}

	public List<Project> getProjects() {
		return projects;
	}

	@Override
    public String toString() {
	    return getSerialNumber() + " (" + getId() + ") tenant " + getTenant().getName();
    }

	public List<SubProduct> getSubProducts() {
		return subProducts;
	}

	public void setSubProducts(List<SubProduct> subProducts) {
		this.subProducts = subProducts;
	}

}
