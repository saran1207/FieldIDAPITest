package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.ejb.parameters.CreateEventParameterBuilder;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.event.viewmodel.WebEventScheduleToEventScheduleBundleConverter;
import com.n4systems.fieldid.actions.helpers.MasterEvent;
import com.n4systems.fieldid.actions.helpers.SubAssetHelper;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.CopyEventFactory;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.handlers.creator.EventPersistenceFactory;
import com.n4systems.handlers.creator.events.factory.ProductionEventPersistenceFactory;
import com.n4systems.model.*;
import com.n4systems.model.utils.FindSubAssets;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.ArrayList;
import java.util.List;


public class MasterEventCrud extends AbstractCrud {
	protected static final String SESSION_KEY = "masterEvent";
	private static Logger logger = Logger.getLogger(MasterEventCrud.class);
	private static final long serialVersionUID = 1L;

	private EventManager eventManager;
	private EventScheduleManager eventScheduleManager;
	private final EventPersistenceFactory eventPersistenceFactory;

	private ThingEvent event;
	private Asset asset;
	private List<SubAssetHelper> subAssets;

	private MasterEvent masterEvent;
	private String token;
	private boolean dirtySession = true;

	private boolean cleanToEventsToMatchConfiguration = false;

    private Long scheduleId;
	

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
		event = masterEvent.getEvent();
	}

	private void createNewMasterEvent() {
		masterEvent = new MasterEvent();
        if (scheduleId != null) {
            ThingEvent openEvent = persistenceManager.find(ThingEvent.class, scheduleId, getTenant(), "asset", "eventForm.sections", "results", "attachments", "infoOptionMap", "type.supportedProofTests", "type.infoFieldNames", "subEvents", "type.eventForm.sections");
            masterEvent.setEvent(openEvent);
        } else {
            masterEvent.setEvent(new ThingEvent());
        }
		token = masterEvent.getToken();
		masterEvent.getEvent().setAsset(asset);
		getSession().put("masterEvent", masterEvent);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		masterEvent = (MasterEvent) getSession().get(SESSION_KEY);

		if (masterEvent == null || token == null || !MasterEvent.matchingMasterEvent(masterEvent, token)) {
			ThingEvent event = eventManager.findAllFields(uniqueId, getSecurityFilter());
			masterEvent = new MasterEvent(event);
			if (event != null) {
				for (SubEvent subEvent : event.getSubEvents()) {
					persistenceManager.reattchAndFetch(subEvent, "asset.id", "results", "infoOptionMap", "type", "attachments", "eventForm");
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

		try {
			if (uniqueID == null) {
				if (masterEvent.isCleanToEventsToMatchConfiguration()) {
					masterEvent.cleanSubEventsForNonValidSubAssets(asset);
				}
                ThingEvent master = CopyEventFactory.copyEvent(masterEvent.getCompletedEvent());
				
				
				CreateEventParameterBuilder createEventBuilder = new CreateEventParameterBuilder(master, getSessionUserId())
						.withProofTestFile(masterEvent.getProofTestFile())
						.withUploadedImages(masterEvent.getUploadedFiles());

				createEventBuilder.addSchedules(createEventScheduleBundles(masterEvent.getNextSchedules()));
				
				event = eventPersistenceFactory.createEventCreator().create(
                        createEventBuilder.build());
				uniqueID = event.getId();
			} else {
                ThingEvent master = CopyEventFactory.copyEvent(masterEvent.getCompletedEvent());
				event = eventManager.updateEvent(master, scheduleId, getSessionUser().getUniqueID(), masterEvent.getProofTestFile(), masterEvent.getUploadedFiles());
			}

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
			logger.error("event save failed identifier " + asset.getIdentifier(), e);
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
	
	
//	private void completeSchedule(Long eventScheduleId, EventSchedule eventSchedule) {
//		if (eventScheduleId != null) {
//
//			if (eventScheduleId.equals(EventScheduleSuggestion.NEW_SCHEDULE)) {
//				eventSchedule = new EventSchedule(event);
//			} else if (eventSchedule != null) {
//				eventSchedule.completed(event);
//			}
//			if (eventSchedule != null) {
//				try {
//					eventScheduleManager.update(event);
//					addFlashMessageText("message.schedulecompleted");
//				} catch (Exception e) {
//					logger.error("could not complete the schedule", e);
//					addFlashErrorText("error.completingschedule");
//				}
//			}
//		}
//	}

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
			event.setType(persistenceManager.find(ThingEventType.class, type, getTenantId()));
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
        this.scheduleId = scheduleId;
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

    @Override
    public String getIEHeader() {
        return "EmulateIE7";
    }

}
