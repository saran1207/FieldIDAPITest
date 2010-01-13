package com.n4systems.fieldid.ui.seenit;

import java.util.Map;

import com.n4systems.model.ui.seenit.SeenItItem;

public interface SeenItRegistryDataSource {

	public Map<SeenItItem, Boolean>loadExisting();

	public void updateItem(SeenItItem item, boolean seenIt);
}
