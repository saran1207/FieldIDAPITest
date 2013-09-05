package com.n4systems.services.search;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.UnsecuredEntity;

import javax.persistence.*;

@Entity
@Table(name="event_index_queue_items")
public class EventIndexQueueItem extends BaseEntity implements UnsecuredEntity {

    public enum EventIndexQueueItemType { TENANT, EVENT_INSERT, EVENT_UPDATE }

    @Column(name="item_id")
    private Long itemId;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private EventIndexQueueItemType type;

    public EventIndexQueueItemType getType() {
        return type;
    }

    public void setType(EventIndexQueueItemType type) {
        this.type = type;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

}
