package rfid.ejb.entity;

import com.n4systems.model.parents.legacy.LegacyBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "infooption")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class InfoOptionBean extends LegacyBaseEntity implements Comparable<InfoOptionBean> {
	private static final long serialVersionUID = 1L;
	private static final Long DEFAULT_WEIGHT = 0L;
	
	@Column( nullable=false )
	private String name;

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
	}

	@Override
	public String toString() {
		String staticStr = (staticData) ? "static" : "dynamic";
		return String.format("InfoOption [%s = %s (%s)]", infoField.getName(), name, staticStr);
	}

	
}
