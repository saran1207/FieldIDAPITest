package com.n4systems.model.inspectionschedule;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

// This loader is a direct migration from InspectionScheduleManager.getNextScheduleFor(Long, Long)
public class NextInspectionScheduleLoader extends Loader<EventSchedule> {

	private Long assetId;
	private Long typeId;
	
	public NextInspectionScheduleLoader() {}

	@Override
	protected EventSchedule load(EntityManager em) {
		EventSchedule schedule = null;
		
		QueryBuilder<EventSchedule> query = new QueryBuilder<EventSchedule>(EventSchedule.class, new OpenSecurityFilter());
		query.addSimpleWhere("asset.id", assetId).addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED).addSimpleWhere("inspectionType.id", typeId);
		query.addOrder("nextDate");
		
		List<EventSchedule> schedules = query.getResultList(em, 0, 1);
			
		if (!schedules.isEmpty()) {
			schedule = schedules.get(0);
		}

		return schedule;
	}

	public NextInspectionScheduleLoader setAssetId(Long assetId) {
		this.assetId = assetId;
		return this;
	}

	public NextInspectionScheduleLoader setTypeId(Long typeId) {
		this.typeId = typeId;
		return this;
	}

	public NextInspectionScheduleLoader setFieldsFromInspection(Event event) {
		setAssetId(event.getAsset().getId());
		setTypeId(event.getType().getId());
		return this;
	}
}
