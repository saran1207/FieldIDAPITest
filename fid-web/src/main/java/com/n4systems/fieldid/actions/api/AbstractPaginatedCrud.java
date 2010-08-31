package com.n4systems.fieldid.actions.api;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.tools.Pager;

public abstract  class AbstractPaginatedCrud<T extends AbstractEntity> extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	
	protected Pager<T> page;
	private Integer currentPage; 

	public AbstractPaginatedCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	abstract protected void initMemberFields();

	abstract protected void loadMemberFields(Long uniqueId);

	public Integer getCurrentPage() {
		if (currentPage == null) {
			currentPage = 1;
		}
		return currentPage;
	}

	public void setCurrentPage(Integer pageNumber) {
		this.currentPage = pageNumber;
	}

	public Pager<T> getPage() {
		return page;
	}

}
