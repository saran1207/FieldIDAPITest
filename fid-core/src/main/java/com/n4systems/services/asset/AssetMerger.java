package com.n4systems.services.asset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.TenantNotValidForActionException;
import com.n4systems.exceptions.UsedOnMasterEventException;
import com.n4systems.exceptions.asset.AssetTypeMissMatchException;
import com.n4systems.exceptions.asset.DuplicateAssetException;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.SubEvent;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.EventScheduleService;
import com.n4systems.services.EventScheduleServiceImpl;
import com.n4systems.util.persistence.QueryBuilder;

public class AssetMerger {

	private final PersistenceManager persistenceManager;
	private final AssetManager assetManager;
	private final EventManager eventManager;
	private final User user;

	private final EventScheduleService scheduleService;

	public AssetMerger(PersistenceManager persistenceManager, AssetManager assetManager, EventManager eventManger, User user) {
		this(persistenceManager, assetManager, eventManger, new EventScheduleServiceImpl(persistenceManager), user);
	}

	public AssetMerger(PersistenceManager persistenceManager, AssetManager assetManager, EventManager eventManger, EventScheduleService eventScheduleService, User user) {
		this.persistenceManager = persistenceManager;
		this.assetManager = assetManager;
		this.eventManager = eventManger;
		this.user = user;
		this.scheduleService = eventScheduleService;
	}

	public Asset merge(Asset winningAsset, Asset losingAsset) {
		guard(winningAsset, losingAsset);

		moveInspections(winningAsset, losingAsset);
		moveSubInspections(winningAsset, losingAsset);
		archiveLosingAsset(losingAsset);

		return winningAsset;
	}

	private void guard(Asset winningAsset, Asset losingAsset) {
		if (!losingAsset.getType().equals(winningAsset.getType())) {
			throw new AssetTypeMissMatchException("asset types must match");
		}
		
		if (losingAsset.equals(winningAsset)) {
			throw new DuplicateAssetException("you can't merge an asset into itself.");
		}

		if (!losingAsset.getTenant().equals(winningAsset.getTenant())) {
			throw new TenantNotValidForActionException("tenants must match");
		}
	}

	private void archiveLosingAsset(Asset losingAsset) {
		try {
			assetManager.archive(losingAsset, user);
		} catch (UsedOnMasterEventException e) {
			throw new ProcessFailureException("could not archive the asset. still on a master", e);
		}
	}

	private void moveInspections(Asset winningAsset, Asset losingAsset) {
		QueryBuilder<Event> inspections = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter()).addSimpleWhere("state", EntityState.ACTIVE).addSimpleWhere("asset", losingAsset);
		List<Event> inspectionsToMove = persistenceManager.findAll(inspections);

		for (Event eventToMove : inspectionsToMove) {
			eventToMove.setAsset(winningAsset);
			updateInspection(eventToMove);
			updateSchedule(winningAsset, eventToMove.getSchedule());
		}
	}

	private void updateSchedule(Asset winningAsset, EventSchedule schedule) {
		if (schedule != null) {
			schedule.setAsset(winningAsset);
			scheduleService.updateSchedule(schedule);
		}
	}

	private void moveSubInspections(Asset winningAsset, Asset losingAsset) {
		String query = "SELECT DISTINCT master from " + Event.class.getName() + " master, IN (master.subInspections) subInspection "
				+ "where subInspection.asset = :losingAsset AND master.state = :activeState";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("losingAsset", losingAsset);
		parameters.put("activeState", EntityState.ACTIVE);
		List<Event> masterInspectionsWithSubEventToMove = persistenceManager.passThroughFindAll(query, parameters);

		for (Event masterEvent : masterInspectionsWithSubEventToMove) {
			updateSubInspectionAssets(winningAsset, losingAsset, masterEvent);
			updateInspection(masterEvent);
		}
	}

	private void updateInspection(Event event) {
		try {
			eventManager.updateEvent(event, user.getId(), null, null);
		} catch (Exception e) {
			throw new ProcessFailureException("could not update inspections to new asset", e);
		}
	}

	private void updateSubInspectionAssets(Asset winningAsset, Asset losingAsset, Event masterEvent) {
		for (SubEvent subEvent : masterEvent.getSubEvents()) {
			if (subEvent.getAsset().equals(losingAsset)) {
				subEvent.setAsset(winningAsset);
			}
		}
	}
}
