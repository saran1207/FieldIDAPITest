package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.fieldid.actions.asset.helpers.AssetLinkedHelper;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.services.EventScheduleServiceImpl;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Asset;
import com.n4systems.model.Project;
import com.n4systems.model.utils.FindSubAssets;
import com.n4systems.security.Permissions;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
public class EventScheduleCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(EventScheduleCrud.class);

	private LegacyAsset legacyProductManager;
	private EventScheduleManager eventScheduleManager;
	protected EventSchedule eventSchedule;
	
	private EventType eventType;
	private List<ListingPair> jobs;
	private Asset asset;
	private String nextDate;

	protected String searchId;

	private List<EventSchedule> eventSchedules;

	public EventScheduleCrud(LegacyAsset legacyProductManager, PersistenceManager persistenceManager, EventScheduleManager eventScheduleManager) {
		super(persistenceManager);
		this.legacyProductManager = legacyProductManager;
		this.eventScheduleManager = eventScheduleManager;
	}

	@Override
	protected void initMemberFields() {
		eventSchedule = new EventSchedule();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		eventSchedule = persistenceManager.find(EventSchedule.class, uniqueId, getTenantId());
	}

	private void testRequiredEntities(boolean existing) {
		testRequiredEntities(existing, false);
	}
	
	protected void testRequiredEntities(boolean existing, boolean eventTypeRequired) {
		if (eventSchedule == null) {
			addActionErrorText("error.noschedule");
			throw new MissingEntityException();
		} else if (existing && eventSchedule.isNew()) {
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
			Project tmpProject = eventSchedule.getProject();
			eventSchedule = new EventSchedule(asset, eventType);
			eventSchedule.setNextDate(convertDate(nextDate));
			eventSchedule.setProject(tmpProject);
			
			uniqueID = new EventScheduleServiceImpl(persistenceManager).createSchedule(eventSchedule);
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

	
	
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent, Permissions.ManageJobs})
	public String doSave() {
		testRequiredEntities(true);
		try {
			eventSchedule.setNextDate(convertDate(nextDate));
			new EventScheduleServiceImpl(persistenceManager).updateSchedule(eventSchedule);
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
			persistenceManager.delete(eventSchedule);
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
		testRequiredEntities(true, false);
		try {
			eventSchedule.stopProgress();
			persistenceManager.update(eventSchedule, getSessionUser().getId());
			addActionMessageText("message.eventscheduleprogressstoped");
		} catch (Exception e) {
			logger.error("could not stop progress on the schedule", e);
			addActionErrorText("error.stopingprogresseventschedule");
			return ERROR;
		}

		return SUCCESS;
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
			for (EventType insType : getEventTypes()) {
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

	public List<EventType> getEventTypes() {
		List<EventType> eventTypes = new ArrayList<EventType>();
		List<AssociatedEventType> associatedEventTypes = getLoaderFactory().createAssociatedEventTypesLoader().setAssetType(asset.getType()).load();
		for (AssociatedEventType associatedEventType : associatedEventTypes) {
			eventTypes.add(associatedEventType.getEventType());
		}
		return eventTypes;
		
	}

	public String getNextDate() {
		if (nextDate == null) {
			nextDate = convertDate(eventSchedule.getNextDate());
		}
		return nextDate;
	}

	
	@RequiredStringValidator(type = ValidatorType.SIMPLE, fieldName = "nextDate", message = "", key = "error.mustbeadate")
	@CustomValidator(type = "n4systemsDateValidator", fieldName = "nextDate", message = "", key = "error.mustbeadate")
	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}

	public List<EventSchedule> getEventSchedules() {
		if (eventSchedules == null) {
			eventSchedules = eventScheduleManager.getAvailableSchedulesFor(asset);
		}
		return eventSchedules;
	}

	public Long getEventCount() {
		return legacyProductManager.countAllEvents(asset, getSecurityFilter());
	}

	public EventSchedule getEventSchedule() {
		return eventSchedule;
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
		return (eventSchedule.getProject() != null) ? eventSchedule.getProject().getId() : null;
	}

	public void setProject(Long project) {
		if (project == null) {
			eventSchedule.setProject(null);
		} else if (eventSchedule.getProject() == null || !project.equals(eventSchedule.getAsset().getId())) {
			eventSchedule.setProject(persistenceManager.find(Project.class, project, getTenantId()));
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
