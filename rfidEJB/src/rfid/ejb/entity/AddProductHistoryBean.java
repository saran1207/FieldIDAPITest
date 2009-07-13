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

import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.JobSite;
import com.n4systems.model.ProductType;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;

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
public class AddProductHistoryBean extends LegacyBaseEntity {
	private static final long serialVersionUID = 1L;

	
	@ManyToOne(optional = true)
	@JoinColumn(name = "r_fieldiduser")
	private UserBean user;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "r_owner")
	private Customer owner;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "r_division")
	private Division division;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "r_producttype")
	private ProductType productType;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "r_productstatus")
	private ProductStatusBean productStatus;
	
	private String purchaseOrder;
	private String location;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "r_jobsite")
	private JobSite jobSite;
	
    @ManyToOne(optional = true)
    @JoinColumn(name = "assigneduser_id")
    private UserBean assignedUser;
	
	@ManyToMany(targetEntity = InfoOptionBean.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "addproducthistory_infooption", joinColumns = @JoinColumn(name = "r_addproducthistory", referencedColumnName = "uniqueid"), inverseJoinColumns = @JoinColumn(name = "r_infooption", referencedColumnName = "uniqueid"))
	private List<InfoOptionBean> infoOptions;


	public UserBean getUser() {
		return user;
	}


	public void setUser(UserBean user) {
		this.user = user;
	}


	public Customer getOwner() {
		return owner;
	}


	public void setOwner(Customer owner) {
		this.owner = owner;
	}


	public Division getDivision() {
		return division;
	}


	public void setDivision(Division division) {
		this.division = division;
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

}
