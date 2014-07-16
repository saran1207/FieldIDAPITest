package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.fieldid.actions.event.viewmodel.CriteriaResultWebModel;
import com.n4systems.fieldid.actions.event.viewmodel.EventWebModel;
import com.n4systems.fieldid.actions.event.viewmodel.ScheduleToWebEventScheduleConverter;
import com.n4systems.fieldid.actions.event.viewmodel.WebEventScheduleToEventScheduleBundleConverter;
import com.n4systems.fieldid.actions.exceptions.PersistenceException;
import com.n4systems.fieldid.actions.exceptions.ValidationException;
import com.n4systems.fieldid.actions.helpers.AssignedToUserGrouper;
import com.n4systems.fieldid.actions.helpers.EventCrudHelper;
import com.n4systems.fieldid.actions.helpers.UploadFileSupport;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.security.NetworkAwareAction;
import com.n4systems.fieldid.security.SafetyNetworkAware;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.ThingEventCreationService;
import com.n4systems.fieldid.service.mixpanel.MixpanelService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.ui.OptionLists;
import com.n4systems.fieldid.util.EventFormHelper;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.fieldid.viewhelpers.EventHelper;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.handlers.creator.events.factory.ProductionEventPersistenceFactory;
import com.n4systems.model.*;
import com.n4systems.model.api.Listable;
import com.n4systems.model.assettype.AssetTypeLoader;
import com.n4systems.model.commenttemplate.CommentTemplate;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.eventbook.EventBookByNameLoader;
import com.n4systems.model.eventbook.EventBookListLoader;
import com.n4systems.model.eventbook.EventBookSaver;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.model.utils.AssetEvent;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleListable;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.File;
import java.util.*;


public class EventCrud extends UploadFileSupport implements SafetyNetworkAware, ActionWithCriteriaResults {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(EventCrud.class);

	private final EventManager eventManager;
	private final LegacyAsset legacyAssetManager;
	private final UserManager userManager;
	protected final AssetManager assetManager;
	private final EventScheduleManager eventScheduleManager;
	protected final ProductionEventPersistenceFactory eventPersistenceFactory;
	protected final EventHelper eventHelper;
	protected final EventFormHelper eventFormHelper;
    private final ThingEventCreationService eventCreationService;
	protected Asset asset;
	protected Event event;

    protected EventBook eventBook;

	protected List<CriteriaResultWebModel> criteriaResults;
	protected String charge;
	protected ProofTestType proofTestType;
	protected File proofTest;// The actual file
	protected String peakLoad;
	protected String testDuration;
	protected String peakLoadDuration;
    protected String prootTestFileName;
    protected String prootTestFileData;
	protected Event openEvent;
    protected Long openEventId;
	private boolean scheduleSuggested = false;
	private String newEventBookTitle;
	private boolean allowNetworkResults = false;
	private List<SubEvent> subEvents;
	private List<ListingPair> examiners;
	private List<CommentTemplate> commentTemplates;
	private List<Listable<Long>> employees;
	private List<ListingPair> eventBooks;
    // TODO : WEB-2358  remove this.
//    private List<EventSchedule> availableSchedules;
	private List<ListingPair> eventJobs;
	private AssignedToUserGrouper userGrouper;
    private boolean refreshAutoSchedules = false;
    private AssetType assetType;
	
	private EventWebModel modifiableEvent;

	private Map<String, String> encodedInfoOptionMap = new HashMap<String, String>(); 

	protected FileDataContainer fileDataContainer = null;

	private String proofTestDirectory;
	private boolean newFile = false;
	
	private List<WebEventSchedule> nextSchedules = new ArrayList<WebEventSchedule>();
	
	private User assignedTo;
	private boolean assignToSomeone = false;

	private boolean isEditing;
    private PersistenceService persistenceService;

    @Autowired
    private UserService userService;

    @Autowired
    private MixpanelService mixpanelService;

    public EventCrud(PersistenceManager persistenceManager, EventManager eventManager, UserManager userManager, LegacyAsset legacyAssetManager,
			AssetManager assetManager, EventScheduleManager eventScheduleManager, ThingEventCreationService eventCreationService, PersistenceService persistenceService) {
		super(persistenceManager);
		this.eventManager = eventManager;
		this.legacyAssetManager = legacyAssetManager;
		this.userManager = userManager;
		this.assetManager = assetManager;
		this.eventHelper = new EventHelper(persistenceManager);
		this.eventScheduleManager = eventScheduleManager;
		this.eventPersistenceFactory = new ProductionEventPersistenceFactory();
		this.eventFormHelper = new EventFormHelper();
        this.eventCreationService = eventCreationService;
        this.persistenceService = persistenceService;
	}

	@Override
	protected void initMemberFields() {
        if (openEventId != null && !openEventId.equals(0L)) {
            event = persistenceManager.find(Event.class, openEventId, getTenant(), "asset", "eventForm.sections", "results", "results.criteriaImages", "attachments", "infoOptionMap", "type.supportedProofTests", "type.infoFieldNames", "subEvents", "type.eventForm.sections");
            event.setInitialResultBasedOnScoreOrOneClicksBeingAvailable();
            event.setEventForm(event.getType().getEventForm());
        } else {
            event = new ThingEvent();
        }
		isEditing = false;
		event.setDate(new Date());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
        Locale previousLanguage = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();
        try {
            ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(getCurrentUser().getLanguage());

            Event testEvent = persistenceService.find(Event.class, uniqueId);

            if (testEvent instanceof PlaceEvent) {
                event = persistenceManager.find(Event.class, uniqueId, Event.PLACE_FIELD_PATHS);
            } else {
                if (allowNetworkResults) {

                    // if we're in a vendor context we need to look events for assigned assets rather than registered assets
                    event = getLoaderFactory().createSafetyNetworkEventLoader(isInVendorContext()).setId(uniqueId).load();

                } else {
                    event = eventManager.findAllFields(uniqueId, getSecurityFilter());
                }
            }


			if (event != null && !event.isActive()) {
				event = null;
			}
		} catch(RuntimeException e) {
			logger.error("Failed to load event", e);
			
		} finally {
            ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(previousLanguage);
        }
	}
	
	@Override
	protected void postInit() {
		super.postInit();
		modifiableEvent = new EventWebModel(new OwnerPicker(getLoaderFactory().createEntityByIdLoader(BaseOrg.class), event), getSessionUser().createUserDateConverter(), this);
		overrideHelper(new EventCrudHelper(getLoaderFactory()));
	}
	
	protected void testDependencies() throws MissingEntityException {
		if (event == null) {
			addActionError(getText("error.noevent"));
			throw new MissingEntityException();
		}

		if (event instanceof AssetEvent && asset == null) {
			addActionError(getText("error.noasset"));
			throw new MissingEntityException();
		}

		if (event.getType() == null) {
			addActionError(getText("error.eventtype"));
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
	public String doQuickEvent() {

		if (asset == null) {
			addActionError(getText("error.noasset"));
			return MISSING;
		}

		if (event == null) {
			addActionError(getText("error.noevent"));
			return MISSING;
		}

		return SUCCESS;
	}

	public List<AssociatedEventType> getAssociatedEventTypes() {
		return getLoaderFactory().createAssociatedEventTypesLoader().setAssetType(asset.getType()).load();
	}
	
	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent, Permissions.EditEvent})
	public String doSelect() {
		if (event == null) {
			addActionError(getText("error.noevent"));
			return MISSING;
		}
		
		if (!event.isNew()) {
            Long id = ((ThingEvent) event).getAsset().getId();
            setAssetId(id);
		}
		
		if (asset == null) {
			addActionError(getText("error.noasset"));
			return MISSING;
		}

		if (event.getEventType().isThingEventType() && ((ThingEvent)event).getThingType().isMaster()) {
			return "master";
		}
		
		return "regular";
		
	}
	

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
	public String doAdd() {
		testDependencies();

		// set defaults.
        if (event instanceof ThingEvent) {
            ((ThingEvent)event).setAssetStatus(asset.getAssetStatus());
        }
		event.setOwner(asset.getOwner());
		event.setAdvancedLocation(asset.getAdvancedLocation());
		event.setDate(DateHelper.getTodayWithTime());
		setPerformedBy(getSessionUser().getUniqueID());
		event.setPrintable(event.getType().isPrintable());
		setUpSupportedProofTestTypes();
		assignedTo = asset.getAssignedUser();
		
		autoSchedule();

		modifiableEvent.updateValuesToMatch(event);
		
		return SUCCESS;
	}

	private void autoSchedule() {
        autoSchedule(asset.getOwner());
	}

    private void autoSchedule(BaseOrg owner) {
        autoSchedule(owner, asset.getType());
    }

    private void autoSchedule(BaseOrg owner, AssetType assetType) {
        AssetTypeSchedule schedule = assetType.getSchedule(event.getType(), owner);
        if (schedule != null) {
            ScheduleToWebEventScheduleConverter converter = new ScheduleToWebEventScheduleConverter(getSessionUser().createUserDateConverter());
            WebEventSchedule nextSchedule = converter.convert(schedule, event.getDate());
            nextSchedule.setAutoScheduled(true);
            nextSchedules.add(0, nextSchedule);
        }
    }

	@SkipValidation
	@NetworkAwareAction
	public String doShow() {
		asset = event != null ? !(event instanceof ThingEvent) ? null : ((ThingEvent)event).getAsset() : null;
		testDependencies();
        mixpanelService.sendEvent(MixpanelService.VIEWED_COMPLETED_EVENT);
		return SUCCESS;
	}
	
	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditEvent})
	public String doEdit() {
		isEditing = true;
		try {
			setAssetId(((ThingEvent)event).getAsset().getId());
			testDependencies();
		} catch (NullPointerException e) {
			return MISSING;
		}

		setAttachments(event.getAttachments());
		// Re-encode the event info option map so it displays properly
		encodeInfoOptionMapForUseInForm();
		

        if (event instanceof ThingEvent) {
            ThingEventProofTest eventProofTest = ((ThingEvent)event).getProofTestInfo();
            if (eventProofTest != null) {
                peakLoad = eventProofTest.getPeakLoad();
                testDuration = eventProofTest.getDuration();
                peakLoadDuration = eventProofTest.getPeakLoadDuration();
                prootTestFileName = eventProofTest.getProofTestFileName();
                //is it necessary to load this? they can be up to couple of MBs each! prootTestFileData = eventProofTest.getProofTestInfo().getProofTestData();
            }

        }

		setUpSupportedProofTestTypes();
		
		modifiableEvent.updateValuesToMatch(event);
		return SUCCESS;
	}

    @SkipValidation
    public String doOwnerChange() {
        nextSchedules.clear();
        autoSchedule(modifiableEvent.getOwner(), assetType);
        return SUCCESS;
    }

	protected void encodeInfoOptionMapForUseInForm() {
		encodedInfoOptionMap = encodeMapKeys(event.getInfoOptionMap());
	}

	protected void setUpSupportedProofTestTypes() {
        if (!(event instanceof ThingEvent)) {
            return;
        }
        ThingEventProofTest proofTest = ((ThingEvent)event).getProofTestInfo();
        if (proofTest != null){
            proofTestType = proofTest.getProofTestType();
		} else if (!((ThingEventType)getEventType()).getSupportedProofTests().isEmpty()) {
			proofTestType = ((ThingEventType)getEventType()).getSupportedProofTests().iterator().next();
		}
	}

	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
	public String doCreate() {
		return save();
	}
	
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditEvent})
	public String doUpdate() {
		return save();
	}
	
	private String save() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}

		try {
			// get the user to set modifiedBy's later
			User modifiedBy = fetchCurrentUser();

            event.setBook(eventBook);
			findEventBook();
			
			//Set asset on the event before pushing other details.
            if (event instanceof ThingEvent) {
                ((ThingEvent)event).setAsset(asset);

                if (null != ((ThingEvent) event).getAssetStatus()) {
                 ((ThingEvent) event).getAsset().setAssetStatus(((ThingEvent) event).getAssetStatus());
                }
            }

            if (event instanceof ThingEvent) {
                modifiableEvent.pushValuesTo((ThingEvent)event);
            }

			event.getInfoOptionMap().putAll(decodeMapKeys(encodedInfoOptionMap));

			event.setTenant(getTenant());
			
			if (assignToSomeone) {
				AssignedToUpdate assignedToUpdate= AssignedToUpdate.assignAssetToUser(assignedTo);
				event.setAssignedTo(assignedToUpdate);
			}
			
			
			processProofTestFile();

			if (event.getWorkflowState() == WorkflowState.OPEN) {
				// the criteriaResults from the form must be processed before setting them on the event
				eventHelper.processFormCriteriaResults(event, criteriaResults, modifiedBy, getSessionUser());


                if (refreshAutoSchedules) {
                    autoSchedule(modifiableEvent.isOwnerSetFromAsset() ? asset.getOwner() : modifiableEvent.getOwner());
                }

				EventResult eventEventResult = (modifiableEvent.getOverrideResult() != null && !"auto".equals(modifiableEvent.getOverrideResult())) ? EventResult.valueOf(modifiableEvent.getOverrideResult()) : null;
                event.setEventResult(eventEventResult);

                event = eventCreationService.createEventWithSchedules((ThingEvent)event, 0L, fileDataContainer, getUploadedFiles(), createEventScheduleBundles());
				uniqueID = event.getId();
				
			} else {
				// only process criteria results if form editing is allowed
				if (event.isEditable()) {
					eventHelper.processFormCriteriaResults(event, criteriaResults, modifiedBy, getSessionUser());
				}
				// when updating, we need to remove any files that should no longer be attached
				updateAttachmentList(event, modifiedBy);
				event = eventManager.updateEvent((ThingEvent)event, null, getSessionUser().getUniqueID(), fileDataContainer, getUploadedFiles());
			}

			
		} catch (ProcessingProofTestException e) {
			logger.error("event save failed processing prooftest", e);
			addActionErrorText("error.processingprooftest");
			return INPUT;
		} catch (FileAttachmentException e) {
			logger.error("event save failed attaching file", e);
			addActionErrorText("error.attachingfile");
			return INPUT;
		} catch (ValidationException e) {
			return INPUT;
		} catch (Exception e) {
			addActionErrorText("error.eventsavefailed");
			logger.error("event save failed identifier " + asset.getIdentifier(), e);
			return ERROR;
		}

		addFlashMessageText("message.eventsaved");
		return SUCCESS;
	}

    protected List<EventScheduleBundle<Asset>> createEventScheduleBundles() {
		List<EventScheduleBundle<Asset>> scheduleBundles = new ArrayList<EventScheduleBundle<Asset>>();
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

	protected void findEventBook() throws ValidationException, PersistenceException {
		if (newEventBookTitle != null) {
			if (newEventBookTitle.trim().length() == 0) {
				addFieldError("newEventBookTitle", getText("error.event_book_title_required"));
				throw new ValidationException("not validated.");
			}

			// TODO: change this to the EventBookFindOrCreateLoader
			EventBookByNameLoader bookLoader = new EventBookByNameLoader(getSecurityFilter());
			bookLoader.setName(newEventBookTitle);
			bookLoader.setOwner(getOwner());
			EventBook eventBook = bookLoader.load();
			
			if (eventBook == null) {
				eventBook = new EventBook();
				eventBook.setName(newEventBookTitle);
				eventBook.setOpen(true);
				eventBook.setOwner(getOwner());
				eventBook.setTenant(getTenant());
				
				EventBookSaver bookSaver = new EventBookSaver();
				bookSaver.save(eventBook);
			}
			
			event.setBook(eventBook);
		}
	}
	
	protected void processProofTestFile() throws ProcessingProofTestException {
        if (!getEventType().isThingEventType()) {
            return;
        }
        ThingEvent thingEvent = (ThingEvent) event;

        if (newFile == true && proofTestDirectory != null && proofTestDirectory.length() != 0) {
            File tmpDirectory = PathHandler.getTempRoot();
            proofTest = new File(tmpDirectory.getAbsolutePath() + '/' + proofTestDirectory);
        }

        if (proofTest != null && proofTestType != ProofTestType.OTHER) {

            if(thingEvent.getProofTestInfo() == null){
                thingEvent.setProofTestInfo(new ThingEventProofTest());
            }
            thingEvent.getProofTestInfo().setProofTestType(proofTestType);
            fileDataContainer = createFileDataContainer();
		} else if (proofTestType == ProofTestType.OTHER) {
            if(thingEvent.getProofTestInfo() == null){
                thingEvent.setProofTestInfo(new ThingEventProofTest());
            }
            thingEvent.getProofTestInfo().setProofTestType(proofTestType);
            fileDataContainer = new FileDataContainer();
            fileDataContainer.setFileType(proofTestType);
            fileDataContainer.setPeakLoad(peakLoad);
            fileDataContainer.setTestDuration(testDuration);
            fileDataContainer.setPeakLoadDuration(peakLoadDuration);
		}
	}

	private FileDataContainer createFileDataContainer() throws ProcessingProofTestException {

		FileDataContainer fileDataContainer = null;
        if (getEventType().isThingEventType()) {
            try {
                ThingEventProofTest thingEventProofTest = ((ThingEvent)event).getProofTestInfo();
                if(thingEventProofTest != null){
                    fileDataContainer = thingEventProofTest.getProofTestType().getFileProcessorInstance().processFile(proofTest);
                }
            } catch (Exception e) {
                throw new ProcessingProofTestException(e);
            } finally {
                // clean up the temp proof test file
                proofTest.delete();
            }

        }

		return fileDataContainer;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditEvent})
	public String doDelete() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}
		try {
			eventManager.retireEvent(event, getSessionUser().getUniqueID());
		} catch (Exception e) {
			addFlashErrorText("error.eventdeleting");
			logger.error("event retire " + asset.getIdentifier(), e);
			return ERROR;
		}

		addFlashMessage(getText("message.eventdeleted"));
		return SUCCESS;
	}
	
	


	public Long getType() {
		return (event.getType() != null) ? event.getType().getId() : null;
	}

	public EventType getEventType() {
		return event.getType();
	}

	public boolean isPrintable() {
		return event.isPrintable();
	}


	public void setPrintable(boolean printable) {
		event.setPrintable(printable);
	}

	@RequiredFieldValidator(message="", key="error.noeventtype")
	public void setType(Long type) {
		if (type == null) {
			event.setType(null);
		} else if (event.getType() == null || !type.equals(event.getType().getId())) {
            ThingEventType eventType = persistenceManager.find(ThingEventType.class, type, getTenantId(), "eventForm.sections", "supportedProofTests", "infoFieldNames");
            event.setType(eventType);
            if (event.getEventForm() == null) {
                event.setEventForm(eventType.getEventForm());
            }
		}
	}

	public Asset getAsset() {
		return asset;
	}

	public Long getAssetId() {
		return (asset != null) ? asset.getId() : null;
	}
	
	public List<ThingEvent> getEventSchedules() {
		return eventScheduleManager.getAvailableSchedulesFor(asset, "asset");
	}

	@RequiredFieldValidator(message="", key="error.noasset")
	public void setAssetId(Long assetId) {
		if (assetId == null) {
			asset = null;
		} else if (asset == null || !assetId.equals(asset.getId())) {
			asset = assetManager.findAsset(assetId, getSecurityFilter(), "type.eventTypes", "infoOptions", "projects");
			asset = assetManager.fillInSubAssetsOnAsset(asset);
		}
	}

	public Event getEvent() {
		return event;
	}
	
	public List<ListingPair> getExaminers() {
		if (examiners == null) {
			examiners = userManager.getExaminers(getSecurityFilter());
			OptionLists.includeInList(examiners, new ListingPair(event.getPerformedBy()));
		}
		return examiners;
	}

	public List<AssetStatus> getAssetStatuses() {
		List<AssetStatus> assetStatuses = getLoaderFactory().createAssetStatusListLoader().load();
	
		if (getEventType().isThingEventType() && isEditing && !assetStatuses.contains(((ThingEvent)event).getAssetStatus())) {
			assetStatuses.add(((ThingEvent) event).getAssetStatus());
		}
		
		return assetStatuses;
	}

	public List<CommentTemplate> getCommentTemplates() {
		if (commentTemplates == null) {
			commentTemplates = getLoaderFactory().createCommentTemplateListableLoader().load();
		}
		return commentTemplates;
	}

	public String getComments() {
		return event.getComments();
	}
	
	public void setComments(String comments) {
		event.setComments(comments);
	}

	public Long getAssetStatus() {
        if (!getEventType().isThingEventType()) {
            return 0L;
        }
		return (((ThingEvent)event).getAssetStatus() != null) ? ((ThingEvent)event).getAssetStatus().getId() : null;
	}

	public void setAssetStatus(Long assetStatus) {
//        if(!getEventType().isThingEventType()) {
//            return;
//        }

		if (assetStatus == null) {
            ((ThingEvent)event).setAssetStatus(null);
		} else if (((ThingEvent)event).getAssetStatus() == null || !assetStatus.equals(((ThingEvent)event).getAssetStatus().getId())) {
            ((ThingEvent)event).setAssetStatus(legacyAssetManager.findAssetStatus(assetStatus, getTenantId()));
		} 
	}

	public Long getperformedBy() {
		return (event.getPerformedBy() != null) ? event.getPerformedBy().getId() : null;
	}

	public void setPerformedBy(Long performedBy) {
		if (performedBy == null) {
			event.setPerformedBy(null);
		} else if (event.getPerformedBy() == null || !performedBy.equals(event.getPerformedBy())) {
			event.setPerformedBy(persistenceManager.find(User.class, performedBy, getTenant()));
		}
	}

	public Long getBook() {
		return (event.getBook() != null) ? event.getBook().getId() : null;
	}

	public void setBook(Long book) {
		if (book == null) {
			eventBook = null;
		} else if (eventBook == null || book.equals(eventBook.getId())) {
			eventBook = persistenceManager.find(EventBook.class, book, getTenant());
		}
	}
	
	public String getNewEventBookTitle() {
		return newEventBookTitle;
	}
	
	public void setNewEventBookTitle(String newEventBookTitle) {
		this.newEventBookTitle = newEventBookTitle;
	}

	@Override
	public List<CriteriaResultWebModel> getCriteriaResults() {
		if (criteriaResults == null) {
			// criteria results need to be placed back in the order that they appear on the form
			// so that the buttons and button images line up correctly.
            criteriaResults = eventHelper.orderCriteriaResults(event, getSessionUser());
		}

		return criteriaResults;
	}

	@Validations(customValidators = {
            @CustomValidator(type = "observationsProcessingValidator"),
			@CustomValidator(type = "allScoresMustBeEntered", message = "", key = "error.scores.required"),
			@CustomValidator(type = "numberCriteriaValidator", message = "", key = "error.invalid_number_criteria")})
    public void setCriteriaResults(List<CriteriaResultWebModel> results) {
		criteriaResults = results;
	}

	public String getProofTestType() {
		getProofTestTypeEnum();
		return (proofTestType != null) ? proofTestType.name() : null;
	}

	public ProofTestType getProofTestTypeEnum() {
		if (getEventType().isThingEventType() && proofTestType == null && ((ThingEvent) event).getProofTestInfo() != null) {
            proofTestType = ((ThingEvent) event).getProofTestInfo().getProofTestType();
		}
		return proofTestType;
	}

	public void setProofTestType(String proofTestType) {
		this.proofTestType = ProofTestType.valueOf(proofTestType);
	}

	public List<ListingPair> getEventBooks() {
		if (eventBooks == null) {
			EventBookListLoader loader = new EventBookListLoader(getSecurityFilter());
			loader.setOpenBooksOnly((uniqueID == null));
			loader.setOwner(getOwner());
			eventBooks = loader.loadListingPair();
		}
		return eventBooks;
	}

	public String getResult() {
		return (event.getEventResult() != null) ? event.getEventResult().name() : null;
	}

	public void setResult(String result) {
		event.setEventResult((result != null && result.trim().length() > 0) ? EventResult.valueOf(result) : null);
	}

	public List<EventResult> getResults() {
		return EventResult.getValidEventResults();
	}

	@SuppressWarnings("unchecked")
	public Map getInfoOptionMap() {
		return event.getInfoOptionMap();
	}

	@SuppressWarnings("unchecked")
	public void setInfoOptionMap(Map infoOptionMap) {
		event.setInfoOptionMap(infoOptionMap);
	}

	
	public File returnFile(String fileName) {
		return new File(fileName);
	}

	public String getPeakLoad() {
		return peakLoad;
	}

	public void setPeakLoad(String peakLoad) {
		this.peakLoad = peakLoad;
	}

	public String getTestDuration() {
		return testDuration;
	}

	public void setTestDuration(String testDuration) {
		this.testDuration = testDuration;
	}
	
	public String getPeakLoadDuration() {
		return peakLoadDuration;
	}

	public void setPeakLoadDuration(String peakLoadDuration) {
		this.peakLoadDuration = peakLoadDuration;
	}

	public Map<String, Boolean> getProofTestTypesUpload() {
		Map<String, Boolean> uploadFlags = new HashMap<String, Boolean>();
        if (getEventType().isThingEventType()) {
            for (ProofTestType proofTestType : ((ThingEvent)event).getThingType().getSupportedProofTests()) {
                uploadFlags.put(proofTestType.name(), proofTestType.isUploadable());
            }
        }

		return uploadFlags;
	}

	public String getProofTestDirectory() {
		return proofTestDirectory;
	}

	public void setProofTestDirectory(String proofTestDirectory) {
		this.proofTestDirectory = proofTestDirectory;
	}

	public boolean isNewFile() {
		return newFile;
	}

	public void setNewFile(boolean newFile) {
		this.newFile = newFile;
	}

	public boolean isParentAsset() {
		return true;
	}

	public List<SubEvent> getSubEvents() {
		if (getEventType().isThingEventType() && subEvents == null) {
			if (!event.getSubEvents().isEmpty()) {
				Set<Long> ids = new HashSet<Long>();
				for (SubEvent subEvent : ((ThingEvent)event).getSubEvents()) {
					ids.add(subEvent.getId());
				}
				subEvents = persistenceManager.findAll(SubEvent.class, ids, getTenant(), SubEvent.ALL_FIELD_PATHS);
			}
		}
		return subEvents;
	}	

	
	
	/** Finds a Recommendation on the criteriaResults for this criteriaId and recommendation index */
	public Recommendation findEditRecommendation(Long criteriaId, int recIndex) {
		// first let's grab the result for this Criteria
		CriteriaResultWebModel result = eventHelper.findResultInCriteriaResultsByCriteriaId(criteriaResults, criteriaId);
		
		// next we need the text of our Recommendation
		String recText = eventHelper.findCriteriaOnEventType(event, criteriaId).getRecommendations().get(recIndex);
		
		// now we need to hunt down a matching Recommendation from our criteria results (if it exists)
		return eventHelper.getObservationForText(result.getRecommendations(), recText);
	}
	
	/** Finds a Deficiency on the criteriaResults for this criteriaId and deficiency index */
	public Deficiency findEditDeficiency(Long criteriaId, int defIndex) {
		// first let's grab the result for this Criteria
		CriteriaResultWebModel result = eventHelper.findResultInCriteriaResultsByCriteriaId(criteriaResults, criteriaId);
		
		// next we need the text of our Deficiency
		String defText = eventHelper.findCriteriaOnEventType(event, criteriaId).getDeficiencies().get(defIndex);
		
		// now we need to hunt down a matching Deficiency from our criteria results (if it exists)
		return eventHelper.getObservationForText(result.getDeficiencies(), defText);
	}
	
	/** Finds a comment Recommendation on the criteriaResults for this criteriaId */
	public Recommendation findEditRecommendationComment(Long criteriaId) {
		CriteriaResultWebModel result = eventHelper.findResultInCriteriaResultsByCriteriaId(criteriaResults, criteriaId);
		return eventHelper.getCommentObservation(result.getRecommendations());
	}
	
	/** Finds a comment Deficiency on the criteriaResults for this criteriaId */
	public Deficiency findEditDeficiencyComment(Long criteriaId) {
		CriteriaResultWebModel result = eventHelper.findResultInCriteriaResultsByCriteriaId(criteriaResults, criteriaId);
		return eventHelper.getCommentObservation(result.getDeficiencies());
	}

	public int countRecommendations(int criteriaIndex) {
		if (criteriaResults == null || criteriaResults.isEmpty() || criteriaResults.get(criteriaIndex) == null) {
			return 0;
		}
		return eventHelper.countObservations(criteriaResults.get(criteriaIndex).getRecommendations());
	}
	
	public int countDeficiencies(int criteriaIndex) {
		if (criteriaResults == null || criteriaResults.isEmpty() || criteriaResults.get(criteriaIndex) == null) {
			return 0;
		}
		return eventHelper.countObservations(criteriaResults.get(criteriaIndex).getDeficiencies());
	}
	
//	public Long getScheduleId() {
//		return eventScheduleId;
//	}

    public Long getScheduleId() {
        return openEventId;
    }

	public void setScheduleId(Long eventScheduleId) {
        openEventId = eventScheduleId;
	}

	public List<ListingPair> getJobs() {
		if (eventJobs == null) {
			List<Listable<Long>> eventJobListables = getLoaderFactory().createEventJobListableLoader().load();
			eventJobs = ListHelper.longListableToListingPair(eventJobListables);
		}
		return eventJobs;
	}
	
	public boolean isScheduleSuggested() {
		return scheduleSuggested;
	}
	
	public Map<String, String> getEncodedInfoOptionMap() {
		return encodedInfoOptionMap;
	}

	public void setEncodedInfoOptionMap(Map<String, String> encodedInfoOptionMap) {
		this.encodedInfoOptionMap = encodedInfoOptionMap;
	}

	public Long getOwnerId() {
		return modifiableEvent.getOwnerId();
	}

	
	public void setOwnerId(Long id) {
		modifiableEvent.setOwnerId(id);
	}

	@VisitorFieldValidator(message = "")
	public EventWebModel getModifiableEvent() {
		if (modifiableEvent == null) {
			throw new NullPointerException("action has not been initialized.");
		}
		return modifiableEvent;
	}
	
	@CustomValidator(type = "requiredOwnerMassEvent", message = "", key = "error.owner.required")
	public BaseOrg getOwner() {		
		return modifiableEvent.getOwner();
	}
	
	@Override
	public void setAllowNetworkResults(boolean allow) {
		this.allowNetworkResults = allow;
	}
	
	public boolean isLinkedEvent() {
		return !event.getTenant().equals(getTenant());
	}

	public EventFormHelper getEventFormHelper() {
		return eventFormHelper;
	}

	@SuppressWarnings("deprecation")
	public List<EventType> getEventTypes() {
		return new ArrayList<EventType>(asset.getType().getEventTypes());
	}

	public List<WebEventSchedule> getNextSchedules() {
		return nextSchedules;
	}

	public Long getAssignedToId() {
		return (assignedTo != null) ? assignedTo.getId() : null;
	}
	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedToId(Long assginedToId) {
		if (assginedToId == null) {
			assignedTo = null;
		} else if (assignedTo == null || !assginedToId.equals(assignedTo.getId())) {
			assignedTo = persistenceManager.find(User.class, assginedToId, getTenantId());
		}
	}

	public void setAssignToSomeone(boolean assignToSomeone) {
		this.assignToSomeone = assignToSomeone;
	}

	public boolean isAssignToSomeone() {
		return assignToSomeone;
	}
	
	public List<Listable<Long>> getEmployees() {
		if (employees == null) {
			employees = new ArrayList<Listable<Long>>();
			employees.add(new SimpleListable<Long>(0L, getText("label.unassigned")));
			employees.addAll(getLoaderFactory().createCombinedUserListableLoader().load());
		}
		return employees;
	}
	
	public Long getEventId() {
		return event.getId();
	}

    public boolean isOpen() {
        return event != null && WorkflowState.OPEN.equals(event.getWorkflowState());
    }

	public AssignedToUserGrouper getUserGrouper() {
		if (userGrouper == null){
			userGrouper = new AssignedToUserGrouper(new TenantOnlySecurityFilter(getSecurityFilter()), getEmployees(), getSessionUser());
		}
		return userGrouper;
	}

    public boolean isAutoResultAvailableForEvent() {
        if (event.getEventForm() == null)
            return false;

        if (event.getEventForm().isUseScoreForResult()) {
            return true;
        }

        for (CriteriaSection section : event.getEventForm().getSections()) {
            for (Criteria criteria : section.getCriteria()) {
                if (criteria instanceof OneClickCriteria && ((OneClickCriteria)criteria).isPrincipal()) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getOverrideResult() {
        return modifiableEvent.getOverrideResult();
    }

    public void setOverrideResult(String overrideResult) {
        this.modifiableEvent.setOverrideResult(overrideResult);
    }
    public void setLocationSetFromAsset(boolean isLocationSetFromAsset) {
		modifiableEvent.setLocationSetFromAsset(isLocationSetFromAsset);
	}
    
    public void setOwnerSetFromAsset(boolean isOwnerSetFromAsset) {
		modifiableEvent.setOwnerSetFromAsset(isOwnerSetFromAsset);
		
		//This is just a placeholder owner to get around the validation in EventWebModel.
		modifiableEvent.setOwnerId(event.getOwner().getId());
	}
    
    public void setStatusSetFromAsset(boolean isStatusSetFromAsset) {
    	modifiableEvent.setStatusSetFromAsset(isStatusSetFromAsset);
    }


    public boolean isRefreshAutoSchedules() {
        return refreshAutoSchedules;
    }

    public void setRefreshAutoSchedules(boolean refreshAutoSchedules) {
        this.refreshAutoSchedules = refreshAutoSchedules;
    }

    public void setAssetTypeId(Long assetTypeId) {
        assetType = new AssetTypeLoader(getSecurityFilter()).setId(assetTypeId).load();
    }

    public String getTemporarySignatureFileId(Long criteriaId) {
        return null;
    }
    
    public String getLatitude() { 
    	return getEvent().getGpsLocation() != null ? formatBigDecimal(getEvent().getGpsLocation().getLatitude()) : "";
    }

    public String getLongitude() { 
    	return getEvent().getGpsLocation() != null ? formatBigDecimal(getEvent().getGpsLocation().getLongitude()) : "";
    }

    @Override
    public boolean isUseLegacyCss() {
        return false;
    }

    public EventHelper getEventHelper() {
        return eventHelper;
    }
    
    public List<EventStatus> getEventStatuses() {
        return persistenceManager.findAll(EventStatus.class, getTenantId(), Collections.singletonMap("name", true));
    }
    
    public Long getEventStatus() {
        return event.getEventStatus() != null ? event.getEventStatus().getId() : null;
    }

    public void setEventStatus(Long id) {
        if(id != null) {
            EventStatus eventStatus = persistenceManager.find(EventStatus.class, id);
            event.setEventStatus(eventStatus);
        } else {
            event.setEventStatus(null);
        }
    }

    public Project getProject() {
        return getEvent().getProject();
    }

    public Long getProjectId() {
        return getProject().getId();
    }
    
    public Asset getLinkedAsset() {
        QueryBuilder<Asset> query = new QueryBuilder<Asset>(Asset.class, getSecurityFilter());
        if (asset.getNetworkId() != null && !asset.getNetworkId().equals(asset.getId())) {
            query.addSimpleWhere("id", asset.getNetworkId());
        } else {
            query.addSimpleWhere("networkId", asset.getId());
        }
        return persistenceManager.find(query);
    }

    public List<UserGroup> getUserGroups() {
        return userGroupService.getVisibleActiveUserGroups();
    }

    public List<User> getAssignees() {
        return userService.getExaminers();
    }

    @Override
    public boolean isOverrideLanguage(String methodName) {
        return false;
    }
}
