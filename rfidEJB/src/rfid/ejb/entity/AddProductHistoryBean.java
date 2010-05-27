package rfid.ejb.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.ProductType;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.api.HasUser;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.legacy.LegacyBeanTenant;
import com.n4systems.model.user.User;

/**
 * This stores, for each system user, the last options they used when creating a
 * new product
 * 
 * @see InfoOptionHistoryBean
 * @author Jesse Miller
 * 
 */
@Entity
@Table(name = "addproducthistory")
public class AddProductHistoryBean extends LegacyBeanTenant implements HasUser, HasOwner {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "r_fieldiduser")
	private User user;
	
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="owner_id", nullable = false)
	private BaseOrg owner;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "r_producttype")
	private ProductType productType;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "r_productstatus")
	private ProductStatusBean productStatus;
	
	private String purchaseOrder;
	private String location;
	
    @ManyToOne(optional = true)
    @JoinColumn(name = "assigneduser_id")
    private User assignedUser;
	
	@ManyToMany(targetEntity = InfoOptionBean.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "addproducthistory_infooption", joinColumns = @JoinColumn(name = "r_addproducthistory", referencedColumnName = "uniqueid"), inverseJoinColumns = @JoinColumn(name = "r_infooption", referencedColumnName = "uniqueid"))
	private List<InfoOptionBean> infoOptions;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BaseOrg getOwner() {
		return owner;
	}

	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}
	
	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public ProductStatusBean getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ProductStatusBean productStatus) {
		this.productStatus = productStatus;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public List<InfoOptionBean> getInfoOptions() {
		return infoOptions;
	}

	public void setInfoOptions(List<InfoOptionBean> infoOptions) {
		this.infoOptions = infoOptions;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
    public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedTo) {
		this.assignedUser = assignedTo;
	}

}
