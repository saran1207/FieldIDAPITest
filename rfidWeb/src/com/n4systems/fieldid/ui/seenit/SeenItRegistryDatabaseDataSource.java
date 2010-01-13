package com.n4systems.fieldid.ui.seenit;

import java.util.HashMap;
import java.util.Map;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.ui.seenit.SeenItItem;
import com.n4systems.model.ui.seenit.SeenItStorageItem;

public class SeenItRegistryDatabaseDataSource implements SeenItRegistryDataSource {
	private final Long userId;
	
	public SeenItRegistryDatabaseDataSource(Long userId) {
		if (userId == null) {
			throw new InvalidArgumentException("You must give a unique user id");
		}
		this.userId = userId;
	}

	public Map<SeenItItem, Boolean> loadExisting() {
		SeenItStorageItem seenItStorageItem = load();
		
		return createItemList(seenItStorageItem);
	}

	private SeenItStorageItem load() {
		SeenItStorageItem seenItStorageItem = getLoader().setUserId(userId).load();
		
		return (seenItStorageItem != null) ? seenItStorageItem : new SeenItStorageItem(userId);
	}

	private Map<SeenItItem, Boolean> createItemList(SeenItStorageItem seenItStorageItem) {
		HashMap<SeenItItem, Boolean> itemSet = new HashMap<SeenItItem, Boolean>();
		
		for (SeenItItem userHasSeenItem : seenItStorageItem.getItemsSeen()) {
			itemSet.put(userHasSeenItem, true);
		}
		
		return itemSet;
	}

	public void updateItem(SeenItItem item, boolean seenIt) {
		SeenItStorageItem storageItemForUser = load();
		
		if (seenIt) {
			storageItemForUser.getItemsSeen().add(item);
		} else {
			storageItemForUser.getItemsSeen().remove(item);
		}
		
		getSaver().saveOrUpdate(storageItemForUser);
	}
	
	protected SeenItItemLoader getLoader() {
		return new SeenItItemLoader();
	}
	
	protected SeenItItemSaver getSaver() {
		return new SeenItItemSaver();
	}

}
