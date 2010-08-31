package com.n4systems.model.inspectionschedule;

import java.util.Date;

import com.n4systems.persistence.Transaction;


public class NextInspectionDateByInspectionPassthruLoader extends NextInspectionDateByInspectionLoader {
	private Date nextDate;
	
	public NextInspectionDateByInspectionPassthruLoader() {
		super(null);
	}

	public NextInspectionDateByInspectionPassthruLoader(Date nextDate) {
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

	public NextInspectionDateByInspectionPassthruLoader setNextDate(Date nextDate) {
		this.nextDate = nextDate;
		return this;
	}

}
