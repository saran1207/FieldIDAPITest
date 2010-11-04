package com.n4systems.fieldid.actions.asset;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.handlers.remover.EventFrequenciesDeleteHandlerImpl;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.inspectiontype.EventFrequencySaver;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.handlers.remover.EventFrequenciesDeleteHandler;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.inspectiontype.AssociatedEventTypeSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.util.persistence.QueryBuilder;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class AssociatedEventTypeCrud extends AbstractCrud {

	private static Logger logger = Logger.getLogger(AssociatedEventTypeCrud.class);
	private static final long serialVersionUID = 1L;

	private AssetType assetType;
	private List<EventType> eventTypes;
	private List<Boolean> assetTypeEvents;

	public AssociatedEventTypeCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}

	public String doList() {
		return SUCCESS;
	}

	public String doSave() {
		
		Transaction transaction = com.n4systems.persistence.PersistenceManager.startTransaction();
		List<EventType> selectedEventTypes = findEventsTypesSet();
		List<AssociatedEventType> types = getLoaderFactory().createAssociatedEventTypesLoader().setAssetType(assetType).load(transaction);
		
		try {
			
			List<AssociatedEventType> toBeAdded = findEventTypesToAdd(selectedEventTypes, types);
			List<AssociatedEventType> toBeRemoved = findEventTypesToRemoved(selectedEventTypes, types);
			AssociatedEventTypeSaver saver = new AssociatedEventTypeSaver();
			
			EventFrequenciesDeleteHandler frequenciesDeleteHandler = new EventFrequenciesDeleteHandlerImpl(getLoaderFactory().createEventFrequenciesListLoader(), new EventFrequencySaver());
			
			for (AssociatedEventType associatedEventType : toBeRemoved) {
				saver.remove(transaction, associatedEventType);
				frequenciesDeleteHandler.forAssociatedEventType(associatedEventType).remove(transaction);
			}
			
			for (AssociatedEventType associatedEventType : toBeAdded) {
				saver.save(transaction, associatedEventType);
			}

			
			transaction.commit();
			
			addFlashMessageText("message.eventtypesselected");
		} catch (Exception e) {
			addActionErrorText("error.failedtosaveeventtypeselection");
			logger.error("failed to change the event type selection", e);
			transaction.rollback();
			
			return ERROR;
		}

		return SUCCESS;
	}

	private List<AssociatedEventType> findEventTypesToAdd(List<EventType> selectedEventTypes, List<AssociatedEventType> types) {
		List<AssociatedEventType> toBeAdded = new ArrayList<AssociatedEventType>();
		for (EventType selectedEventType : selectedEventTypes) {
			boolean found = false;
			for (AssociatedEventType associatedEventType : types) {
				if (associatedEventType.getEventType().equals(selectedEventType)) {
					found = true;
				}
			}
			if (!found) {
				toBeAdded.add(new AssociatedEventType(selectedEventType, assetType));
			}
		}
		return toBeAdded;
	}
	
	private List<AssociatedEventType> findEventTypesToRemoved(List<EventType> selectedEventTypes, List<AssociatedEventType> types) {
		List<AssociatedEventType> toBeRemoved = new ArrayList<AssociatedEventType>();
		for (AssociatedEventType associatedEventType : types) {
			boolean found = false;
			
			for (EventType selectedEventType : selectedEventTypes) {
				if (associatedEventType.getEventType().equals(selectedEventType)) {
					found = true;
				}
			}
			if (!found) {
				toBeRemoved.add(associatedEventType);
			}
		}
		return toBeRemoved;
	}
	

	private List<EventType> findEventsTypesSet() {
		List<EventType> selectedEventTypes = new ArrayList<EventType>();

		for (EventType eventType : getEventTypes()) {
			if (assetTypeEvents.get(getEventTypes().indexOf(eventType))) {
				selectedEventTypes.add(eventType);
			}
		}

		return selectedEventTypes;
	}

	/**
	 * @return the productType
	 */
	public Long getAssetTypeId() {
		return (assetType != null) ? assetType.getId() : null;
	}

	/**
	 * @param assetType
	 *            the productType to set
	 */
	public void setAssetTypeId(Long assetType) {
		if (assetType == null) {
			this.assetType = null;
		} else if (this.assetType == null || assetType.equals(this.assetType.getId())) {
			this.assetType = persistenceManager.find(new QueryBuilder<AssetType>(AssetType.class, getSecurityFilter())
														.addSimpleWhere("id", assetType));
		}
		
	}

	public AssetType getAssetType() {
		return assetType;
	}

	/**
	 * @return the productTypeEvents
	 */
	public List<Boolean> getAssetTypeEventTypes() {
		if (assetTypeEvents == null) {
			assetTypeEvents = new ArrayList<Boolean>();
			for (EventType eventType : getEventTypes()) {
				boolean found = false;
				for (AssociatedEventType associatedEventType : associatedEventTypes()) {
					if (eventType.equals(associatedEventType.getEventType())) {
						assetTypeEvents.add(true);
						found = true;
						break;
					}
				}
				if (!found) {
					assetTypeEvents.add(false);
				}
			}
		}
		return assetTypeEvents;
	}

	private List<AssociatedEventType> associatedEventTypes() {
		return getLoaderFactory().createAssociatedEventTypesLoader().setAssetType(assetType).load();
	}

	public void setAssetTypeEventTypes(List<Boolean> assetTypeEvents) {
		this.assetTypeEvents = assetTypeEvents;
	}

	/**
	 * @return the eventTypes
	 */
	public List<EventType> getEventTypes() {
		if (eventTypes == null) {
			QueryBuilder<EventType> queryBuilder = new QueryBuilder<EventType>(EventType.class, getSecurityFilter());
			queryBuilder.addSimpleWhere("state", EntityState.ACTIVE);
			queryBuilder.addOrder("name");
			eventTypes = persistenceManager.findAll(queryBuilder);
		}
		return eventTypes;
	}

}
