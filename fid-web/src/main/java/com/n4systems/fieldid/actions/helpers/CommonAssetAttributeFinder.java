package com.n4systems.fieldid.actions.helpers;

import java.util.List;
import java.util.SortedSet;

public interface CommonAssetAttributeFinder {
	public SortedSet<String> findAllCommonInfoFieldNames(List<Long> assetTypeIds);
}