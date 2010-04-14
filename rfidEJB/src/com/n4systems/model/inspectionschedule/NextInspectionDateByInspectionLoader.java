package com.n4systems.model.inspectionschedule;

import java.util.Date;

import javax.persistence.EntityManager;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.MinSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class NextInspectionDateByInspectionLoader extends SecurityFilteredLoader<Date> {
	private Inspection inspection;
	
	public NextInspectionDateByInspectionLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Date load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Date> builder = new QueryBuilder<Date>(InspectionSchedule.class, filter);
		builder.setSelectArgument(new MinSelect("nextDate"));
		builder.addWhere(WhereClauseFactory.create("product.id", inspection.getProduct().getId()));
		builder.addWhere(WhereClauseFactory.create("inspectionType.id", inspection.getType().getId()));
		builder.addWhere(WhereClauseFactory.create(Comparator.NE, "status", ScheduleStatus.COMPLETED));

		Date nextDate = builder.getSingleResult(em);
		return nextDate;
	}

	public NextInspectionDateByInspectionLoader setInspection(Inspection inspection) {
		this.inspection = inspection;
		return this;
	}
}
