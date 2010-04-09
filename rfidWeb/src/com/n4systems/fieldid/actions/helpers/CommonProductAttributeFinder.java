package com.n4systems.fieldid.actions.helpers;

import java.util.List;
import java.util.SortedSet;

public interface CommonProductAttributeFinder {
	public SortedSet<String> findAllCommonInfoFieldNames(List<Long> productTypeIds);
}