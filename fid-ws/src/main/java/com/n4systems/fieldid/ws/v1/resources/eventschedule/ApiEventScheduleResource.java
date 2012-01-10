package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

@Component
@Path("eventSchedule")
public class ApiEventScheduleResource extends ApiResource<ApiEventSchedule, EventSchedule> {

	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiEventSchedule> findAll(@QueryParam("assetId") String assetId) {
		QueryBuilder<EventSchedule> builder = createUserSecurityBuilder(EventSchedule.class);
		builder.addWhere(WhereClauseFactory.create("asset.mobileGUID", assetId));
		builder.addWhere(WhereClauseFactory.create("status", ScheduleStatus.SCHEDULED));
		builder.addOrder("nextDate");

		List<EventSchedule> schedules = persistenceService.findAll(builder);
		
		List<ApiEventSchedule> apiSchedules = convertAllEntitiesToApiModels(schedules);
		ListResponse<ApiEventSchedule> response = new ListResponse<ApiEventSchedule>(apiSchedules, 0, 0, schedules.size());
		return response;
	}

	@Override
	protected ApiEventSchedule convertEntityToApiModel(EventSchedule schedule) {
		ApiEventSchedule apiSchedule = new ApiEventSchedule();
		apiSchedule.setSid(schedule.getMobileGUID());
		apiSchedule.setActive(true);
		apiSchedule.setModified(schedule.getModified());
		apiSchedule.setOwnerId(schedule.getOwner().getId());
		apiSchedule.setAssetId(schedule.getAsset().getMobileGUID());
		apiSchedule.setEventTypeId(schedule.getEventType().getId());
		apiSchedule.setEventTypeName(schedule.getEventType().getName());
		apiSchedule.setNextDate(schedule.getNextDate());
		return apiSchedule;
	}

}
