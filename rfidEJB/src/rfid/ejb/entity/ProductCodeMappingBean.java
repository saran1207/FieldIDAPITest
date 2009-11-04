package rfid.ejb.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.ProductType;
import com.n4systems.model.parents.legacy.LegacyBeanTenant;

@Entity
@Table (name = "productcodemapping")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ProductCodeMappingBean extends LegacyBeanTenant {
	private static final long serialVersionUID = 1L;
	
	private String productCode;

	private String customerRefNumber;
	
	@ManyToOne
	@JoinColumn (name="r_productinfo")
	private ProductType productInfo;
	
	@ManyToMany (targetEntity = InfoOptionBean.class, cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable (name = "productcodemapping_infooption",
					joinColumns = @JoinColumn(name = "r_productcodemapping", referencedColumnName = "uniqueid"),
					inverseJoinColumns = @JoinColumn(name = "r_infooption", referencedColumnName = "uniqueid") )
	private List<InfoOptionBean> infoOptions = new ArrayList<InfoOptionBean>();	
	
	
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public ProductType getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(ProductType productInfo) {
		this.productInfo = productInfo;
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
