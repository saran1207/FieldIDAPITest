package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.parents.legacy.LegacyBeanTenantId;

@Entity
@Table(name = "productserialextension")
public class AssetSerialExtension extends LegacyBeanTenantId {
	private static final long serialVersionUID = 1L;

	private String extensionKey;
	private String extensionLabel;

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
