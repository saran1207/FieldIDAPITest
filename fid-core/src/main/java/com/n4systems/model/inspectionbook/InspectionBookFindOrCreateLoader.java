package com.n4systems.model.inspectionbook;

import javax.persistence.EntityManager;

import com.n4systems.model.InspectionBook;
import com.n4systems.model.security.SecurityFilter;

public class InspectionBookFindOrCreateLoader extends InspectionBookByNameLoader {
	private final InspectionBookSaver saver;
	
	public InspectionBookFindOrCreateLoader(SecurityFilter filter) {
		this(filter, new InspectionBookSaver());
	}
	
	public InspectionBookFindOrCreateLoader(SecurityFilter filter, InspectionBookSaver saver) {
		super(filter);
		this.saver = saver;
	}

	@Override
	public InspectionBook load(EntityManager em, SecurityFilter filter) {
		InspectionBook book = createIfNull(super.load(em, filter));
		return book;
	}

	private InspectionBook createIfNull(InspectionBook book) {
		if (book == null) {
			book = new InspectionBook();
			book.setName(getName());
			book.setOpen(true);
			book.setOwner(getOwner());
			book.setTenant(getOwner().getTenant());
			
			saver.save(book);
		}
		return book;
	}

}
