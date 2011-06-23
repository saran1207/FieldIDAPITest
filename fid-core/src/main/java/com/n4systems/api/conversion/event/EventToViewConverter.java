package com.n4systems.api.conversion.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.CriteriaResultView;
import com.n4systems.api.model.EventView;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.eventschedule.NextEventDateByEventLoader;
import com.n4systems.model.orgs.BaseOrg;

public class EventToViewConverter implements ModelToViewConverter<Event, EventView> {
	private final NextEventDateByEventLoader nextDateLoader;
	
	public EventToViewConverter(NextEventDateByEventLoader nextDateLoader) {
		this.nextDateLoader = nextDateLoader;
	}

	@Override
	public EventView toView(Event model) throws ConversionException {
		EventView view = new EventView();
		
		convertDirectFields(model, view);
		converterPerformedBy(model, view);
		convertAssetIdentifier(model, view);
		convertEventStatus(model, view);
		convertOwnerFields(model.getOwner(), view);
		convertBook(model, view);
		convertAssetStatus(model, view);
		convertNextDate(model, view);
		convertCriteriaResults(model, view);
		
		return view;
	}

	private void convertCriteriaResults(Event model, EventView view) {
		view.setCriteriaResults(convertCriteriaResults(model));
	}

	private List<CriteriaResultView> convertCriteriaResults(Event model) {
		List<CriteriaResultView> results = new ArrayList<CriteriaResultView>();
		for (CriteriaResult result:model.getResults()) { 
			results.add(convertCriteriaResult(model, result));
		}
		return results;
	}

	private CriteriaResultView convertCriteriaResult(Event model, CriteriaResult result) {
		CriteriaResultView resultView = new CriteriaResultView();		
		Criteria criteria = result.getCriteria();
		
		resultView.setSection(model.findSection(criteria).getDisplayName());
		resultView.setDisplayText(result.getCriteria().getDisplayText());
		// NOTE : for exporting, we leave these fields blank.
		resultView.setRecommendation("");		
		resultView.setDeficiencyString("");
		resultView.setResultString("");
		return resultView;
	}

	protected void convertDirectFields(Event model, EventView view) {
		view.setComments(model.getComments());
		view.setDatePerformed(model.getDate());
		view.setLocation(model.getAdvancedLocation().getFreeformLocation());
		view.setPrintable(model.isPrintable());
	}

	protected void convertEventStatus(Event model, EventView view) {
		view.setStatus(model.getStatus().getDisplayName());
	}

	protected void convertAssetIdentifier(Event model, EventView view) {
		view.setIdentifier(model.getAsset().getSerialNumber());
	}

	protected void converterPerformedBy(Event model, EventView view) {
		view.setPerformedBy(model.getPerformedBy().getFullName());
	}
	
	protected void convertNextDate(Event model, EventView view) {
		Date nextDate = nextDateLoader.setEvent(model).load();
		view.setNextEventDate(nextDate);
	}

	protected void convertBook(Event model, EventView view) {
		if (model.getBook() != null) {
			view.setEventBook(model.getBook().getName());
		}
	}

	protected void convertAssetStatus(Event model, EventView view) {
		if (model.getAssetStatus() != null) {
			view.setAssetStatus(model.getAssetStatus().getName());
		}
	}

	protected void convertOwnerFields(BaseOrg owner, EventView view) {
		view.setOrganization(owner.getInternalOrg().getName());
		
		if (owner.isExternal()) {
			view.setCustomer(owner.getCustomerOrg().getName());
		
			if (owner.isDivision()) {
				view.setDivision(owner.getDivisionOrg().getName());
			}
		}
	}
	
}
