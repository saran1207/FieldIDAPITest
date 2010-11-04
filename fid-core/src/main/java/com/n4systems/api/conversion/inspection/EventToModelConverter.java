package com.n4systems.api.conversion.inspection;

import com.n4systems.api.model.EventView;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventType;
import com.n4systems.model.assetstatus.AssetStatusByNameLoader;
import rfid.ejb.entity.AssetStatus;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ViewToModelConverter;
import com.n4systems.model.Asset;
import com.n4systems.model.Status;
import com.n4systems.model.inspectionbook.EventBookFindOrCreateLoader;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByFullNameLoader;
import com.n4systems.persistence.Transaction;

public class EventToModelConverter implements ViewToModelConverter<Event, EventView> {
	private final OrgByNameLoader orgLoader;
	private final SmartSearchLoader assetLoader;
	private final AssetStatusByNameLoader assetStatusLoader;
	private final EventBookFindOrCreateLoader eventBookLoader;
	private final UserByFullNameLoader userLoader;
	
	private EventType type;
	
	public EventToModelConverter(OrgByNameLoader orgLoader, SmartSearchLoader assetLoader, AssetStatusByNameLoader assetStatusLoader, EventBookFindOrCreateLoader eventBookLoader, UserByFullNameLoader userLoader) {
		this.orgLoader = orgLoader;
		this.assetLoader = assetLoader;
		this.assetStatusLoader = assetStatusLoader;
		this.eventBookLoader = eventBookLoader;
		this.userLoader = userLoader;
	}

	@Override
	public Event toModel(EventView view, Transaction transaction) throws ConversionException {
		Event model = new Event();
		
		resolveType(model);
		resolveOwner(view, model, transaction);
		
		model.setAdvancedLocation(Location.onlyFreeformLocation(view.getLocation()));
		model.setDate(view.getDatePerformedAsDate());
		model.setComments(view.getComments());
		
		resolveStatus(view.getStatus(), model);
		resolveAsset(view, model, transaction);
		resolvePrintable(view, model);
		resolvePerformedBy(view, model, transaction);
		resolveEventBook(view, model, transaction);
		
		resolveAssetStatus(view, model, transaction);
		
		return model;
	}

	protected void resolveType(Event model) {
		model.setType(type);
		model.setFormVersion(type.getFormVersion());
		model.setTenant(type.getTenant());
	}
	
	protected void resolveStatus(String statusName, Event model) {
		String cleanStatus = statusName.toUpperCase();
		
		Status status = Status.NA;
		if (cleanStatus.equals("PASS")) {
			status = Status.PASS;
		} else if (cleanStatus.equals("FAIL")) {
			status = Status.FAIL;
		}
		
		model.setStatus(status);
	}

	protected void resolveAsset(EventView view, Event model, Transaction transaction) {
		// the validator will ensure this returns exactly 1 asset
		Asset asset = assetLoader.setSearchText(view.getIdentifier()).load(transaction).get(0);
		model.setAsset(asset);
	}

	protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {
		// the validator will ensure this returns exactly 1 user
		User performedBy = userLoader.setFullName(view.getPerformedBy()).load(transaction).get(0);
		model.setPerformedBy(performedBy);
	}

	protected void resolvePrintable(EventView view, Event model) {
		if (view.isPrintable() != null) {
			model.setPrintable(view.isPrintable());
		} else {
			model.setPrintable(type.isPrintable());
		}
	}

	protected void resolveEventBook(EventView view, Event model, Transaction transaction) {
		if (view.getEventBook() != null) {
			eventBookLoader.setName(view.getEventBook());
			eventBookLoader.setOwner(model.getOwner());
			EventBook book = eventBookLoader.load(transaction);
			model.setBook(book);
		}
	}

	protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {
		if (view.getAssetStatus() != null) {
			AssetStatus status = assetStatusLoader.setName(view.getAssetStatus()).load(transaction);
			model.setAssetStatus(status);
		}
	}

	protected void resolveOwner(EventView view, Event model, Transaction transaction) {
		orgLoader.setOrganizationName(view.getOrganization());
		orgLoader.setCustomerName(view.getCustomer());
		orgLoader.setDivision(view.getDivision());
		
		BaseOrg owner = orgLoader.load(transaction);
		model.setOwner(owner);
	}

	public void setType(EventType type) {
		this.type = type;
	}
	
	public EventType getType() {
		return type;
	}
}
