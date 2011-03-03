package com.n4systems.fieldid.actions.asset;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.handlers.remover.EventFrequenciesDeleteHandlerImpl;
import com.n4systems.handlers.remover.ScheduleListDeleteHandler;
import com.n4systems.handlers.remover.ScheduleListDeleteHandlerImpl;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.eventtype.EventFrequencySaver;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.handlers.remover.EventFrequenciesDeleteHandler;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.eventtype.AssociatedEventTypeSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.util.persistence.QueryBuilder;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class AssociatedEventTypeCrud extends AbstractCrud {

	private static Logger logger = Logger.getLogger(AssociatedEventTypeCrud.class);
	private static final long serialVersionUID = 1L;

	private AssetType assetType;
	private EventType eventType;
	private List<EventType> eventTypes;
	private List<Boolean> assetTypeEvents;
	private List<Boolean> eventTypeAssets;
	private List<AssetType> assetTypes;

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
			ScheduleListDeleteHandlerImpl scheduleDeleteHandler = new ScheduleListDeleteHandlerImpl();
			
			for (AssociatedEventType associatedEventType : toBeRemoved) {
				saver.remove(transaction, associatedEventType);
				frequenciesDeleteHandler.forAssociatedEventType(associatedEventType).remove(transaction);
				scheduleDeleteHandler.setAssociatedEventType(associatedEventType).remove(transaction);
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
	
	public String doSaveAssetTypes(){
		Transaction transaction = com.n4systems.persistence.PersistenceManager.startTransaction();
		List<AssetType> selectedAssetTypes = findAssetTypesSet();
		List<AssociatedEventType> types = getLoaderFactory().createAssociatedEventTypesLoader().setEventType(eventType).load(transaction);
	
		try {
		
			List<AssociatedEventType> toBeAdded = findAssetTypesToAdd(selectedAssetTypes, types);
			List<AssociatedEventType> toBeRemoved = findAssetTypesToRemove(selectedAssetTypes, types);
			AssociatedEventTypeSaver saver = new AssociatedEventTypeSaver();
			
			EventFrequenciesDeleteHandler frequenciesDeleteHandler = new EventFrequenciesDeleteHandlerImpl(getLoaderFactory().createEventFrequenciesListLoader(), new EventFrequencySaver());
			ScheduleListDeleteHandlerImpl scheduleDeleteHandler = new ScheduleListDeleteHandlerImpl();
			
			for (AssociatedEventType associatedEventType : toBeRemoved) {
				saver.remove(transaction, associatedEventType);
				frequenciesDeleteHandler.forAssociatedEventType(associatedEventType).remove(transaction);
				scheduleDeleteHandler.setAssociatedEventType(associatedEventType).remove(transaction);
			}
			
			for (AssociatedEventType associatedEventType : toBeAdded) {
				saver.save(transaction, associatedEventType);
			}
			
			transaction.commit();
			
			addFlashMessageText("message.assettypesselected");
		} catch (Exception e) {
			addActionErrorText("error.failedtosaveassettypeselection");
			logger.error("failed to change the asset type selection", e);
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
	
	private List<AssociatedEventType> findAssetTypesToAdd(List<AssetType> selectedAssetTypes, List<AssociatedEventType> types) {
		List<AssociatedEventType> toBeAdded = new ArrayList<AssociatedEventType>();
		for (AssetType selectedAssetType : selectedAssetTypes) {
			boolean found = false;
			for (AssociatedEventType associatedEventType : types) {
				if (associatedEventType.getAssetType().equals(selectedAssetType)) {
					found = true;
				}
			}
			if (!found) {
				toBeAdded.add(new AssociatedEventType( eventType,selectedAssetType));
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

	private List<AssociatedEventType> findAssetTypesToRemove(List<AssetType> selectedAssetTypes, List<AssociatedEventType> types) {
		List<AssociatedEventType> toBeRemoved = new ArrayList<AssociatedEventType>();
		for (AssociatedEventType associatedEventType : types) {
			boolean found = false;
			
			for (AssetType selectedAssetType : selectedAssetTypes) {
				if (associatedEventType.getAssetType().equals(selectedAssetType)) {
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
	
	private List<AssetType> findAssetTypesSet() {
		List<AssetType> selectedAssetTypes = new ArrayList<AssetType>();

		for (AssetType assetType : getAssetTypes()) {
			if (eventTypeAssets.get(getAssetTypes().indexOf(assetType))) {
				selectedAssetTypes.add(assetType);
			}
		}

		return selectedAssetTypes;
	}

	/**
	 * @return the assetType
	 */
	public Long getAssetTypeId() {
		return (assetType != null) ? assetType.getId() : null;
	}
	
	/**
	 * @return the eventType
	 */
	public Long getEventTypeId() {
		return (eventType != null) ? eventType.getId() : null;
	}

	/**
	 * @param assetType
	 *            the assetType to set
	 */
	public void setAssetTypeId(Long assetType) {
		if (assetType == null) {
			this.assetType = null;
		} else if (this.assetType == null || assetType.equals(this.assetType.getId())) {
			this.assetType = persistenceManager.find(new QueryBuilder<AssetType>(AssetType.class, getSecurityFilter())
														.addSimpleWhere("id", assetType));
		}
	}
	
	/**
	 * @param assetType
	 *            the assetType to set
	 */
	public void setEventTypeId(Long eventType) {
		if (eventType == null) {
			this.eventType = null;
		} else if (this.eventType == null || eventType.equals(this.eventType.getId())) {
			this.eventType = persistenceManager.find(new QueryBuilder<EventType>(EventType.class, getSecurityFilter())
														.addSimpleWhere("id", eventType));
		}
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public EventType getEventType() {
		return eventType;
	}
	
	/**
	 * @return the assetTypeEvents
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

	/**
	 * @return the eventTypeAssets
	 */
	public List<Boolean> getEventTypeAssetTypes(){
		if (eventTypeAssets == null){
			eventTypeAssets = new ArrayList<Boolean>();
			for (AssetType assetType : getAssetTypes()){
				boolean found=false;
				for(AssociatedEventType associatedAssetType : associatedAssetTypes()){
					if(assetType.equals(associatedAssetType.getAssetType())){
						eventTypeAssets.add(true);
						found=true;
						break;
					}
				}
				if (!found){
					eventTypeAssets.add(false);
				}
			}
		}
		return eventTypeAssets;	
	}

	private List<AssociatedEventType> associatedEventTypes() {
		return getLoaderFactory().createAssociatedEventTypesLoader().setAssetType(assetType).load();
	}
	
	private List<AssociatedEventType> associatedAssetTypes(){
		return getLoaderFactory().createAssociatedEventTypesLoader().setEventType(eventType).load();
	}

	public void setAssetTypeEventTypes(List<Boolean> assetTypeEvents) {
		this.assetTypeEvents = assetTypeEvents;
	}
	
	public void setEventTypeAssetTypes(List<Boolean> eventTypeAssets){
		this.eventTypeAssets = eventTypeAssets;
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

	public List<AssetType> getAssetTypes(){
		if (assetTypes == null) {
			QueryBuilder<AssetType> queryBuilder = new QueryBuilder<AssetType>(AssetType.class, getSecurityFilter());
			queryBuilder.addSimpleWhere("state", EntityState.ACTIVE);
			queryBuilder.addOrder("name");
			assetTypes = persistenceManager.findAll(queryBuilder);
		}
		return assetTypes;
	}
	
}
