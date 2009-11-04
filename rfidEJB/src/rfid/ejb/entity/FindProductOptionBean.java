package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.parents.legacy.LegacyBaseEntity;

@Entity
@Table(name = "findproductoption")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class FindProductOptionBean extends LegacyBaseEntity {
	private static final long serialVersionUID = 1L;
	
	private String identifier;
	private String value;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKey() {
		return getIdentifier();
	}

	public void setKey(String key) {
		setIdentifier(key);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	
}
