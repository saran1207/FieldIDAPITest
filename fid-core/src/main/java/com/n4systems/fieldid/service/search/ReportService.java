package com.n4systems.fieldid.service.search;

import com.google.common.collect.Lists;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.event.MostRecentThingEvent;
import com.n4systems.model.search.*;
import com.n4systems.model.security.NetworkIdSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.date.DateService;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.terms.GpsBoundsTerm;
import com.n4systems.util.persistence.search.terms.HasGpsTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.persistence.search.terms.completedordue.AssetStatusTerm;
import com.n4systems.util.persistence.search.terms.completedordue.AssignedUserTerm;
import com.n4systems.util.persistence.search.terms.completedordue.CompletedOrDueDateRange;
import com.n4systems.util.persistence.search.terms.completedordue.LocationTerm;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ReportService extends SearchService<EventReportCriteria, ThingEvent, ThingEvent> {

    @Autowired
    private DateService dateService;

    public ReportService() {
        super(ThingEvent.class);
    }

    @Override
    protected void addSearchTerms(EventReportCriteria criteriaModel, List<SearchTermDefiner> searchTerms, boolean includeGps) {
        User user = getCurrentUser();
        TimeZone timeZone = user.getTimeZone();

        String prefix = criteriaModel.isShowMostRecentEventsOnly() ? "event." : "";

        addSimpleTerm(searchTerms, "type.actionType", criteriaModel.getEventSearchType().equals(EventSearchType.ACTIONS));

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
        addSimpleTerm(searchTerms, prefix + "performedBy.id", getId(criteriaModel.getPerformedBy()));
        addSimpleTerm(searchTerms, prefix + "project.id", getId(criteriaModel.getJob()));
        addSimpleTerm(searchTerms, prefix + "priority.id", getId(criteriaModel.getPriority()));
        addSimpleTerm(searchTerms, prefix + "eventResult", criteriaModel.getEventResult());
        addSimpleTerm(searchTerms, prefix + "eventStatus", criteriaModel.getEventStatus());
        if (criteriaModel.isUnassignedOnly()) {
            addNullTerm(searchTerms, prefix + "assignee");
            addNullTerm(searchTerms, prefix + "assignedGroup");
        } else {
            addSimpleTerm(searchTerms, prefix + "assignee", criteriaModel.getAssignee());
            addSimpleTerm(searchTerms, prefix + "assignedGroup", criteriaModel.getAssignedUserGroup());
        }

        if (criteriaModel.getWorkflowState() == WorkflowStateCriteria.COMPLETE) {
            addSimpleTerm(searchTerms, prefix + "workflowState", com.n4systems.model.WorkflowState.COMPLETED);
        } else if (criteriaModel.getWorkflowState() == WorkflowStateCriteria.OPEN) {
            addSimpleTerm(searchTerms, prefix + "workflowState", com.n4systems.model.WorkflowState.OPEN);
        } else if (criteriaModel.getWorkflowState() == WorkflowStateCriteria.CLOSED) {
            addSimpleTerm(searchTerms, prefix + "workflowState", com.n4systems.model.WorkflowState.CLOSED);
        }

        if (IncludeDueDateRange.HAS_NO_DUE_DATE.equals(criteriaModel.getIncludeDueDateRange())) {
            addNullTerm(searchTerms, prefix +  "dueDate");
        } else if (IncludeDueDateRange.HAS_A_DUE_DATE.equals(criteriaModel.getIncludeDueDateRange())) {
            addNotNullTerm(searchTerms, prefix +  "dueDate");
        } else if (criteriaModel.getWorkflowState() == WorkflowStateCriteria.OPEN || IncludeDueDateRange.SELECT_DUE_DATE_RANGE.equals(criteriaModel.getIncludeDueDateRange())) {
            if (criteriaModel.getDueDateRange() != null && !criteriaModel.getDueDateRange().isEmptyCustom()) {
                // recall : due dates don't have timeZone.
                Date from = dateService.calculateFromDate(criteriaModel.getDueDateRange());
                Date to = dateService.calculateInclusiveToDate(criteriaModel.getDueDateRange());
                addDateRangeTerm(searchTerms, prefix + "dueDate", from, to);
            }
        }

        addHasGpsTerm(searchTerms, criteriaModel);
        if (includeGps) {
            addGpsLocationTerm(searchTerms, criteriaModel);
        }

        if (criteriaModel.getDateRange() != null && !criteriaModel.getDateRange().isEmptyCustom()) {
            if (criteriaModel.getWorkflowState() == WorkflowStateCriteria.COMPLETE || criteriaModel.getWorkflowState()== WorkflowStateCriteria.CLOSED) {
                Date from = dateService.calculateFromDateWithTimeZone(criteriaModel.getDateRange(), timeZone);
                Date to = dateService.calculateInclusiveToDateWithTimeZone(criteriaModel.getDateRange(), timeZone);
                addDateRangeTerm(searchTerms, prefix + "completedDate", DateHelper.convertToUTC(from, timeZone), DateHelper.convertToUTC(to, timeZone));
            } else if (criteriaModel.getWorkflowState() == WorkflowStateCriteria.ALL) {
                searchTerms.add(new CompletedOrDueDateRange(timeZone, criteriaModel.getDateRange()));
            }
        }

        if(criteriaModel.getEventBook() != null && criteriaModel.getEventBook().getId() == 0) {
            addNullTerm(searchTerms, prefix + "book.id");
        } else {
            addSimpleTerm(searchTerms, prefix + "book.id", getId(criteriaModel.getEventBook()));
        }

        Long assignedUserId = getId(criteriaModel.getAssignedTo());
        if (assignedUserId != null) {
            addAssignedUserTerm(assignedUserId, criteriaModel, searchTerms);
        }

        if (!criteriaModel.getLocation().isBlank()) {
            searchTerms.add(new LocationTerm(criteriaModel, getId(criteriaModel.getLocation().getPredefinedLocation())));
        }
    }


    private void addHasGpsTerm(List<SearchTermDefiner> search, EventReportCriteria criteriaModel) {
        if(criteriaModel.getHasGps() != null) {
            search.add(new HasGpsTerm(criteriaModel));
        }
    }

    private void addGpsLocationTerm(List<SearchTermDefiner> search, EventReportCriteria criteriaModel) {
        String prefix = criteriaModel.isShowMostRecentEventsOnly() ? "event." : "";
        search.add(new GpsBoundsTerm(prefix + "gpsLocation",criteriaModel.getBounds()));
    }


    private void addAssignedUserTerm(Long assignedUserId, EventReportCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {
        searchTerms.add(new AssignedUserTerm(criteriaModel, assignedUserId));
    }

    private void addAssetStatusTerm(EventReportCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {
        searchTerms.add(new AssetStatusTerm(criteriaModel));
    }

    @Override
    protected <E> void addSortJoinTerms(QueryBuilder<E> searchBuilder, EventReportCriteria criteriaModel) {
        ColumnMappingView sortColumn = criteriaModel.getSortColumn();
        if (criteriaModel.isShowMostRecentEventsOnly()) {
            if(sortColumn.getJoinExpression() != null) {
                String [] joinExpressions = sortColumn.getJoinExpression().split(",");
                String newJoinExpression = "";
                for(String joinExpression: joinExpressions) {
                    if(!joinExpression.startsWith("asset.") && !joinExpression.startsWith("event.")) {
                        newJoinExpression +="event." + joinExpression + ",";
                    }
                }
                if(!newJoinExpression.isEmpty()) {
                    sortColumn.setJoinExpression(newJoinExpression.substring(0, newJoinExpression.length()-1));
                }
            }
        }
        super.addSortJoinTerms(searchBuilder, criteriaModel);
    }

    @Override
    protected void addJoinTerms(EventReportCriteria criteriaModel, List<JoinTerm> joinTerms) {
        Long predefLocationId = getId(criteriaModel.getLocation().getPredefinedLocation());

        String prefix = criteriaModel.isShowMostRecentEventsOnly() ? "event." : "";
        WorkflowStateCriteria status = criteriaModel.getWorkflowState();

        if (predefLocationId != null && status.includesIncomplete()) {
            JoinTerm joinTerm = new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER, "asset.advancedLocation.predefinedLocation.searchIds", "assetPreLocSearchId", true);
            joinTerms.add(joinTerm);
		}

        if (predefLocationId != null && status.includesComplete()) {
            JoinTerm joinTerm = new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER, prefix + "advancedLocation.predefinedLocation.searchIds", "eventPreLocSearchId", true);
            joinTerms.add(joinTerm);
        }

        if (criteriaModel.sortingByDivision()) {
            addOrgJoinTerms(criteriaModel, "divisionOrg", "assetDivisionOrg", "eventDivisionOrg", joinTerms);
        }

        if (criteriaModel.sortingByOrganization()) {
            addOrgJoinTerms(criteriaModel, "secondaryOrg", "assetSecondaryOrg", "eventSecondaryOrg", joinTerms);
        }

        if (criteriaModel.sortingByCustomer()) {
            addOrgJoinTerms(criteriaModel, "customerOrg", "assetCustomerOrg", "eventCustomerOrg", joinTerms);
        }
    }


    private void addOrgJoinTerms(EventReportCriteria criteriaModel, String basePath, String assetJoinAlias, String eventJoinAlias, List<JoinTerm> joinTerms) {
        String prefix = criteriaModel.isShowMostRecentEventsOnly() ? "event." : "";
        WorkflowStateCriteria status = criteriaModel.getWorkflowState();
        if (status.includesIncomplete()) {
            JoinTerm joinTerm = new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER, "asset.owner." + basePath, assetJoinAlias, true);
            joinTerms.add(joinTerm);
        }

        if (status.includesComplete()) {
            JoinTerm joinTerm = new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER, prefix + "owner." + basePath, eventJoinAlias, true);
            joinTerms.add(joinTerm);
        }
    }

    @Override
    protected void addSortTerms(EventReportCriteria criteriaModel, QueryBuilder<?> searchBuilder, ColumnMappingView sortColumn, SortDirection sortDirection) {
        String prefix = criteriaModel.isShowMostRecentEventsOnly() ? "event." : "";

        // Reporting has a few special case sorts that are more complicated due to displaying two types of data. ie sorting on two columns.
        // To prevent weird mixing, when one of these special cases occurs, we always sort by state first
        if (criteriaModel.sortingByLocation()) {
            addStatusSortIfNecessary(criteriaModel, searchBuilder, sortDirection);

            if (criteriaModel.getWorkflowState().includesComplete()) {
                SortTerm sortTerm = new SortTerm( prefix +"advancedLocation.predefinedLocation.id", sortDirection);
                SortTerm sortTerm2 = new SortTerm( prefix +"advancedLocation.freeformLocation", sortDirection);

                searchBuilder.getOrderArguments().add(sortTerm.toSortField());
                searchBuilder.getOrderArguments().add(sortTerm2.toSortField());
            }

            if (criteriaModel.getWorkflowState().includesIncomplete()) {
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
            if(criteriaModel.isShowMostRecentEventsOnly()) {
                String[] sortExpressions = sortColumn.getSortExpression().split(",");
                String newSortExpressions = "";
                for(String sortExpression : sortExpressions) {
                    if(!sortExpression.startsWith("asset.")
                            && !sortExpression.startsWith("type.")
                            && !sortExpression.startsWith("event.")) {
                        newSortExpressions +="event." + sortExpression + ",";
                    }
                }
                if(!newSortExpressions.isEmpty()) {
                    sortColumn.setSortExpression(newSortExpressions.substring(0, newSortExpressions.length()-1));
                }
            }
            super.addSortTerms(criteriaModel, searchBuilder, sortColumn, sortDirection);
        }
    }

    private void addOrgSort(EventReportCriteria criteriaModel, QueryBuilder<?> searchBuilder, SortDirection sortDirection, String assetOrgAlias, String eventOrgAlias) {
        addStatusSortIfNecessary(criteriaModel, searchBuilder, sortDirection);

        if (criteriaModel.getWorkflowState().includesComplete()) {
            SortTerm sortTerm = new SortTerm(eventOrgAlias+".name", sortDirection);
            sortTerm.setAlwaysDropAlias(true);
            searchBuilder.getOrderArguments().add(sortTerm.toSortField());
        }

        if (criteriaModel.getWorkflowState().includesIncomplete()) {
            SortTerm sortTerm = new SortTerm(assetOrgAlias+".name", sortDirection);
            sortTerm.setAlwaysDropAlias(true);
            searchBuilder.getOrderArguments().add(sortTerm.toSortField());
        }

    }

    // Because the column we are sorting by is actually two columns (one for completed events and one for incomplete events)
    // we decided to sort by workflowState first so there's less unexpected weirdness mixing complete/incomplete.
    private void addStatusSortIfNecessary(EventReportCriteria criteriaModel, QueryBuilder<?> searchBuilder, SortDirection sortDirection) {
        if (criteriaModel.getWorkflowState() == WorkflowStateCriteria.ALL) {
            String prefix = criteriaModel.isShowMostRecentEventsOnly() ? "event." : "";
            SortTerm sortTerm = new SortTerm(prefix + "workflowState", sortDirection);
            searchBuilder.getOrderArguments().add(sortTerm.toSortField());
        }
    }

    @Override
    protected <E> QueryBuilder<E> createAppropriateQueryBuilder(EventReportCriteria criteria, Class<E> searchClass) {
        if (criteria.isIncludeSafetyNetwork()) {
            return new QueryBuilder<E>(searchClass, new NetworkIdSecurityFilter(securityContext.getUserSecurityFilter(), "asset.networkId"));
        } if(criteria.isShowMostRecentEventsOnly()) {
            return new QueryBuilder<E>(MostRecentThingEvent.class, securityContext.getUserSecurityFilter());
        }
        return super.createAppropriateQueryBuilder(criteria, searchClass);
    }

    @Override
    protected List<ThingEvent> convertResults(EventReportCriteria criteriaModel, List results) {
        if(criteriaModel.isShowMostRecentEventsOnly()) {
            List<ThingEvent> convertedResults = Lists.newArrayList();
            for (Object result : results) {
                ThingEvent event = ((MostRecentThingEvent) result).getEvent();
                convertedResults.add(event);
            }
            return convertedResults;
        }
        return super.convertResults(criteriaModel, results);
    }
}
