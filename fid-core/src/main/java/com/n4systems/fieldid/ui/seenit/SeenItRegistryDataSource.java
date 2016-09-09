package com.n4systems.fieldid.ui.seenit;

import com.n4systems.model.ui.seenit.SeenItItem;

import java.util.Map;

public interface SeenItRegistryDataSource {

	public Map<SeenItItem, Boolean>loadExisting();

	public void updateItem(SeenItItem item, boolean seenIt);
}
