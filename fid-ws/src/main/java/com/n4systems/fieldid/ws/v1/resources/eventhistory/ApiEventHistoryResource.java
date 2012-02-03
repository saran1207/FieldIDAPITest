package com.n4systems.fieldid.ws.v1.resources.eventhistory;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Event;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

@Component
@Path("eventHistory")
public class ApiEventHistoryResource extends ApiResource<ApiEventHistory, Event> {

	//This method is obsolete, we can remove it after findAllEventHistory is wired both in web service and client.
	@GET
	@Path("{assetId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiEventHistory> findAll(
			@PathParam("assetId") String assetId,
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("50") @QueryParam("pageSize") int pageSize) {
	
	QueryBuilder<Event> builder = createUserSecurityBuilder(Event.class);
	builder.addWhere(WhereClauseFactory.create("asset.mobileGUID", assetId));
	builder.addOrder("date", false);

	List<Event> events = persistenceService.findAll(builder, page, pageSize);
	Long total = persistenceService.count(builder);
	
	List<ApiEventHistory> apiEventHistory = convertAllEntitiesToApiModels(events);
	ListResponse<ApiEventHistory> response = new ListResponse<ApiEventHistory>(apiEventHistory, page, pageSize, total);
	return response;
		
	}
	
	public List<ApiEventHistory> findAllEventHistory(String assetId) {
		QueryBuilder<Event> builder = createUserSecurityBuilder(Event.class);
		builder.addWhere(WhereClauseFactory.create("asset.mobileGUID", assetId));
		builder.addOrder("date", false);

		List<Event> events = persistenceService.findAll(builder);
		List<ApiEventHistory> apiEventHistory = convertAllEntitiesToApiModels(events);
		return apiEventHistory;
	}
	
	@Override
	protected ApiEventHistory convertEntityToApiModel(Event event) {
		ApiEventHistory apiEventHistory = new ApiEventHistory();
		apiEventHistory.setAssetId(event.getAsset().getMobileGUID());
		apiEventHistory.setAssetTypeId(event.getType().getId());
		apiEventHistory.setEventTypeName(event.getType().getName());
		apiEventHistory.setEventDate(event.getDate());
		apiEventHistory.setEventId(event.getMobileGUID());
		apiEventHistory.setPerformedBy(event.getPerformedBy().getDisplayName());
		apiEventHistory.setStatus(event.getStatus().getDisplayName());
		return apiEventHistory;
	}
}
