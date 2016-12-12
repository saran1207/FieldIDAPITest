package com.n4systems.services.asset;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.TenantNotValidForActionException;
import com.n4systems.exceptions.UsedOnMasterEventException;
import com.n4systems.exceptions.asset.AssetTypeMissMatchException;
import com.n4systems.exceptions.asset.DuplicateAssetException;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.SubEvent;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.EventScheduleService;
import com.n4systems.services.EventScheduleServiceImpl;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		moveEvents(winningAsset, losingAsset);
		moveSubEvents(winningAsset, losingAsset);
		archiveLosingAsset(losingAsset);
		updateLastEventDate(winningAsset, losingAsset);

		return winningAsset;
	}

	private void updateLastEventDate(Asset winningAsset, Asset losingAsset) {
		if (winningAsset.getLastEventDate() == null && losingAsset.getLastEventDate() != null) {
			winningAsset.setLastEventDate(losingAsset.getLastEventDate());
		} else if (winningAsset.getLastEventDate() != null && losingAsset.getLastEventDate() != null) {
			if (winningAsset.getLastEventDate().before(losingAsset.getLastEventDate())) {
				winningAsset.setLastEventDate(losingAsset.getLastEventDate());
			}
		}
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
			List<SubAsset> subAssets = assetManager.findSubAssetsForAsset(losingAsset);
					
			assetManager.archive(losingAsset, user);
			
			if(subAssets != null) {
				for(SubAsset subAsset: subAssets) {
					assetManager.archive(subAsset.getAsset(), user);
				}
			}
		} catch (UsedOnMasterEventException e) {
			throw new ProcessFailureException("could not archive the asset. still on a master", e);
		}
	}

	private void moveEvents(Asset winningAsset, Asset losingAsset) {
		QueryBuilder<ThingEvent> eventsQuery = new QueryBuilder<ThingEvent>(ThingEvent.class, new OpenSecurityFilter()).addSimpleWhere("state", EntityState.ACTIVE).addSimpleWhere("asset", losingAsset);
		List<ThingEvent> eventsToMove = persistenceManager.findAll(eventsQuery);

		for (ThingEvent eventToMove : eventsToMove) {
			eventToMove.setAsset(winningAsset);
			updateEvent(eventToMove);
		}
	}

	private void moveSubEvents(Asset winningAsset, Asset losingAsset) {
		String query = "SELECT DISTINCT master from " + ThingEvent.class.getName() + " master, IN (master.subEvents) subEvent "
				+ "where subEvent.asset = :losingAsset AND master.state = :activeState";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("losingAsset", losingAsset);
		parameters.put("activeState", EntityState.ACTIVE);
		List<ThingEvent> masterEventsWithSubEventToMove = persistenceManager.passThroughFindAll(query, parameters);

		for (ThingEvent masterEvent : masterEventsWithSubEventToMove) {
			updateSubEventAssets(winningAsset, losingAsset, masterEvent);
			updateEvent(masterEvent);
		}
	}

	private void updateEvent(ThingEvent event) {
		try {
			eventManager.updateEvent(event, 0L, user.getId(), null, null);
		} catch (Exception e) {
			throw new ProcessFailureException("could not update events to new asset", e);
		}
	}

	private void updateSubEventAssets(Asset winningAsset, Asset losingAsset, ThingEvent masterEvent) {
		for (SubEvent subEvent : masterEvent.getSubEvents()) {
			if (subEvent.getAsset().equals(losingAsset)) {
				subEvent.setAsset(winningAsset);
			}
		}
	}
}
