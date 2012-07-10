package com.n4systems.fieldid.service.search;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.EventState;
import com.n4systems.model.search.IncludeDueDateRange;
import com.n4systems.model.user.User;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.persistence.search.terms.SimpleTerm;
import com.n4systems.util.persistence.search.terms.completedordue.AssetStatusTerm;
import com.n4systems.util.persistence.search.terms.completedordue.AssignedUserTerm;
import com.n4systems.util.persistence.search.terms.completedordue.CompletedOrDueDateRange;
import com.n4systems.util.persistence.search.terms.completedordue.LocationTerm;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ReportService extends SearchService<EventReportCriteria, Event> {

    public ReportService() {
        super(Event.class);
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
        addSimpleTerm(searchTerms, "asset.type.id", getId(criteriaModel.getAssetType()));
        addSimpleTerm(searchTerms, "asset.type.group.id", getId(criteriaModel.getAssetTypeGroup()));

        if (criteriaModel.getAssetStatus() != null) {
            addAssetStatusTerm(criteriaModel, searchTerms);
        }

        addSimpleTerm(searchTerms, "type.id", getId(criteriaModel.getEventType()));
        addSimpleTerm(searchTerms, "type.group.id", getId(criteriaModel.getEventTypeGroup()));
        addSimpleTerm(searchTerms, "performedBy.id", getId(criteriaModel.getPerformedBy()));
        addSimpleTerm(searchTerms, "project.id", getId(criteriaModel.getJob()));
        addSimpleTerm(searchTerms, "status", criteriaModel.getResult());

        if (criteriaModel.getEventState() == EventState.COMPLETE) {
            addSimpleTerm(searchTerms, "eventState", Event.EventState.COMPLETED);
        } else if (criteriaModel.getEventState() == EventState.INCOMPLETE) {
            searchTerms.add(new SimpleTerm<Event.EventState>("eventState", Event.EventState.COMPLETED, WhereParameter.Comparator.NE));
        }

        if (IncludeDueDateRange.HAS_NO_DUE_DATE.equals(criteriaModel.getIncludeDueDateRange())) {
            addNullTerm(searchTerms, "nextDate");
        } else if (IncludeDueDateRange.HAS_A_DUE_DATE.equals(criteriaModel.getIncludeDueDateRange())) {
            addNotNullTerm(searchTerms,  "nextDate");
        } else if (criteriaModel.getEventState() == EventState.INCOMPLETE || IncludeDueDateRange.SELECT_DUE_DATE_RANGE.equals(criteriaModel.getIncludeDueDateRange())) {
            if (criteriaModel.getDueDateRange() != null && !criteriaModel.getDueDateRange().isEmptyCustom()) {
                addDateRangeTerm(searchTerms, "nextDate", criteriaModel.getDueDateRange().calculateFromDate(), nextDay(criteriaModel.getDueDateRange().calculateToDate()));
            }
        }

        if (criteriaModel.getDateRange() != null && !criteriaModel.getDateRange().isEmptyCustom()) {
            if (criteriaModel.getEventState() == EventState.COMPLETE) {
                addDateRangeTerm(searchTerms, "completedDate", DateHelper.convertToUTC(criteriaModel.getDateRange().calculateFromDate(), timeZone), DateHelper.convertToUTC(nextDay(criteriaModel.getDateRange().calculateToDate()), timeZone));
            } else if (criteriaModel.getEventState() == EventState.ALL) {
                searchTerms.add(new CompletedOrDueDateRange(timeZone, criteriaModel.getDateRange()));
            }
        }

        if(criteriaModel.getEventBook() != null && criteriaModel.getEventBook().getId() == 0) {
            addNullTerm(searchTerms, "book.id");
        } else {
            addSimpleTerm(searchTerms, "book.id", getId(criteriaModel.getEventBook()));
        }

        Long assignedUserId = getId(criteriaModel.getAssignedTo());
        if (assignedUserId != null) {
            addAssignedUserTerm(assignedUserId, criteriaModel, searchTerms);
        }

        if (!criteriaModel.getLocation().isBlank()) {
            searchTerms.add(new LocationTerm(criteriaModel.getEventState(), getId(criteriaModel.getLocation().getPredefinedLocation()), criteriaModel.getLocation().getFreeformLocation()));
        }

    }

    private void addAssignedUserTerm(Long assignedUserId, EventReportCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {
        searchTerms.add(new AssignedUserTerm(criteriaModel.getEventState(), assignedUserId));
    }

    private void addAssetStatusTerm(EventReportCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {
        searchTerms.add(new AssetStatusTerm(criteriaModel.getEventState(), criteriaModel.getAssetStatus()));
    }

    @Override
    protected void addJoinTerms(EventReportCriteria criteriaModel, List<JoinTerm> joinTerms) {
        Long predefLocationId = getId(criteriaModel.getLocation().getPredefinedLocation());

        EventState status = criteriaModel.getEventState();

        if (predefLocationId != null && status.includesIncomplete()) {
            JoinTerm joinTerm = new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER, "asset.advancedLocation.predefinedLocation.searchIds", "assetPreLocSearchId", true);
            joinTerms.add(joinTerm);
		}

        if (predefLocationId != null && status.includesComplete()) {
            JoinTerm joinTerm = new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER, "event.advancedLocation.predefinedLocation.searchIds", "eventPreLocSearchId", true);
            joinTerms.add(joinTerm);
        }

        // TODO WEB 2926 : implement searching for resolved events.

        if (criteriaModel.sortingByDivision()) {
            addOrgJoinTerms(status, "divisionOrg", "assetDivisionOrg", "eventDivisionOrg", joinTerms);
        }

        if (criteriaModel.sortingByOrganization()) {
            addOrgJoinTerms(status, "secondaryOrg", "assetSecondaryOrg", "eventSecondaryOrg", joinTerms);
        }

        if (criteriaModel.sortingByCustomer()) {
            addOrgJoinTerms(status, "customerOrg", "assetCustomerOrg", "eventCustomerOrg", joinTerms);
        }
    }

    private void addOrgJoinTerms(EventState status, String basePath, String assetJoinAlias, String eventJoinAlias, List<JoinTerm> joinTerms) {
        if (status.includesIncomplete()) {
            JoinTerm joinTerm = new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER, "asset.owner." + basePath, assetJoinAlias, true);
            joinTerms.add(joinTerm);
        }

//        if (status.includesComplete()) {
//            JoinTerm joinTerm = new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER, "event.owner." + basePath, eventJoinAlias, true);
//            joinTerms.add(joinTerm);
//        }
    }

    private Date nextDay(Date date) {
        return date == null ? null : DateUtils.addDays(date, 1);
    }

    @Override
    protected void addSortTerms(EventReportCriteria criteriaModel, QueryBuilder<?> searchBuilder, ColumnMappingView sortColumn, SortDirection sortDirection) {
        // Reporting has a few special case sorts that are more complicated due to displaying two types of data. ie sorting on two columns.
        // To prevent weird mixing, when one of these special cases occurs, we always sort by state first
        if (criteriaModel.sortingByLocation()) {
            addStatusSortIfNecessary(criteriaModel, searchBuilder, sortDirection);

            if (criteriaModel.getEventState().includesComplete()) {
                SortTerm sortTerm = new SortTerm("outer_event.advancedLocation.predefinedLocation.id", sortDirection);
                sortTerm.setAlwaysDropAlias(true);

                SortTerm sortTerm2 = new SortTerm("outer_event.advancedLocation.freeformLocation", sortDirection);
                sortTerm2.setAlwaysDropAlias(true);

                searchBuilder.getOrderArguments().add(sortTerm.toSortField());
                searchBuilder.getOrderArguments().add(sortTerm2.toSortField());
            }

            if (criteriaModel.getEventState().includesIncomplete()) {
                searchBuilder.addOrder("asset.advancedLocation.predefinedLocation.id", sortDirection.isAscending());
                searchBuilder.addOrder("asset.advancedLocation.freeformLocation", sortDirection.isAscending());
            }

        } else if (criteriaModel.sortingByDivision()) {
            addOrgSort(criteriaModel, searchBuilder, sortDirection, "assetDivisionOrg", "eventDivisionOrg");
        } else if (criteriaModel.sortingByOrganization()) {
            addOrgSort(criteriaModel, searchBuilder, sortDirection, "assetSecondaryOrg", "eventSecondaryOrg");
        } else if (criteriaModel.sortingByCustomer()) {
            addOrgSort(criteriaModel, searchBuilder, sortDirection, "assetCustomerOrg", "eventCustomerOrg");
        } else {
            // This is a standard sort
            super.addSortTerms(criteriaModel, searchBuilder, sortColumn, sortDirection);
        }
    }

    private void addOrgSort(EventReportCriteria criteriaModel, QueryBuilder<?> searchBuilder, SortDirection sortDirection, String assetOrgAlias, String eventOrgAlias) {
        addStatusSortIfNecessary(criteriaModel, searchBuilder, sortDirection);

        if (criteriaModel.getEventState().includesComplete()) {
            SortTerm sortTerm = new SortTerm(eventOrgAlias+".name", sortDirection);
            sortTerm.setAlwaysDropAlias(true);
            searchBuilder.getOrderArguments().add(sortTerm.toSortField());
        }

        if (criteriaModel.getEventState().includesIncomplete()) {
            SortTerm sortTerm = new SortTerm(assetOrgAlias+".name", sortDirection);
            sortTerm.setAlwaysDropAlias(true);
            searchBuilder.getOrderArguments().add(sortTerm.toSortField());
        }

    }

    // Because the column we are sorting by is actually two columns (one for completed events and one for incomplete events)
    // we decided to sort by eventState first so there's less unexpected weirdness mixing complete/incomplete.
    private void addStatusSortIfNecessary(EventReportCriteria criteriaModel, QueryBuilder<?> searchBuilder, SortDirection sortDirection) {
        if (criteriaModel.getEventState() == EventState.ALL) {
            SortTerm sortTerm = new SortTerm("eventState", sortDirection);
            searchBuilder.getOrderArguments().add(sortTerm.toSortField());
        }
    }

}
