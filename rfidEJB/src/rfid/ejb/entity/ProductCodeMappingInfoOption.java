package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.parents.legacy.LegacyBaseEntity;


@Entity
@Table (name = "productcodemapping_infooption")
public class ProductCodeMappingInfoOption extends LegacyBaseEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne 
	@JoinColumn (name="r_productCodeMapping")
	private ProductCodeMappingBean productCodeMapping;
	
	@ManyToOne
	@JoinColumn (name="r_infoOption")
	private InfoOptionBean infoOption;

	public ProductCodeMappingBean getProductCodeMapping() {
		return productCodeMapping;
	}

	public void setProductCodeMapping(ProductCodeMappingBean productCodeMapping) {
		this.productCodeMapping = productCodeMapping;
	}

	public InfoOptionBean getInfoOption() {
		return infoOption;
	}

	public void setInfoOption(InfoOptionBean infoOption) {
		this.infoOption = infoOption;
	}
	
	
	
	
}
