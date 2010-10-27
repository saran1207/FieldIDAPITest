package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.Asset;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;

@Entity
@Table(name = "assetextensionvalue")
public class AssetExtensionValue extends LegacyBaseEntity {
	private static final long serialVersionUID = 1L;

	private String extensionValue;

	@ManyToOne(optional = false)
	@JoinColumn(name = "r_productserial")
	private Asset asset;

	@ManyToOne(optional = false)
	@JoinColumn(name = "r_productserialextension")
	private AssetExtension assetExtension;

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public AssetExtension getAssetExtension() {
		return assetExtension;
	}

	public void setAssetExtension(AssetExtension assetExtension) {
		this.assetExtension = assetExtension;
	}

	public String getExtensionValue() {
		return extensionValue;
	}

	public void setExtensionValue(String extensionValue) {
		this.extensionValue = extensionValue;
	}

}
