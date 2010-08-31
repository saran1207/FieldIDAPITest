package com.n4systems.ejb;

import java.util.List;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.persistence.search.BaseSearchDefiner;
import com.n4systems.util.persistence.search.SearchDefiner;

public class SearchPerformerWithReadOnlyTransactionManagement implements SearchPerformer {

	
	

	public int countPages(BaseSearchDefiner definer, SecurityFilter filter, long pageSize) {
		Transaction t = com.n4systems.persistence.PersistenceManager.startTransaction();
		int countPages;
		
		try {
			com.n4systems.persistence.PersistenceManager.setSessionReadOnly(t.getEntityManager());
			SearchPerformer delegateSearchPerformer = new PerformSearchRequiringTransaction(t.getEntityManager());
			
			countPages = delegateSearchPerformer.countPages(definer, filter, pageSize);
			
		} finally {
			t.commit();
		}
		return countPages;
	}

	public List<Long> idSearch(BaseSearchDefiner definer, SecurityFilter filter) {
		Transaction t = com.n4systems.persistence.PersistenceManager.startTransaction();
		List<Long> idSearch;
		try {
			com.n4systems.persistence.PersistenceManager.setSessionReadOnly(t.getEntityManager());
			SearchPerformer delegateSearchPerformer = new PerformSearchRequiringTransaction(t.getEntityManager());
			
			idSearch = delegateSearchPerformer.idSearch(definer, filter);
		} finally {
			t.commit();
		}
		return idSearch;
	}

	public <K> PageHolder<K> search(SearchDefiner<K> definer, SecurityFilter filter) {
		Transaction t = com.n4systems.persistence.PersistenceManager.startTransaction();
		PageHolder<K> performSearch;
		try {
			com.n4systems.persistence.PersistenceManager.setSessionReadOnly(t.getEntityManager());
			SearchPerformer delegateSearchPerformer = new PerformSearchRequiringTransaction(t.getEntityManager());
			
			performSearch = delegateSearchPerformer.search(definer, filter);
		} finally {
			t.commit();
		}
		
		return performSearch;
	}
	
	

}
