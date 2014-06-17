package com.n4systems.fieldid.service.search;

import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.user.User;
import com.n4systems.services.reporting.AssetSearchRecord;
import com.n4systems.services.search.MappedResults;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.terms.GpsBoundsTerm;
import com.n4systems.util.persistence.search.terms.HasGpsTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Transactional(readOnly = true)
public class AssetSearchService extends SearchService<AssetSearchCriteria, Asset, AssetSearchRecord> {

    public AssetSearchService() {
        super(Asset.class);
    }

    @Override
    protected void addSearchTerms(AssetSearchCriteria criteriaModel, List<SearchTermDefiner> search, boolean includeGps) {
        User user = getCurrentUser();
        TimeZone timeZone = user.getTimeZone();

        addWildcardOrStringTerm(search, "rfidNumber", criteriaModel.getRfidNumber());
		addWildcardOrStringTerm(search, "identifier", criteriaModel.getIdentifier());
		addWildcardOrStringTerm(search, "advancedLocation.freeformLocation", criteriaModel.getLocation().getFreeformLocation());
		if (isIntegrationEnabled()) {
			addWildcardOrStringTerm(search, "shopOrder.order.orderNumber", criteriaModel.getOrderNumber());
		} else {
			addWildcardOrStringTerm(search, "nonIntergrationOrderNumber", criteriaModel.getOrderNumber());
		}
		addWildcardOrStringTerm(search, "customerRefNumber", criteriaModel.getReferenceNumber());
		addWildcardOrStringTerm(search, "purchaseOrder", criteriaModel.getPurchaseOrder());
		addSimpleTerm(search, "type.id", getId(criteriaModel.getAssetType()));
		addSimpleTerm(search, "type.group.id", getId(criteriaModel.getAssetTypeGroup()));
		addSimpleTerm(search, "assetStatus.id", getId(criteriaModel.getAssetStatus()));
		addDateRangeTerm(search, "identified", dateService.calculateFromDate(criteriaModel.getDateRange()), dateService.calculateInclusiveToDateWithTimeZone(criteriaModel.getDateRange(), timeZone));

		addPredefinedLocationTerm(search, criteriaModel);
		addAssignedUserTerm(search, criteriaModel);

        addHasGpsTerm(search, criteriaModel);
        if (includeGps) {
            addGpsLocationTerm(search, criteriaModel);
        }
    }

    private void addHasGpsTerm(List<SearchTermDefiner> search, AssetSearchCriteria criteriaModel) {
        if(criteriaModel.getHasGps() != null) {
            search.add(new HasGpsTerm(criteriaModel.getHasGps()));
        }
    }

    private void addGpsLocationTerm(List<SearchTermDefiner> search, AssetSearchCriteria criteriaModel) {
        search.add(new GpsBoundsTerm("gpsLocation",criteriaModel.getBounds()));
    }


    private void addPredefinedLocationTerm(List<SearchTermDefiner> search, AssetSearchCriteria criteriaModel) {
        Long predefLocationId = getId(criteriaModel.getLocation().getPredefinedLocation());
        if (predefLocationId != null) {
			search.add(new PredefinedLocationSearchTerm("preLocSearchId", predefLocationId));
		}
	}

    @Override
    protected void addJoinTerms(AssetSearchCriteria criteriaModel, List<JoinTerm> joinTerms) {
        Long predefLocationId = getId(criteriaModel.getLocation().getPredefinedLocation());
        if (predefLocationId != null) {
			addRequiredLeftJoin(joinTerms, "advancedLocation.predefinedLocation.searchIds", "preLocSearchId");
		}
    }

    private void addAssignedUserTerm(List<SearchTermDefiner> search, AssetSearchCriteria criteriaModel) {
        Long assignedUserId = getId(criteriaModel.getAssignedTo());
        if(assignedUserId != null && assignedUserId == 0) {
			addNullTerm(search, "assignedUser.id");
		} else {
			addSimpleTerm(search, "assignedUser.id", assignedUserId);
		}
	}

    @Override
    protected void addSortTerms(AssetSearchCriteria criteriaModel, QueryBuilder<?> searchBuilder, ColumnMappingView sortColumn, SortDirection sortDirection) {
        if (criteriaModel.sortingByOrIncludingLastEventDate()) {

            // Adjust the query. We need to insert a subquery to find a derived column: the last event date.
            QueryBuilder<Event> subQuery = createUserSecurityBuilder(Event.class);
            subQuery.setMaxSelect("completedDate");
            subQuery.addSimpleWhere("workflowState", WorkflowState.COMPLETED);
            subQuery.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
            NoVariableClause eventMatchesOuterAssetClause = new NoVariableClause("outerAssetClause", WhereParameter.Comparator.EQ, "evt.asset", "obj", WhereClause.ChainOp.AND);
            eventMatchesOuterAssetClause.setDropAlias(true);
            subQuery.addWhere(eventMatchesOuterAssetClause);
            subQuery.setQueryAlias("lastEventDate");
            subQuery.getFromArgument().setAlias("evt");


            if (criteriaModel.sortingByLastEventDate()) {
                OrderClause orderClause = new OrderClause("lastEventDate", sortDirection.equals(SortDirection.ASC));
                orderClause.setAlwaysDropAlias(true);

                searchBuilder.getOrderArguments().add(orderClause);
            } else {
                super.addSortTerms(criteriaModel, searchBuilder, sortColumn, sortDirection);
            }

            MultipleSelectClause multiSelect = new MultipleSelectClause();
            multiSelect.addSimpleSelect("obj");
            multiSelect.addSubQuery(subQuery);

            searchBuilder.setSelectArgument(multiSelect);
        } else {
            super.addSortTerms(criteriaModel, searchBuilder, sortColumn, sortDirection);
        }
    }

    @Override
    protected List<Asset> convertResults(AssetSearchCriteria criteriaModel, List results) {
        if (criteriaModel.sortingByOrIncludingLastEventDate() && !results.isEmpty() && results.iterator().next() instanceof Object[]) {
            List<Asset> convertedResults = new ArrayList<Asset>();
            for (Object result : results) {
                Object[] objectArray = (Object[]) result;
                Asset asset = (Asset) objectArray[0];
                asset.setLastEventDate((Date)objectArray[1]);
                convertedResults.add(asset);
            }
            return convertedResults;
        }
        return super.convertResults(criteriaModel, results);
    }

    public MappedResults<AssetSearchRecord> performMapSearch(AssetSearchCriteria criteria) {
        QueryBuilder<Asset> query = createBaseMappedSearchQueryBuilder(criteria);

        int limit = criteria.getMaxItemsBeforeGrouping() + 1;
        query.setLimit(limit);
        List<Asset> queryResults = persistenceService.findAll(query);
        MappedResults<AssetSearchRecord> searchResult = new MappedResults<AssetSearchRecord>();
        if (queryResults.size()==limit) {
            searchResult.setGroupedResult(queryResults);
        } else {
            for (Asset asset:queryResults) {
                searchResult.add(new AssetSearchRecord(asset));
            }
        }

        return searchResult;
    }


}
