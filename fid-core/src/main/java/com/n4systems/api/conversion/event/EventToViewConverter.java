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
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Event;
import com.n4systems.model.eventschedule.NextEventDateByEventLoader;
import com.n4systems.model.eventschedule.NextEventDateByEventPassthruLoader;
import com.n4systems.model.orgs.BaseOrg;

public class EventToViewConverter implements ModelToViewConverter<Event, EventView> {
	private final NextEventDateByEventLoader nextDateLoader;
	
	public EventToViewConverter(NextEventDateByEventLoader nextDateLoader) {
		this.nextDateLoader = nextDateLoader;
	}

	public EventToViewConverter() {
		this(new NextEventDateByEventPassthruLoader());
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
		CriteriaSection section = model.findSection(criteria);
		String sectionName = section!=null ? section.getDisplayName() : "Undefined Section : " + criteria.getDisplayName();
		resultView.setSection(sectionName);
		resultView.setDisplayText(result.getCriteria().getDisplayText());
		resultView.setRecommendation(getRecommendation(result));		
		resultView.setDeficiencyString(getDeficiency(result));
		resultView.setResultString(result.getResultString());
		return resultView;
	}

	private String getDeficiency(CriteriaResult result) {
		return result.getDeficiencies().size() > 0 ? result.getDeficiencies().get(0).getText() : "";
	}

	private String getRecommendation(CriteriaResult result) {
		return result.getRecommendations().size() > 0 ? result.getRecommendations().get(0).getText() : "";
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
		view.setIdentifier(model.getAsset().getIdentifier());
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
