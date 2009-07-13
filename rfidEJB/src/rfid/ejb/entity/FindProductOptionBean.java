package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.parents.legacy.LegacyBaseEntity;

@Entity
@Table(name = "findproductoption")
public class FindProductOptionBean extends LegacyBaseEntity {
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String value;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
