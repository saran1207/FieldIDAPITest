package rfid.ejb.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.AssetType;
import com.n4systems.model.parents.legacy.LegacyBeanTenant;

@Entity
@Table (name = "productcodemapping")
public class AssetCodeMapping extends LegacyBeanTenant {
	private static final long serialVersionUID = 1L;

    @Column(name="productcode")
	private String assetCode;

	private String customerRefNumber;
	
	@ManyToOne
	@JoinColumn (name="r_productinfo")
	private AssetType assetInfo;
	
	@ManyToMany (targetEntity = InfoOptionBean.class, cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable (name = "productcodemapping_infooption",
					joinColumns = @JoinColumn(name = "r_productcodemapping", referencedColumnName = "uniqueid"),
					inverseJoinColumns = @JoinColumn(name = "r_infooption", referencedColumnName = "uniqueid") )
	private List<InfoOptionBean> infoOptions = new ArrayList<InfoOptionBean>();	
	
	
	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public AssetType getAssetInfo() {
		return assetInfo;
	}

	public void setAssetInfo(AssetType assetInfo) {
		this.assetInfo = assetInfo;
	}

	public List<InfoOptionBean> getInfoOptions() {
		return infoOptions;
	}

	public void setInfoOptions(List<InfoOptionBean> infoOptions) {
		this.infoOptions = infoOptions;
	}

	public String getCustomerRefNumber() {
		return customerRefNumber;
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}
}
