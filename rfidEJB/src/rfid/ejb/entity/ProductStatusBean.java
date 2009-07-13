package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.parents.legacy.LegacyBeanTenantWithCreateModifyDate;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;

@Entity
@Table(name = "ProductStatus")
public class ProductStatusBean extends LegacyBeanTenantWithCreateModifyDate implements FilteredEntity {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String modifiedBy;

	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets(TENANT_ID_FIELD);
	}
	
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
