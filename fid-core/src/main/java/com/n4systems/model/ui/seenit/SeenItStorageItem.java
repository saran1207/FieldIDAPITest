package com.n4systems.model.ui.seenit;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.parents.AbstractEntity;

@Entity
@Table(name="seenitstorageitem")
public class SeenItStorageItem extends AbstractEntity implements UnsecuredEntity {
	
	@Column(nullable=false)
	private Long userId;
	
	//TODO Make sure we don't need additional annotations here... I think we might.
	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Set<SeenItItem> itemsSeen = new HashSet<SeenItItem>();
	
	public SeenItStorageItem() {
		super();
	}

	public SeenItStorageItem(Long userId) {
		super();
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Set<SeenItItem> getItemsSeen() {
		return itemsSeen;
	}

	public void setItemsSeen(Set<SeenItItem> itemsSeen) {
		this.itemsSeen = itemsSeen;
	}

}
