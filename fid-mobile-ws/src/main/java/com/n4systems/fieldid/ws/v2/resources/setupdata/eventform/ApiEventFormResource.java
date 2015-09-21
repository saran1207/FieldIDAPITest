package com.n4systems.fieldid.ws.v2.resources.setupdata.eventform;

import com.n4systems.fieldid.ws.v2.resources.ApiKeyString;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.fieldid.ws.v2.resources.setupdata.eventform.criteria.ApiCriteriaConverter;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("eventForm")
public class ApiEventFormResource extends SetupDataResourceReadOnly<ApiEventForm, EventForm> {

	@Autowired private ApiCriteriaConverter apiCriteriaConverter;

	public ApiEventFormResource() {
		super(EventForm.class, true);
	}

	@GET
	@Path("query/event")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> queryEvent(@QueryParam("eventId") List<ApiKeyString> eventIds) {
		QueryBuilder<ApiModelHeader> query = new QueryBuilder<>(EventForm.class, securityContext.getUserSecurityFilter(true));
		query.setSelectArgument(new NewObjectSelect(ApiModelHeader.class, "id", "modified").setDistinct(true));

		// Pulls only the EventForms for to the list of events
		query.addWhere(
				WhereClauseFactory.createExists("for_events",
						createUserSecurityBuilder(AbstractEvent.class)
								.setTableAlias("e")
								.addWhere(WhereClauseFactory.createNoVariable("eventForm", query.getFromArgument().getAlias()).setNoAliasRight(true))
								.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "mobileGUID", unwrapKeys(eventIds)))
				)
		);

		List<ApiModelHeader> results = persistenceService.findAll(query);
		return results;
	}

	@Override
	protected void addTermsToLatestQuery(QueryBuilder<?> query) {
		// When calling eventForm/query/latest we only want to return the forms for active EventTypes
		query.addWhere(
				WhereClauseFactory.createExists("event_type_exists",
						createUserSecurityBuilder(EventType.class)
							.setTableAlias("et")
							.addWhere(WhereClauseFactory.createNoVariable("eventForm", query.getFromArgument().getAlias()).setNoAliasRight(true))
				)
		);
	}

	@Override
	protected ApiEventForm convertEntityToApiModel(EventForm eventForm) {
		ApiEventForm apiEventForm = new ApiEventForm();
		apiEventForm.setSid(eventForm.getId());
		apiEventForm.setActive(eventForm.isActive());
		apiEventForm.setModified(eventForm.getModified());
		apiEventForm.setUseScoreForResult(eventForm.isUseScoreForResult());
		apiEventForm.setUseObservationCountForResult(eventForm.isUseObservationCountForResult());
		apiEventForm.getSections().addAll(eventForm.getSections().stream().map(this::convertCriteriaSection).collect(Collectors.toList()));

        //Set the Score pass/fail ranges.
        apiEventForm.setScoreCalculationType(eventForm.getScoreCalculationType().name());
        if(eventForm.getPassRange() != null) {
            apiEventForm.getScorePassRange().setComparator(eventForm.getPassRange().getComparator().name());
            apiEventForm.getScorePassRange().setValue1(eventForm.getPassRange().getValue1());
            apiEventForm.getScorePassRange().setValue2(eventForm.getPassRange().getValue2());
        }

        if(eventForm.getFailRange() != null) {
            apiEventForm.getScoreFailRange().setComparator(eventForm.getFailRange().getComparator().name());
            apiEventForm.getScoreFailRange().setValue1(eventForm.getFailRange().getValue1());
            apiEventForm.getScoreFailRange().setValue2(eventForm.getFailRange().getValue2());
        }

        //Set the Observation pass/fail ranges.
        apiEventForm.setObservationPassCalculationType(eventForm.getObservationcountPassCalculationType().name());
        if(eventForm.getObservationcountPassRange() != null) {
            apiEventForm.getObservationPassRange().setComparator(eventForm.getObservationcountPassRange().getComparator().name());
            apiEventForm.getObservationPassRange().setValue1(eventForm.getObservationcountPassRange().getValue1());
            apiEventForm.getObservationPassRange().setValue2(eventForm.getObservationcountPassRange().getValue2());
        }

        apiEventForm.setObservationFailCalcaulationType(eventForm.getObservationcountFailCalculationType().name());
        if(eventForm.getObservationcountFailRange() != null) {
            apiEventForm.getObservationFailRange().setComparator(eventForm.getObservationcountFailRange().getComparator().name());
            apiEventForm.getObservationFailRange().setValue1(eventForm.getObservationcountFailRange().getValue1());
            apiEventForm.getObservationFailRange().setValue2(eventForm.getObservationcountFailRange().getValue2());
        }

		return apiEventForm;
	}

	private ApiCriteriaSection convertCriteriaSection(CriteriaSection section) {
		ApiCriteriaSection apiSection = new ApiCriteriaSection();
		apiSection.setSid(section.getId());
		apiSection.setActive(!section.isRetired());
		apiSection.setModified(section.getModified());
		apiSection.setTitle(section.getTitle());
		apiSection.setOptional(section.isOptional());
		apiSection.getCriteria().addAll(section.getCriteria().stream().map(apiCriteriaConverter::convert).collect(Collectors.toList()));
		return apiSection;
	}

}
