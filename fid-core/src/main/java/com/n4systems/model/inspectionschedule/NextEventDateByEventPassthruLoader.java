package com.n4systems.model.inspectionschedule;

import java.util.Date;

import com.n4systems.persistence.Transaction;


public class NextEventDateByEventPassthruLoader extends NextEventDateByEventLoader {
	private Date nextDate;
	
	public NextEventDateByEventPassthruLoader() {
		super(null);
	}

	public NextEventDateByEventPassthruLoader(Date nextDate) {
		super(null);
		this.nextDate = nextDate;
	}

	
	@Override
	public Date load() {
		return nextDate;
	}

	@Override
	public Date load(Transaction transaction) {
		return load();
	}

	public NextEventDateByEventPassthruLoader setNextDate(Date nextDate) {
		this.nextDate = nextDate;
		return this;
	}

}
