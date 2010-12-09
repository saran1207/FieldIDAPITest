package com.n4systems.fieldid.actions.event;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.fieldid.actions.helpers.MasterEvent;
import com.n4systems.fieldid.actions.helpers.SubAssetHelper;
import com.n4systems.fieldid.actions.event.viewmodel.WebEventScheduleToEventScheduleBundleConverter;
import com.n4systems.fieldid.utils.CopyEventFactory;
import com.n4systems.handlers.creator.EventPersistenceFactory;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.SubAsset;
import com.n4systems.model.SubEvent;
import com.n4systems.model.utils.FindSubAssets;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.ejb.parameters.CreateEventParameterBuilder;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.EventScheduleSuggestion;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.handlers.creator.events.factory.ProductionEventPersistenceFactory;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;


public class MasterEventCrud extends AbstractCrud {
	protected static final String SESSION_KEY = "masterEvent";
	private static Logger logger = Logger.getLogger(MasterEventCrud.class);
	private static final long serialVersionUID = 1L;

	private EventManager eventManager;
	private EventScheduleManager eventScheduleManager;
	private final EventPersistenceFactory eventPersistenceFactory;

	private Event event;
	private EventGroup eventGroup;
	private Asset asset;
	private List<SubAssetHelper> subAssets;

	private MasterEvent masterEvent;
	private String token;
	private boolean dirtySession = true;

	private boolean cleanToEventsToMatchConfiguration = false;
	

	public MasterEventCrud(PersistenceManager persistenceManager, EventManager eventManager, EventScheduleManager eventScheduleManager) {
		super(persistenceManager);
		this.eventManager = eventManager;
		this.eventScheduleManager = eventScheduleManager;
		this.eventPersistenceFactory = new ProductionEventPersistenceFactory();
	}

	@Override
	protected void initMemberFields() {
		masterEvent = (MasterEvent) getSession().get(SESSION_KEY);

		if (masterEvent == null || token == null) {
			createNewMasterEvent();
		} else if (!MasterEvent.matchingMasterEvent(masterEvent, token)) {
			masterEvent = null;
			return;
		}

		if (eventGroup == null) {
			setEventGroupId(masterEvent.getEventGroupId());
		}
		event = masterEvent.getEvent();
	}

	private void createNewMasterEvent() {
		masterEvent = new MasterEvent();
		masterEvent.setEvent(new Event());
		token = masterEvent.getToken();
		masterEvent.getEvent().setAsset(asset);
		getSession().put("masterEvent", masterEvent);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		masterEvent = (MasterEvent) getSession().get(SESSION_KEY);

		if (masterEvent == null || token == null || !MasterEvent.matchingMasterEvent(masterEvent, token)) {
			Event event = eventManager.findAllFields(uniqueId, getSecurityFilter());
			masterEvent = new MasterEvent(event);
			if (event != null) {
				for (SubEvent i : event.getSubEvents()) {
					persistenceManager.reattchAndFetch(i, "asset.id", "results", "infoOptionMap", "type", "attachments");
				}
			}
		}

		if (masterEvent != null) {
			event = masterEvent.getEvent();
			token = masterEvent.getToken();
			setAssetId(masterEvent.getEvent().getAsset().getId());
			getSession().put("masterEvent", masterEvent);
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
	public String doAdd() {

		if (masterEvent == null) {
			addActionError(getText("error.nomasterevent"));
			return MISSING;
		}

		if (asset == null) {
			if (masterEvent.getEvent().getAsset() == null) {
				addActionError(getText("error.noasset"));
				return MISSING;
			} else {
				asset = masterEvent.getEvent().getAsset();
			}
		}

		if (masterEvent.getEvent() == null) {
			addActionError(getText("error.noevent"));
			return ERROR;
		}
		if (masterEvent.getEvent().getType() == null) {
			addActionError(getText("error.noinpsectiontype"));
			return MISSING;
		}

		event = masterEvent.getEvent();

		if (eventGroup != null) {
			masterEvent.setEventGroupId(eventGroup.getId());
		}

		if (masterEvent.getSchedule() != null) {
			masterEvent.getSchedule().inProgress();
			try {
				persistenceManager.update(masterEvent.getSchedule(), getSessionUser().getId());
				addActionMessageText("message.scheduleinprogress");
			} catch (Exception e) {
				logger.warn("could not move schedule to in progress", e);
			}
		}

		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditEvent})
	public String doEdit() {

		if (masterEvent == null) {
			addActionError(getText("error.nomasterevent"));
			return MISSING;
		}

		if (asset == null) {
			if (masterEvent.getEvent().getAsset() == null) {
				addActionError(getText("error.noasset"));
				return MISSING;
			} else {
				asset = masterEvent.getEvent().getAsset();
			}
		}

		if (masterEvent.getEvent() == null) {
			addActionError(getText("error.noevent"));
			return MISSING;
		}

		return SUCCESS;
	}

	
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
	public String doCreate() {
		return save();
	}
	
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditEvent})
	public String doUpdate() {
		return save();
	}
	
	
	@Validations(requiredFields = { @RequiredFieldValidator(message = "", key = "error.mastereventnotcomplete", fieldName = "eventComplete") })
	private String save() {

		if (masterEvent == null) {
			return ERROR;
		}

		setEventGroupId(masterEvent.getEventGroupId());
		event.setGroup(eventGroup);

		try {
			if (uniqueID == null) {
				if (cleanToEventsToMatchConfiguration) {
					masterEvent.cleanSubEventsForNonValidSubAssets(asset);
				}
				Event master = CopyEventFactory.copyEvent(masterEvent.getCompletedEvent());
				
				
				CreateEventParameterBuilder createInspecitonBuiler = new CreateEventParameterBuilder(master, getSessionUserId())
						.withProofTestFile(masterEvent.getProofTestFile())
						.withUploadedImages(masterEvent.getUploadedFiles());
				
				
				
				createInspecitonBuiler.addSchedules(createEventScheduleBundles(masterEvent.getNextSchedules()));
				
				event = eventPersistenceFactory.createEventCreator().create(
						createInspecitonBuiler.build());
				uniqueID = event.getId();
			} else {
				Event master = CopyEventFactory.copyEvent(masterEvent.getCompletedEvent());
				event = eventManager.updateEvent(master, getSessionUser().getUniqueID(), masterEvent.getProofTestFile(), masterEvent.getUploadedFiles());
			}

			completeSchedule(masterEvent.getScheduleId(), masterEvent.getSchedule());

			for (int i = 0; i < masterEvent.getSubEvents().size(); i++) {
				SubEvent subEvent = new SubEvent();
				subEvent.setName("unknown");
				SubEvent uploadedFileKey = masterEvent.getSubEvents().get(i);
				try {
					subEvent = event.getSubEvents().get(i);

					event = eventManager.attachFilesToSubEvent(event, subEvent, masterEvent.getSubEventUploadedFiles().get(uploadedFileKey));

				} catch (Exception e) {
					addFlashError(getText("error.subeventfileupload", subEvent.getName()));
					logger.error("failed to attach uploaded files to sub asset", e);
				}
			}

			getSession().remove(SESSION_KEY);
			addFlashMessageText("message.mastereventsaved");
			return SUCCESS;

		} catch (ProcessingProofTestException e) {
			addActionError(getText("error.processingprooftest"));

			return INPUT;
		} catch (UnknownSubAsset e) {
			cleanToEventsToMatchConfiguration = true;
			addActionError(getText("error.assetconfigurationchanged"));
			return INPUT;
		} catch (FileAttachmentException e) {
			addActionError(getText("error.attachingfile"));
			return INPUT;
		} catch (Exception e) {
			addActionError(getText("error.eventsavefailed"));
			logger.error("event save failed serial number " + asset.getSerialNumber(), e);
			return ERROR;
		}
	}

	
	
	protected List<EventScheduleBundle> createEventScheduleBundles(List<WebEventSchedule> nextSchedules) {
		List<EventScheduleBundle> scheduleBundles = new ArrayList<EventScheduleBundle>();
		StrutsListHelper.clearNulls(nextSchedules);
		
		WebEventScheduleToEventScheduleBundleConverter converter = createWebEventScheduleToEventScheduleBundleConverter();
		
		for (WebEventSchedule nextSchedule : nextSchedules) {
			EventScheduleBundle bundle = converter.convert(nextSchedule, asset);
			scheduleBundles.add(bundle );
		}
	
		
		return scheduleBundles;
	}

	private WebEventScheduleToEventScheduleBundleConverter createWebEventScheduleToEventScheduleBundleConverter() {
		WebEventScheduleToEventScheduleBundleConverter converter = new WebEventScheduleToEventScheduleBundleConverter(getLoaderFactory(), getSessionUser().createUserDateConverter());
		return converter;
	}
	
	
	private void completeSchedule(Long eventScheduleId, EventSchedule eventSchedule) {
		if (eventScheduleId != null) {

			if (eventScheduleId.equals(EventScheduleSuggestion.NEW_SCHEDULE)) {
				eventSchedule = new EventSchedule(event);
			} else if (eventSchedule != null) {
				eventSchedule.completed(event);
			}
			if (eventSchedule != null) {
				try {
					eventScheduleManager.update(eventSchedule);
					addFlashMessageText("message.schedulecompleted");
				} catch (Exception e) {
					logger.error("could not complete the schedule", e);
					addFlashErrorText("error.completingschedule");
				}
			}
		}
	}

	public Long getAssetId() {
		return (asset != null) ? asset.getId() : null;
	}

	public void setAssetId(Long assetId) {
		if (assetId == null) {
			asset = null;

		} else if (asset == null || !asset.getId().equals(assetId)) {
			asset = persistenceManager.find(Asset.class, assetId, getSecurityFilter(), "type.subTypes");
			asset = new FindSubAssets(persistenceManager, asset).fillInSubAssets();
			if (asset != null) {
				for (SubAsset subAsset : asset.getSubAssets()) {
					persistenceManager.reattchAndFetch(subAsset.getAsset().getType(), "eventTypes");
				}
			}

		}
		
		if (masterEvent != null) {
			masterEvent.setMasterAsset(asset);
		}
	}

	public Long getEventGroupId() {
		return (eventGroup != null) ? eventGroup.getId() : null;
	}

	public void setEventGroupId(Long eventGroupId) {
		if (eventGroupId == null) {
			eventGroup = null;
		} else if (eventGroup == null || eventGroupId.equals(eventGroup.getId())) {
			eventGroup = persistenceManager.find(EventGroup.class, eventGroupId, getTenantId());
		}
	}

	public Long getType() {
		return (event != null && event.getType() != null) ? event.getType().getId() : null;
	}

	public void setType(Long type) {
		if (dirtySession) {
			event.setType(null);
		}
		if (type == null) {
			event.setType(null);
		} else if (event.getType() == null || !type.equals(event.getType())) {
			event.setType(persistenceManager.find(EventType.class, type, getTenantId()));
		}
	}

	public EventType getEventType() {
		return event.getType();
	}

	// validate this to be sure we have
	public MasterEvent getMasterEvent() {
		return masterEvent;
	}

	public List<SubEvent> getEventsFor(Asset asset) {
		return masterEvent.getSubEventFor(asset);
	}
	
	public String getNameFor(Asset asset) {
		List<SubEvent> subEvents = getEventsFor(asset);
		String result = null;
		if (!subEvents.isEmpty()) {
			result = subEvents.iterator().next().getName();
		}
		
		return result;
	}

	public Event getEvent() {
		return event;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isComplete() {
		return (masterEvent != null && masterEvent.isMainEventStored());
	}

	public Object getEventComplete() {
		if (isComplete()) {
			return new Object();
		}
		return null;
	}

	public boolean isCleanToEventsToMatchConfiguration() {
		return cleanToEventsToMatchConfiguration;
	}

	public void setCleanToEventsToMatchConfiguration(boolean cleanToEventsToMatchConfiguration) {
		this.cleanToEventsToMatchConfiguration = cleanToEventsToMatchConfiguration;
	}

	public boolean isMasterEvent(Long id) {
		return eventManager.isMasterEvent(id);
	}

	public List<Asset> getAvailableSubAssets() {
		List<Asset> availableSubAssets = new ArrayList<Asset>();
		for (SubEvent subEvent : event.getSubEvents()) {
			if (!availableSubAssets.contains(subEvent.getAsset())) {
				availableSubAssets.add(subEvent.getAsset());
			}
		}
		return availableSubAssets;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setSubAssets(List<SubAssetHelper> subAssets) {
		this.subAssets = subAssets;
	}

	public List<SubAssetHelper> getSubAssets() {
		if (subAssets == null) {
			subAssets = new ArrayList<SubAssetHelper>();
		}
		return subAssets;
	}

	public List<AssetType> getSubTypes() {
		return new ArrayList<AssetType>(asset.getType().getSubTypes());
	}

	public void setScheduleId(Long scheduleId) {
		if (masterEvent != null) {
			masterEvent.setScheduleId(scheduleId);
		}
	}
	
	public Long getScheduleId() {
		return (masterEvent != null) ? masterEvent.getScheduleId() : null;
	}
	
	public Long getEventId() {
		return masterEvent.getEvent().getId();
	}
	
}
