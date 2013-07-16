package com.n4systems.services.search;

import com.n4systems.model.api.UnsecuredEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "index_queue_items")
public class IndexQueueItem implements Serializable, UnsecuredEntity {

	public enum IndexQueueItemType { ASSET_INSERT, ASSET_UPDATE, USER, ORG, ORDER, PREDEFINEDLOCATION, ASSETTYPE, ASSETTYPEGROUP, ASSETSTATUS, TENANT, EVENT_INSERT, EVENT_UPDATE };

	@EmbeddedId
	private IndexQueueItemPK item = new IndexQueueItemPK();

	public Long getId() {
		return item.id;
	}

	public void setId(Long id) {
		this.item.id = id;
	}

	public IndexQueueItemType getType() {
		return item.type;
	}

	public void setType(IndexQueueItemType type) {
		this.item.type = type;
	}

	@Embeddable
	public static class IndexQueueItemPK implements Serializable {
		private Long id;

		@Enumerated(EnumType.STRING)
		private IndexQueueItemType type;

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			IndexQueueItemPK that = (IndexQueueItemPK) o;
			if (!id.equals(that.id)) return false;
			if (type != that.type) return false;
			return true;
		}

		@Override
		public int hashCode() {
			return 31 * id.hashCode() + type.hashCode();
		}
	}

}
