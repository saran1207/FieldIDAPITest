package com.n4systems.fieldid.service.search;

import com.n4systems.model.Asset;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

import java.util.List;
import java.util.TimeZone;

public class AssetSearchService extends SearchService<AssetSearchCriteria, Asset> {



    public AssetSearchService() {
        super(Asset.class);
    }

    @Override
    protected void addSearchTerms(AssetSearchCriteria criteriaModel, List<SearchTermDefiner> search) {
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

}
