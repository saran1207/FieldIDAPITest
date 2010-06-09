package com.n4systems.fieldid.ui;

import java.util.List;

public class OptionLists {

	public static <T> void includeInList(List<T> existingList, T elementToEnsureIsInList) {
		if (elementToEnsureIsInList != null && !existingList.contains(elementToEnsureIsInList)) {
			existingList.add(elementToEnsureIsInList);
		}
	}

}
