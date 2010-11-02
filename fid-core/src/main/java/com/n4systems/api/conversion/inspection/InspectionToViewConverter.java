package com.n4systems.api.conversion.inspection;

import java.util.Date;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.EventView;
import com.n4systems.model.Event;
import com.n4systems.model.inspectionschedule.NextEventDateByEventLoader;
import com.n4systems.model.orgs.BaseOrg;

public class InspectionToViewConverter implements ModelToViewConverter<Event, EventView> {
	private final NextEventDateByEventLoader nextDateLoader;
	
	public InspectionToViewConverter(NextEventDateByEventLoader nextDateLoader) {
		this.nextDateLoader = nextDateLoader;
	}
	
	@Override
	public EventView toView(Event model) throws ConversionException {
		EventView view = new EventView();
		
		convertDirectFields(model, view);
		converterPerformedBy(model, view);
		convertAssetIdentifier(model, view);
		convertInspectionStatus(model, view);
		convertOwnerFields(model.getOwner(), view);
		convertBook(model, view);
		convertAssetStatus(model, view);
		convertNextDate(model, view);
		
		return view;
	}

	protected void convertDirectFields(Event model, EventView view) {
		view.setComments(model.getComments());
		view.setDatePerformed(model.getDate());
		view.setLocation(model.getAdvancedLocation().getFreeformLocation());
		view.setPrintable(model.isPrintable());
	}

	protected void convertInspectionStatus(Event model, EventView view) {
		view.setStatus(model.getStatus().getDisplayName());
	}

	protected void convertAssetIdentifier(Event model, EventView view) {
		view.setIdentifier(model.getAsset().getSerialNumber());
	}

	protected void converterPerformedBy(Event model, EventView view) {
		view.setPerformedBy(model.getPerformedBy().getFullName());
	}
	
	protected void convertNextDate(Event model, EventView view) {
		Date nextDate = nextDateLoader.setInspection(model).load();
		view.setNextInspectionDate(nextDate);
	}

	protected void convertBook(Event model, EventView view) {
		if (model.getBook() != null) {
			view.setInspectionBook(model.getBook().getName());
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
