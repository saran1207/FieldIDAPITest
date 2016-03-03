package com.n4systems.fieldid.actions;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.asset.helpers.AssetLinkedHelper;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.*;
import com.n4systems.model.utils.FindSubAssets;
import com.n4systems.security.Permissions;
import com.n4systems.services.EventScheduleServiceImpl;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@UserPermissionFilter(userRequiresOneOf={Permissions.CREATE_EVENT})
public class EventScheduleCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(EventScheduleCrud.class);

	private LegacyAsset legacyAssetManager;
	private EventScheduleManager eventScheduleManager;
	protected ThingEvent openEvent;
	
	private ThingEventType eventType;
	private List<ListingPair> jobs;
	private Asset asset;
	private String nextDate;

	protected String searchId;

	private List<ThingEvent> eventSchedules;

	public EventScheduleCrud(LegacyAsset legacyAssetManager, PersistenceManager persistenceManager, EventScheduleManager eventScheduleManager) {
		super(persistenceManager);
		this.legacyAssetManager = legacyAssetManager;
		this.eventScheduleManager = eventScheduleManager;
	}

	@Override
	protected void initMemberFields() {
		openEvent = new ThingEvent();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		openEvent = persistenceManager.find(ThingEvent.class, uniqueId, getTenantId(), "asset");
	}

	private void testRequiredEntities(boolean existing) {
		testRequiredEntities(existing, false);
	}
	
	protected void testRequiredEntities(boolean existing, boolean eventTypeRequired) {
		if (openEvent == null) {
			addActionErrorText("error.noschedule");
			throw new MissingEntityException();
		} else if (existing && openEvent.isNew()) {
			addActionErrorText("error.noschedule");
			throw new MissingEntityException();
		}
		
		if (asset == null) {
			addActionErrorText("error.noasset");
			throw new MissingEntityException();
		}
		
		if (eventTypeRequired && eventType == null) {
			addActionErrorText("error.noeventtype");
			throw new MissingEntityException();
		} 
	}
	
	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		return INPUT;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	
	public String doCreate() {
		testRequiredEntities(false);
		try {
			Project tmpProject = openEvent.getProject();
			openEvent = new ThingEvent();
            openEvent.setAsset(asset);
            openEvent.setType(eventType);
			openEvent.setDueDate(convertDateWithOptionalTime(nextDate));
			openEvent.setProject(tmpProject);
            openEvent.setTenant(getTenant());
			
			uniqueID = new EventScheduleServiceImpl(persistenceManager).createSchedule(openEvent);
			addActionMessageText("message.eventschedulesaved");
		} catch (Exception e) {
			logger.error("could not save schedule", e);
			addActionErrorText("error.savingeventschedule");
			return ERROR;
			
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	
	
	@UserPermissionFilter(userRequiresOneOf={Permissions.CREATE_EVENT, Permissions.MANAGE_JOBS})
	public String doSave() {
		testRequiredEntities(true);
		try {
			openEvent.setDueDate(convertDate(nextDate));
			new EventScheduleServiceImpl(persistenceManager).updateSchedule(openEvent);
			addActionMessageText("message.eventschedulesaved");
		} catch (Exception e) {
			logger.error("could not save schedule", e);
			addActionErrorText("error.savingeventschedule");
			return ERROR;
			
		}
		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(open=true)
	public String doList() {
		setPageType("asset", "event_schedules");
		testRequiredEntities(false, false);
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		try {
			persistenceManager.delete(openEvent);
			addActionMessageText("message.eventscheduledeleted");
		} catch (Exception e) {
			logger.error("could not delete schedule", e);
			addActionErrorText("error.deletingeventschedule");
			return ERROR;
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String doStopProgress() {
        throw new InvalidOperationException("stopping progress - operation deprecated");
//		testRequiredEntities(true, false);
//		try {
//			eventSchedule.stopProgress();
//			persistenceManager.update(eventSchedule, getSessionUser().getId());
//			addActionMessageText("message.eventscheduleprogressstoped");
//		} catch (Exception e) {
//			logger.error("could not stop progress on the schedule", e);
//			addActionErrorText("error.stopingprogresseventschedule");
//			return ERROR;
//		}

//		return SUCCESS;
	}

	public Long getAssetId() {
		return (asset != null) ? asset.getId() : null;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAssetId(Long id) {
		if (asset == null || !asset.getId().equals(id)) {
			if (!isInVendorContext()) {
				asset = persistenceManager.find(Asset.class, id, getSecurityFilter(), "type.subTypes", "type.eventTypes");
				asset = new FindSubAssets(persistenceManager, asset).fillInSubAssets();
			} else {
				asset = getLoaderFactory().createSafetyNetworkAssetLoader().withAllFields().setAssetId(id).load();
			}
		}
	}

	public Long getType() {
		return (eventType != null) ? eventType.getId() : null;
	}

	public void setType(Long eventTypeId) {
		if (this.eventType == null || !this.eventType.getId().equals(eventTypeId)) {
			this.eventType = null;
			for (ThingEventType insType : getEventTypes()) {
				if (insType.getId().equals(eventTypeId)) {
					this.eventType = insType;
					break;
				}
			}
		}
	}

	public EventType getEventType() {
		return eventType;
	}

	public List<ThingEventType> getEventTypes() {
		List<ThingEventType> eventTypes = new ArrayList<ThingEventType>();
		List<AssociatedEventType> associatedEventTypes = getLoaderFactory().createAssociatedEventTypesLoader().setAssetType(asset.getType()).load();
		for (AssociatedEventType associatedEventType : associatedEventTypes) {
			eventTypes.add(associatedEventType.getEventType());
		}
		return eventTypes;
	}

	public String getNextDate() {
		if (nextDate == null) {
			nextDate = convertDateTime(openEvent.getDueDate());
		}
		return nextDate;
	}

	@CustomValidator(type = "n4systemsDateValidator", fieldName = "nextDate", message = "", key = "error.mustbeadate")
	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}

	public List<ThingEvent> getEventSchedules() {
		if (eventSchedules == null) {
			eventSchedules = eventScheduleManager.getAvailableSchedulesFor(asset);
		}
		return eventSchedules;
	}

	public Long getEventCount() {
		return legacyAssetManager.countAllEvents(asset, getSecurityFilter());
	}

	public Event getEventSchedule() {
		return openEvent;
	}

	public List<ListingPair> getJobs() {
		if (jobs == null) {
			QueryBuilder<ListingPair> query = new QueryBuilder<ListingPair>(Project.class, getSecurityFilter());
			query.addSimpleWhere("eventJob", true);
			query.addSimpleWhere("retired", false);
			jobs = persistenceManager.findAllLP(query, "name");
		}
		return jobs;
	}

	public Long getProject() {
		return (openEvent.getProject() != null) ? openEvent.getProject().getId() : null;
	}

	public void setProject(Long project) {
		if (project == null) {
			openEvent.setProject(null);
		} else if (openEvent.getProject() == null || !project.equals(openEvent.getAsset().getId())) {
			openEvent.setProject(persistenceManager.find(Project.class, project, getTenantId()));
		}
	}

	public boolean isLinked() {
		return AssetLinkedHelper.isLinked(asset, getLoaderFactory());
	}
	
	
	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

}
