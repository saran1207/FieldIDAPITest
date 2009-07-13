package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.parents.legacy.LegacyBeanTenantId;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;

@Entity
@Table(name = "productserialextension")
public class ProductSerialExtensionBean extends LegacyBeanTenantId implements FilteredEntity {
	private static final long serialVersionUID = 1L;

	private String extensionKey;
	private String extensionLabel;
	
	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets("tenantId");
	}

	public String getExtensionKey() {
		return extensionKey;
	}

	public void setExtensionKey(String extensionKey) {
		this.extensionKey = extensionKey;
	}

	public String getExtensionLabel() {
		return extensionLabel;
	}

	public void setExtensionLabel(String extensionLabel) {
		this.extensionLabel = extensionLabel;
	}


}
