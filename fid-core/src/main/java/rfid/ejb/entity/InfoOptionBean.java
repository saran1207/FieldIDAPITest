package rfid.ejb.entity;

import com.n4systems.model.parents.legacy.LegacyBaseEntity;
import com.n4systems.persistence.localization.Localized;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "infooption")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InfoOptionBean extends LegacyBaseEntity implements Comparable<InfoOptionBean> {
	private static final long serialVersionUID = 1L;
	private static final Long DEFAULT_WEIGHT = 0L;
	
	public static InfoOptionBean createBlankInfoOption(InfoFieldBean infoField) {
		InfoOptionBean result = new InfoOptionBean();
		result.setInfoField(infoField);
		result.setName("");
		result.setUniqueID(0L);
		return result;
	}

	@Column( nullable=false )
	private @Localized String name;

	private Long weight = DEFAULT_WEIGHT;
	private boolean staticData = false;

	@ManyToOne(optional = false)
	@JoinColumn(name = "r_infofield")
	private InfoFieldBean infoField;
	
	@PreUpdate
	@PrePersist
	protected void presave() {
		if (weight == null) {
			weight = DEFAULT_WEIGHT;
		}
	}
	
	public InfoFieldBean getInfoField() {
		return infoField;
	}

	public void setInfoField(InfoFieldBean infoField) {
		this.infoField = infoField;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStaticData() {
		return staticData;
	}

	public void setStaticData(boolean staticData) {
		this.staticData = staticData;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public int compareTo(InfoOptionBean other) {
		try {
		if (0 == getInfoField().compareTo(other.getInfoField())) {
			if (getWeight() == null) {
				setWeight(0L);
			}
			if (getWeight().equals(other.getWeight())) {
				return getName().compareTo(other.getName());
			} else {
				return getWeight().compareTo(other.getWeight());
			}
		} else {
			return getInfoField().compareTo(other.getInfoField());
		}
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public String toString() {
		String staticStr = (staticData) ? "static" : "dynamic";
		return String.format("InfoOption [%s = %s (%s)]", infoField.getName(), name, staticStr);
	}

	
}
