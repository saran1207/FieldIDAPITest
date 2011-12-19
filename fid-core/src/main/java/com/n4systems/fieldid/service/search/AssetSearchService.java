package com.n4systems.fieldid.service.search;

import com.n4systems.model.Asset;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

import java.util.List;

public class AssetSearchService extends SearchService<AssetSearchCriteriaModel, Asset> {

    public AssetSearchService() {
        super(Asset.class);
    }

    @Override
    protected void addSearchTerms(AssetSearchCriteriaModel criteriaModel, List<SearchTermDefiner> search) {
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
		addDateRangeTerm(search, "identified", criteriaModel.getIdentifiedFromDate(), criteriaModel.getIdentifiedToDate());

		addPredefinedLocationTerm(search, criteriaModel);
		addAssignedUserTerm(search, criteriaModel);
    }

	private void addPredefinedLocationTerm(List<SearchTermDefiner> search, AssetSearchCriteriaModel criteriaModel) {
        Long predefLocationId = getId(criteriaModel.getLocation().getPredefinedLocation());
        if (predefLocationId != null) {
			search.add(new PredefinedLocationSearchTerm("preLocSearchId", predefLocationId));
		}
	}

	private void addAssignedUserTerm(List<SearchTermDefiner> search, AssetSearchCriteriaModel criteriaModel) {
        Long assignedUserId = getId(criteriaModel.getAssignedTo());
        if(assignedUserId != null && assignedUserId == 0) {
			addNullTerm(search, "assignedUser.id");
		} else {
			addSimpleTerm(search, "assignedUser.id", assignedUserId);
		}
	}

}
