package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.Asset;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;

@Entity
@Table(name = "productserialextensionvalue")
public class AssetSerialExtensionValue extends LegacyBaseEntity {
	private static final long serialVersionUID = 1L;

	private String extensionValue;

	@ManyToOne(optional = false)
	@JoinColumn(name = "r_productserial")
	private Asset assetSerial;

	@ManyToOne(optional = false)
	@JoinColumn(name = "r_productserialextension")
	private AssetSerialExtension assetSerialExtension;

	public Asset getProductSerial() {
		return assetSerial;
	}

	public void setProductSerial(Asset assetSerial) {
		this.assetSerial = assetSerial;
	}

	public AssetSerialExtension getAssetSerialExtension() {
		return assetSerialExtension;
	}

	public void setAssetSerialExtension(AssetSerialExtension assetSerialExtension) {
		this.assetSerialExtension = assetSerialExtension;
	}

	public String getExtensionValue() {
		return extensionValue;
	}

	public void setExtensionValue(String extensionValue) {
		this.extensionValue = extensionValue;
	}

}
