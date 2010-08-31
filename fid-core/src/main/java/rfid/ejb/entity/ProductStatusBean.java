package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.legacy.LegacyBeanTenantWithCreateModifyDate;

@Entity
@Table(name = "ProductStatus")
public class ProductStatusBean extends LegacyBeanTenantWithCreateModifyDate implements Saveable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String modifiedBy;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
