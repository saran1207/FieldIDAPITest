package com.n4systems.services.search;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.UnsecuredEntity;

import javax.persistence.*;

@Entity
@Table(name = "index_queue_items")
public class IndexQueueItem extends BaseEntity implements UnsecuredEntity {

    public enum IndexQueueItemType { ASSET_INSERT, ASSET_UPDATE, USER, ORG, ORDER, PREDEFINEDLOCATION, ASSETTYPE, ASSETTYPEGROUP, ASSETSTATUS, TENANT }

    @ManyToOne
    @JoinColumn(name="tenant_id")
    private Tenant tenant;

    @Enumerated(EnumType.STRING)
    @Column
    private IndexQueueItemType type;

    @Column(name="item_id")
    private Long itemId;

	public IndexQueueItemType getType() {
		return type;
	}

	public void setType(IndexQueueItemType type) {
		this.type = type;
	}

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
