package rfid.ejb.entity;

import com.n4systems.model.parents.legacy.LegacyBaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table (name = "assetcodemapping_infooption")
public class AssetCodeMappingInfoOption extends LegacyBaseEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne 
	@JoinColumn (name="r_productCodeMapping")
	private AssetCodeMapping assetCodeMapping;
	
	@ManyToOne
	@JoinColumn (name="r_infoOption")
	private InfoOptionBean infoOption;

	public AssetCodeMapping getAssetCodeMapping() {
		return assetCodeMapping;
	}

	public void setAssetCodeMapping(AssetCodeMapping assetCodeMapping) {
		this.assetCodeMapping = assetCodeMapping;
	}

	public InfoOptionBean getInfoOption() {
		return infoOption;
	}

	public void setInfoOption(InfoOptionBean infoOption) {
		this.infoOption = infoOption;
	}
	
	
	
	
}
