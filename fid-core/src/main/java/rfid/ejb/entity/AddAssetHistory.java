package rfid.ejb.entity;

import java.util.List;

import javax.persistence.*;

import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.api.HasUser;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.parents.legacy.LegacyBeanTenant;
import com.n4systems.model.user.User;

/**
 * This stores, for each system user, the last options they used when creating a
 * new asset
 * 
 * @author Jesse Miller
 * 
 */
@Entity
@Table(name = "add_asset_history")
public class AddAssetHistory extends EntityWithTenant implements HasUser, HasOwner {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "user_id")
	private User user;
	
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="owner_id", nullable = false)
	private BaseOrg owner;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "asset_type_id")
	private AssetType assetType;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "asset_status_id")
	private AssetStatus assetStatus;

    @Column(name="purchase_order")
	private String purchaseOrder;
	
	
	private Location location = new Location();
	
    @ManyToOne(optional = true)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;
	
	@ManyToMany(targetEntity = InfoOptionBean.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "add_asset_history_infooption", joinColumns = @JoinColumn(name = "add_asset_history_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "infooption_id", referencedColumnName = "uniqueid"))
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
	
	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public AssetStatus getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(AssetStatus assetStatus) {
		this.assetStatus = assetStatus;
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

	
	
    public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedTo) {
		this.assignedUser = assignedTo;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

}
