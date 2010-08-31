package com.n4systems.persistence.utils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.Loader;

public class LazyLoadingList<T> extends AbstractList<T> {

	private final List<Long> inspectionIds; 
	private final IdLoader<? extends Loader<T>> loader; 
	private final Transaction transaction;

	

	public LazyLoadingList(List<Long> inspectionIds, IdLoader<? extends Loader<T>> loader, Transaction transaction) {
		super();
		this.inspectionIds = new ArrayList<Long>(inspectionIds);
		this.loader = loader;
		this.transaction = transaction;
	}

	@Override
	public T get(int index) {
		return loader.setId(inspectionIds.get(index)).load(transaction);
	}

	@Override
	public int size() {
		return inspectionIds.size();
	}

}
