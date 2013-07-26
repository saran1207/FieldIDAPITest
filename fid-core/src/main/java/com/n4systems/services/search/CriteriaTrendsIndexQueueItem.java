package com.n4systems.services.search;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.UnsecuredEntity;

import javax.persistence.*;

@Entity
@Table(name="criteria_trends_index_queue_items")
public class CriteriaTrendsIndexQueueItem extends BaseEntity implements UnsecuredEntity {

    public enum CriteriaTrendsIndexQueueItemType { TENANT, EVENT_INSERT, EVENT_UPDATE }

    @Column(name="item_id")
    private Long itemId;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private CriteriaTrendsIndexQueueItemType type;

    public CriteriaTrendsIndexQueueItemType getType() {
        return type;
    }

    public void setType(CriteriaTrendsIndexQueueItemType type) {
        this.type = type;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
