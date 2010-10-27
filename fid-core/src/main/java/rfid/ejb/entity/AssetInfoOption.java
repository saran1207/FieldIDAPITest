package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.parents.legacy.LegacyBaseEntity;


@Entity
@Table(name = "asset_infooption")
public class AssetInfoOption extends LegacyBaseEntity {
	private static final long serialVersionUID = 1L;

	private Long r_productserial;

	@ManyToOne(optional = false)
	@JoinColumn(name = "r_infooption")
	private InfoOptionBean infoOption;

	public Long getR_productserial() {
		return r_productserial;
	}

	public void setR_productserial(Long r_productserial) {
		this.r_productserial = r_productserial;
	}

	public InfoOptionBean getInfoOption() {
		return infoOption;
	}

	public void setInfoOption(InfoOptionBean infoOption) {
		this.infoOption = infoOption;
	}

}
