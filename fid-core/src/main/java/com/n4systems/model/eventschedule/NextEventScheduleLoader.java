package com.n4systems.model.eventschedule;

import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

import javax.persistence.EntityManager;
import java.util.List;

// This loader is a direct migration from EventScheduleManager.getNextScheduleFor(Long, Long)
public class NextEventScheduleLoader extends Loader<ThingEvent> {

	private Long assetId;
	private Long typeId;
	
	public NextEventScheduleLoader() {}

	@Override
	public ThingEvent load(EntityManager em) {
		ThingEvent schedule = null;
		
		QueryBuilder<ThingEvent> query = new QueryBuilder<ThingEvent>(ThingEvent.class, new OpenSecurityFilter());
		query.addSimpleWhere("asset.id", assetId).addWhere(Comparator.EQ, "workflowState", "workflowState", WorkflowState.OPEN);
		if (typeId != null)
			query.addSimpleWhere("type.id", typeId);
		query.addOrder("dueDate");
		
		List<ThingEvent> schedules = query.getResultList(em, 0, 1);
			
		if (!schedules.isEmpty()) {
			schedule = schedules.get(0);
		}

		return schedule;
	}

	public NextEventScheduleLoader setAssetId(Long assetId) {
		this.assetId = assetId;
		return this;
	}

	public NextEventScheduleLoader setTypeId(Long typeId) {
		this.typeId = typeId;
		return this;
	}

	public NextEventScheduleLoader setFieldsFromEvent(ThingEvent event) {
		setAssetId(event.getAsset().getId());
		setTypeId(event.getType().getId());
		return this;
	}
}
