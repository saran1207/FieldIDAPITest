package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.Product;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;

@Entity
@Table(name = "productserialextensionvalue")
public class ProductSerialExtensionValueBean extends LegacyBaseEntity {
	private static final long serialVersionUID = 1L;

	private String extensionValue;

	@ManyToOne(optional = false)
	@JoinColumn(name = "r_productserial")
	private Product productSerial;

	@ManyToOne(optional = false)
	@JoinColumn(name = "r_productserialextension")
	private ProductSerialExtensionBean productSerialExtension;

	public Product getProductSerial() {
		return productSerial;
	}

	public void setProductSerial(Product productSerial) {
		this.productSerial = productSerial;
	}

	public ProductSerialExtensionBean getProductSerialExtension() {
		return productSerialExtension;
	}

	public void setProductSerialExtension(ProductSerialExtensionBean productSerialExtension) {
		this.productSerialExtension = productSerialExtension;
	}

	public String getExtensionValue() {
		return extensionValue;
	}

	public void setExtensionValue(String extensionValue) {
		this.extensionValue = extensionValue;
	}

}
