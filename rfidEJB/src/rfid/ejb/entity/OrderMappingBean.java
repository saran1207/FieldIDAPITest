package rfid.ejb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.OrderKey;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;

@Entity
@Table(name = "ordermapping")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class OrderMappingBean extends LegacyBaseEntity {
	private static final long serialVersionUID = 1L;
	
	private String organizationID;
	private String externalSourceID;
	private String sourceOrderKey;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private OrderKey orderKey;

	public String getOrganizationID() {
		return organizationID;
	}

	public void setOrganizationID(String organizationID) {
		this.organizationID = organizationID;
	}

	public String getExternalSourceID() {
		return externalSourceID;
	}

	public void setExternalSourceID(String externalSourceID) {
		this.externalSourceID = externalSourceID;
	}

	public OrderKey getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(OrderKey orderKey) {
		this.orderKey = orderKey;
	}

	public String getSourceOrderKey() {
		return sourceOrderKey;
	}

	public void setSourceOrderKey(String sourceOrderKey) {
		this.sourceOrderKey = sourceOrderKey;
	}

}
