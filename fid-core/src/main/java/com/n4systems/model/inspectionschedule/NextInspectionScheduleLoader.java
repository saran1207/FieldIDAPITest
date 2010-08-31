package com.n4systems.model.inspectionschedule;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

// This loader is a direct migration from InspectionScheduleManager.getNextScheduleFor(Long, Long)
public class NextInspectionScheduleLoader extends Loader<InspectionSchedule> {

	private Long productId;
	private Long typeId;
	
	public NextInspectionScheduleLoader() {}

	@Override
	protected InspectionSchedule load(EntityManager em) {
		InspectionSchedule schedule = null;
		
		QueryBuilder<InspectionSchedule> query = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, new OpenSecurityFilter());
		query.addSimpleWhere("product.id", productId).addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED).addSimpleWhere("inspectionType.id", typeId);
		query.addOrder("nextDate");
		
		List<InspectionSchedule> schedules = query.getResultList(em, 0, 1);
			
		if (!schedules.isEmpty()) {
			schedule = schedules.get(0);
		}

		return schedule;
	}

	public NextInspectionScheduleLoader setProductId(Long productId) {
		this.productId = productId;
		return this;
	}

	public NextInspectionScheduleLoader setTypeId(Long typeId) {
		this.typeId = typeId;
		return this;
	}

	public NextInspectionScheduleLoader setFieldsFromInspection(Inspection inspection) {
		setProductId(inspection.getProduct().getId());
		setTypeId(inspection.getType().getId());
		return this;
	}
}
