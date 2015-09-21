package com.n4systems.fieldid.ws.v2.resources.customerdata.eventhistory;

import com.n4systems.fieldid.ws.v2.resources.ApiKeyString;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.fieldid.ws.v2.resources.ApiSortedModelHeader;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("eventHistory")
public class ApiEventHistoryResource extends ApiResource<ApiEventHistory, ThingEvent> {

	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> queryById(@QueryParam("id") List<ApiKeyString> eventIds) {
		if (eventIds.isEmpty()) return new ArrayList<>();

		List<ApiModelHeader> headers = persistenceService.findAll(
				createModelHeaderQueryBuilder(ThingEvent.class, "mobileGUID", "modified")
						.addWhere(WhereClauseFactory.create("workflowState", WorkflowState.COMPLETED))
						.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "mobileGUID", unwrapKeys(eventIds)))
		);
		return headers;
	}

	@GET
	@Path("query/asset")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiSortedModelHeader> queryAsset(@QueryParam("assetId") List<ApiKeyString> assetIds) {
		if (assetIds.isEmpty()) return new ArrayList<>();

		List<ApiSortedModelHeader> headers = persistenceService.findAll(
				createModelHeaderQueryBuilder(ThingEvent.class, "mobileGUID", "modified", "completedDate", false)
						.addWhere(WhereClauseFactory.create("workflowState", WorkflowState.COMPLETED))
						.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "asset.mobileGUID", unwrapKeys(assetIds)))
		);
		return headers;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiEventHistory> findAll(@QueryParam("id") List<ApiKeyString> eventIds) {
		if (eventIds.isEmpty()) return new ArrayList<>();

		QueryBuilder<ThingEvent> queryBuilder = createUserSecurityBuilder(ThingEvent.class);
		queryBuilder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "mobileGUID", unwrapKeys(eventIds)));
		List<ApiEventHistory> apiAttachment = convertAllEntitiesToApiModels(persistenceService.findAll(queryBuilder));
		return apiAttachment;
	}
	
	@Override
	protected ApiEventHistory convertEntityToApiModel(ThingEvent event) {
		ApiEventHistory apiEventHistory = new ApiEventHistory();
		apiEventHistory.setAssetId(event.getAsset().getMobileGUID());
		apiEventHistory.setAssetTypeId(event.getType().getId());
		apiEventHistory.setEventTypeName(event.getType().getName());
		apiEventHistory.setEventDate(event.getDate());
		apiEventHistory.setEventId(event.getMobileGUID());
		apiEventHistory.setEventTypeId(event.getType().getId());
		apiEventHistory.setPerformedBy(event.getPerformedBy().getDisplayName());
		apiEventHistory.setStatus(event.getEventResult().getDisplayName());
		apiEventHistory.setPrintable(event.isPrintable());
		return apiEventHistory;
	}
}
