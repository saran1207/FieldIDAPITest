package com.n4systems.fieldid.service.search;

import com.n4systems.model.Asset;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.user.User;
import com.n4systems.services.reporting.AssetSearchRecord;
import com.n4systems.services.search.MappedResults;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.search.AssetLockoutTagoutStatus;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.terms.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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

        addHasProcedureTerm(search, criteriaModel.getAssetLockoutTagoutStatus());
    }

    private void addHasProcedureTerm(List<SearchTermDefiner> search, AssetLockoutTagoutStatus status) {
        if(status != null && !status.equals(AssetLockoutTagoutStatus.ALL)) {
            QueryBuilder<ProcedureDefinition> subQuery = createUserSecurityBuilder(ProcedureDefinition.class);
            subQuery.setSimpleSelect("asset.id", true);
            subQuery.addWhere(WhereParameter.Comparator.IN, "publishedState", "publishedState", Arrays.asList(PublishedState.ACTIVE_STATES));
            if (status.equals(AssetLockoutTagoutStatus.WITHPROCEDURES))
                search.add(new SubSelectInTerm("id", subQuery));
            else
                search.add(new SubSelectNotInTerm("id", subQuery));
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
    protected List<Asset> convertResults(AssetSearchCriteria criteriaModel, List results) {
        if (criteriaModel.sortingByOrIncludingLastEventDate() && !results.isEmpty() && results.iterator().next() instanceof Object[]) {
            List<Asset> convertedResults = new ArrayList<>();
            for (Object result : results) {
                Object[] objectArray = (Object[]) result;
                Asset asset = (Asset) objectArray[0];
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
        MappedResults<AssetSearchRecord> searchResult = new MappedResults<>();
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
