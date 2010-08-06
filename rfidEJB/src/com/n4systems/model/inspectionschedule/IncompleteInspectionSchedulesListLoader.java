package com.n4systems.model.inspectionschedule;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class IncompleteInspectionSchedulesListLoader extends ListLoader<InspectionSchedule> {
	
	private Product product;
	private InspectionType inspectionType;

	public IncompleteInspectionSchedulesListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<InspectionSchedule> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<InspectionSchedule> query = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, filter);

		query.addSimpleWhere("product", product).addSimpleWhere("inspectionType", inspectionType);
		query.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		query.addOrder("nextDate");
		
		return query.getResultList(em);
	}

	public IncompleteInspectionSchedulesListLoader setProduct(Product product) {
		this.product = product;
		return this;
	}

	public IncompleteInspectionSchedulesListLoader setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}

}
