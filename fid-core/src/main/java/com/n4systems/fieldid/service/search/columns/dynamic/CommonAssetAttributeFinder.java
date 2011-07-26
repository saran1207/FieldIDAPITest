package com.n4systems.fieldid.service.search.columns.dynamic;

import java.util.List;
import java.util.SortedSet;

public interface CommonAssetAttributeFinder {
	public SortedSet<String> findAllCommonInfoFieldNames(List<Long> assetTypeIds);
}