package com.n4systems.fieldid.service.search;

import com.n4systems.model.EventSchedule;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.EventStatus;
import com.n4systems.model.search.IncludeDueDateRange;
import com.n4systems.model.user.User;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.terms.CompletedOrDueDateRange;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.persistence.search.terms.SimpleTerm;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ReportService extends SearchService<EventReportCriteria, EventSchedule> {

    public ReportService() {
        super(EventSchedule.class);
    }

    @Override
    protected void addSearchTerms(EventReportCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {
        User user = getCurrentUser();
        TimeZone timeZone = user.getTimeZone();

        addWildcardOrStringTerm(searchTerms, "asset.rfidNumber", criteriaModel.getRfidNumber());
        addWildcardOrStringTerm(searchTerms, "asset.identifier", criteriaModel.getIdentifier());
		if(isIntegrationEnabled()) {
			addWildcardOrStringTerm(searchTerms, "asset.shopOrder.order.orderNumber", criteriaModel.getOrderNumber());
		} else {
			addWildcardOrStringTerm(searchTerms, "asset.nonIntergrationOrderNumber", criteriaModel.getOrderNumber());
		}
        addWildcardOrStringTerm(searchTerms, "asset.purchaseOrder", criteriaModel.getPurchaseOrder());
        addWildcardOrStringTerm(searchTerms, "asset.customerRefNumber", criteriaModel.getReferenceNumber());
        addWildcardOrStringTerm(searchTerms, "advancedLocation.freeformLocation", criteriaModel.getLocation().getFreeformLocation());
        addSimpleTerm(searchTerms, "asset.type.id", getId(criteriaModel.getAssetType()));
        addSimpleTerm(searchTerms, "asset.type.group.id", getId(criteriaModel.getAssetTypeGroup()));

        addSimpleTerm(searchTerms, "event.assetStatus.id", getId(criteriaModel.getAssetStatus()));

        addSimpleTerm(searchTerms, "eventType.id", getId(criteriaModel.getEventType()));
        addSimpleTerm(searchTerms, "eventType.group.id", getId(criteriaModel.getEventTypeGroup()));
        addSimpleTerm(searchTerms, "event.performedBy.id", getId(criteriaModel.getPerformedBy()));
        addSimpleTerm(searchTerms, "project.id", getId(criteriaModel.getJob()));
        addSimpleTerm(searchTerms, "event.status", criteriaModel.getResult());

        if (criteriaModel.getEventStatus() == EventStatus.COMPLETE) {
            addSimpleTerm(searchTerms, "status", EventSchedule.ScheduleStatus.COMPLETED);
        } else if (criteriaModel.getEventStatus() == EventStatus.INCOMPLETE) {
            searchTerms.add(new SimpleTerm<EventSchedule.ScheduleStatus>("status", EventSchedule.ScheduleStatus.COMPLETED, WhereParameter.Comparator.NE));
        }

        if (IncludeDueDateRange.HAS_NO_DUE_DATE.equals(criteriaModel.getIncludeDueDateRange())) {
            addNullTerm(searchTerms, "nextDate");
        } else if (IncludeDueDateRange.HAS_A_DUE_DATE.equals(criteriaModel.getIncludeDueDateRange())) {
            addNotNullTerm(searchTerms,  "nextDate");
        } else if (IncludeDueDateRange.SELECT_DUE_DATE_RANGE.equals(criteriaModel.getIncludeDueDateRange()) || criteriaModel.getEventStatus() == EventStatus.INCOMPLETE) {
            if (criteriaModel.getDueDateRange() != null) {
                addDateRangeTerm(searchTerms, "nextDate", criteriaModel.getDueDateRange().calculateFromDate(), criteriaModel.getDueDateRange().calculateToDate());
            }
        }

        if (criteriaModel.getDateRange() != null) {
            if (criteriaModel.getEventStatus() == EventStatus.COMPLETE) {
                addDateRangeTerm(searchTerms, "event.schedule.date", DateHelper.convertToUTC(criteriaModel.getDateRange().calculateFromDate(), timeZone), DateHelper.convertToUTC(nextDay(criteriaModel.getDateRange().calculateToDate()), timeZone));
            } else if (criteriaModel.getEventStatus() == EventStatus.ALL) {
                searchTerms.add(new CompletedOrDueDateRange(timeZone, criteriaModel.getDateRange()));
            }
        }

        if(criteriaModel.getEventBook() != null && criteriaModel.getEventBook().getId() == 0) {
            addNullTerm(searchTerms, "event.book.id");
        } else {
            addSimpleTerm(searchTerms, "event.book.id", getId(criteriaModel.getEventBook()));
        }

        Long assignedUserId = getId(criteriaModel.getAssignedTo());
        if (assignedUserId != null) {
            addSimpleTerm(searchTerms, "event.assignedTo.assignmentApplyed", true);

            if(assignedUserId == 0) {
                addNullTerm(searchTerms, "event.assignedTo.assignedUser.id");
            } else {
                addSimpleTerm(searchTerms, "event.assignedTo.assignedUser.id", assignedUserId);
            }
        }

        addPredefinedLocationTerm(searchTerms, criteriaModel);
    }

	private void addPredefinedLocationTerm(List<SearchTermDefiner> search, EventReportCriteria criteriaModel) {
        Long predefLocationId = getId(criteriaModel.getLocation().getPredefinedLocation());
        if (predefLocationId != null) {
			search.add(new PredefinedLocationSearchTerm("preLocSearchId", predefLocationId));
		}
	}

    @Override
    protected void addJoinTerms(EventReportCriteria criteriaModel, List<JoinTerm> joinTerms) {
        Long predefLocationId = getId(criteriaModel.getLocation().getPredefinedLocation());
        if (predefLocationId != null) {
			addRequiredLeftJoin(joinTerms, "advancedLocation.predefinedLocation.searchIds", "preLocSearchId");
		}
    }

    private Date nextDay(Date date) {
        return date == null ? null : DateUtils.addDays(date, 1);
    }

}
