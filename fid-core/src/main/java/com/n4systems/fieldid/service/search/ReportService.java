package com.n4systems.fieldid.service.search;

import com.n4systems.model.Event;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

import java.util.List;

public class ReportService extends SearchService<EventReportCriteriaModel, Event> {

    public ReportService() {
        super(Event.class);
    }

    @Override
    protected void addSearchTerms(EventReportCriteriaModel criteriaModel, List<SearchTermDefiner> searchTerms) {
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
        addSimpleTerm(searchTerms, "assetStatus.id", getId(criteriaModel.getAssetStatus()));
        addSimpleTerm(searchTerms, "type.id", getId(criteriaModel.getEventType()));
        addSimpleTerm(searchTerms, "type.group.id", getId(criteriaModel.getEventTypeGroup()));
        addSimpleTerm(searchTerms, "performedBy.id", getId(criteriaModel.getPerformedBy()));
        addSimpleTerm(searchTerms, "schedule.project.id", getId(criteriaModel.getJob()));
        addSimpleTerm(searchTerms, "status", criteriaModel.getResult());
        if (criteriaModel.getDateRange() != null) {
            addDateRangeTerm(searchTerms, "date", criteriaModel.getDateRange().calculateFromDate(), criteriaModel.getDateRange().calculateToDate());
        }

        if(criteriaModel.getEventBook() != null && criteriaModel.getEventBook().getId() == 0) {
            addNullTerm(searchTerms, "book.id");
        } else {
            addSimpleTerm(searchTerms, "book.id", getId(criteriaModel.getEventBook()));
        }

        Long assignedUserId = getId(criteriaModel.getAssignedTo());
        if (assignedUserId != null) {
            addSimpleTerm(searchTerms, "assignedTo.assignmentApplyed", true);

            if(assignedUserId == 0) {
                addNullTerm(searchTerms, "assignedTo.assignedUser.id");
            } else {
                addSimpleTerm(searchTerms, "assignedTo.assignedUser.id", assignedUserId);
            }
        }

        addPredefinedLocationTerm(searchTerms, criteriaModel);
    }

	private void addPredefinedLocationTerm(List<SearchTermDefiner> search, EventReportCriteriaModel criteriaModel) {
        Long predefLocationId = getId(criteriaModel.getLocation().getPredefinedLocation());
        if (predefLocationId != null) {
			search.add(new PredefinedLocationSearchTerm("preLocSearchId", predefLocationId));
		}
	}

    @Override
    protected void addJoinTerms(EventReportCriteriaModel criteriaModel, List<JoinTerm> joinTerms) {
        Long predefLocationId = getId(criteriaModel.getLocation().getPredefinedLocation());
        if (predefLocationId != null) {
			addRequiredLeftJoin(joinTerms, "advancedLocation.predefinedLocation.searchIds", "preLocSearchId");
		}
    }

}
