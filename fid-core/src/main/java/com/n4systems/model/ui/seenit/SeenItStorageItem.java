package com.n4systems.model.ui.seenit;

import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.parents.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
//Swapped for OrderColumn due to deprecation.  If this is somehow broken by this change... you know what to do.
//import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name="seenitstorageitem")
@PrimaryKeyJoinColumn(name="id")
public class SeenItStorageItem extends AbstractEntity implements UnsecuredEntity {
	
	@Column(nullable=false)
	private Long userId;
	
    @Column(name="element")
    @ElementCollection(fetch=FetchType.EAGER)
    @JoinTable(name="seenitstorageitem_itemsseen", joinColumns = @JoinColumn(name="seenitstorageitem_id"))
	@Enumerated(EnumType.STRING)
    @OrderColumn(name="id")
	private Set<SeenItItem> itemsSeen = new HashSet<>();
	
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
