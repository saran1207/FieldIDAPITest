package com.n4systems.fieldid.actions.event;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.ejb.parameters.CreateEventParameterBuilder;
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
import com.n4systems.fieldid.actions.helpers.EventScheduleSuggestion;
import com.n4systems.fieldid.actions.helpers.UploadFileSupport;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.security.NetworkAwareAction;
import com.n4systems.fieldid.security.SafetyNetworkAware;
import com.n4systems.fieldid.ui.OptionLists;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.fieldid.viewhelpers.EventHelper;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.handlers.creator.events.factory.ProductionEventPersistenceFactory;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Deficiency;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.Recommendation;
import com.n4systems.model.Status;
import com.n4systems.model.SubEvent;
import com.n4systems.model.api.Listable;
import com.n4systems.model.assettype.AssetTypeLoader;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.eventbook.EventBookByNameLoader;
import com.n4systems.model.eventbook.EventBookListLoader;
import com.n4systems.model.eventbook.EventBookSaver;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.SimpleListable;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;


public class EventCrud extends UploadFileSupport implements SafetyNetworkAware {
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
	
	private EventGroup eventGroup;
	protected Asset asset;
	protected Event event;

	protected List<CriteriaResultWebModel> criteriaResults;
	protected String charge;
	protected ProofTestType proofTestType;
	protected File proofTest;// The actual file
	protected String peakLoad;
	protected String testDuration;
	protected String peakLoadDuration;
	protected EventSchedule eventSchedule;
	protected Long eventScheduleId;
	private boolean eventScheduleOnEvent = false;
	private boolean scheduleSuggested = false;
	private String newEventBookTitle;
	private boolean allowNetworkResults = false;
	private List<SubEvent> subEvents;
	private List<ListingPair> examiners;
	private List<Listable<Long>> commentTemplates;
	private List<Listable<Long>> employees;
	private List<ListingPair> eventBooks;
	private List<EventSchedule> availableSchedules;
	private List<ListingPair> eventJobs;
	private AssignedToUserGrouper userGrouper;
    private boolean refreshAutoSchedules = false;
    private AssetType assetType;
	
	private EventWebModel modifiableEvent;

	private Map<String, String> encodedInfoOptionMap = new HashMap<String, String>(); 

	protected FileDataContainer fileData = null;

	private String proofTestDirectory;
	private boolean newFile = false;
	
	private List<WebEventSchedule> nextSchedules = new ArrayList<WebEventSchedule>();
	
	private User assignedTo;
	private boolean assignToSomeone = false;

	private boolean isEditing;
	
	public EventCrud(PersistenceManager persistenceManager, EventManager eventManager, UserManager userManager, LegacyAsset legacyAssetManager,
			AssetManager assetManager, EventScheduleManager eventScheduleManager) {
		super(persistenceManager);
		this.eventManager = eventManager;
		this.legacyAssetManager = legacyAssetManager;
		this.userManager = userManager;
		this.assetManager = assetManager;
		this.eventHelper = new EventHelper(persistenceManager);
		this.eventScheduleManager = eventScheduleManager;
		this.eventPersistenceFactory = new ProductionEventPersistenceFactory();
		this.eventFormHelper = new EventFormHelper();
	}

	@Override
	protected void initMemberFields() {
		event = new Event();
		isEditing = false;
		event.setDate(new Date());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		try {
			if (allowNetworkResults) {
				
				// if we're in a vendor context we need to look events for assigned assets rather than registered assets
				event = getLoaderFactory().createSafetyNetworkEventLoader(isInVendorContext()).setId(uniqueId).load();
				
			} else {
				event = eventManager.findAllFields(uniqueId, getSecurityFilter());
			}
			
			if (event != null && !event.isActive()) {
				event = null;
			}
	
			if (event != null) {
				eventGroup = event.getGroup();
				eventScheduleOnEvent = (event.getSchedule() != null);
			}
		} catch(RuntimeException e) {
			logger.error("Failed to load event", e);
			
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

		if (asset == null) {
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
			setAssetId(event.getAsset().getId());
		}
		
		if (asset == null) {
			addActionError(getText("error.noasset"));
			return MISSING;
		}

		if (event.getType().isMaster()) {
			return "master";
		}
		
		return "regular";
		
	}
	

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
	public String doAdd() {
		testDependencies();

		// set defaults.
		event.setAssetStatus(asset.getAssetStatus());
		event.setOwner(asset.getOwner());
		event.setAdvancedLocation(asset.getAdvancedLocation());
		event.setDate(DateHelper.getTodayWithTime());
		setPerformedBy(getSessionUser().getUniqueID());
		event.setPrintable(event.getType().isPrintable());
		setUpSupportedProofTestTypes();
		assignedTo = asset.getAssignedUser();
		
		if (eventSchedule != null) {
			eventSchedule.inProgress();
			try {
				persistenceManager.update(eventSchedule, getSessionUser().getId());
				addActionMessageText("message.scheduleinprogress");
			} catch (Exception e) {
				logger.warn("could not move schedule to in progress", e);
			}
		}
		
		suggestSchedule();

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

	private void suggestSchedule() {
		if (eventScheduleOnEvent == false && eventSchedule == null) {
			setScheduleId(new EventScheduleSuggestion(getAvailableSchedules()).getSuggestedScheduleId());
			scheduleSuggested = (eventSchedule != null);
			
		}
	}

	@SkipValidation
	@NetworkAwareAction
	public String doShow() {
		asset = event != null ? event.getAsset() : null;
		testDependencies();

		eventGroup = event.getGroup();
		return SUCCESS;
	}
	
	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditEvent})
	public String doEdit() {
		isEditing = true;
		try {
			setAssetId(event.getAsset().getId());
			testDependencies();
		} catch (NullPointerException e) {
			return MISSING;
		}

		setAttachments(event.getAttachments());
		// Re-encode the event info option map so it displays properly
		encodeInfoOptionMapForUseInForm();
		

		if (event.getProofTestInfo() != null) {
			peakLoad = event.getProofTestInfo().getPeakLoad();
			testDuration = event.getProofTestInfo().getDuration();
			peakLoadDuration = event.getProofTestInfo().getPeakLoadDuration();
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
		if (event.getProofTestInfo() != null && event.getProofTestInfo().getProofTestType() != null) {
			proofTestType = event.getProofTestInfo().getProofTestType();
		} else if (!getEventType().getSupportedProofTests().isEmpty()) {
			proofTestType = getEventType().getSupportedProofTests().iterator().next();
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
			
			findEventBook();
			
			//Set asset on the event before pushing other details.
			event.setAsset(asset);
			modifiableEvent.pushValuesTo(event);
		
			event.setInfoOptionMap(decodeMapKeys(encodedInfoOptionMap));

			event.setGroup(eventGroup);
			event.setTenant(getTenant());
			
			if (assignToSomeone) {
				AssignedToUpdate assignedToUpdate= AssignedToUpdate.assignAssetToUser(assignedTo);
				event.setAssignedTo(assignedToUpdate);
			}
			
			
			processProofTestFile();

			if (event.isNew()) {
				// the criteriaResults from the form must be processed before setting them on the event
				eventHelper.processFormCriteriaResults(event, criteriaResults, modifiedBy, getSessionUser());

				CreateEventParameterBuilder createEventParameterBuilder = new CreateEventParameterBuilder(event, getSessionUserId())
						.withProofTestFile(fileData)
						.withUploadedImages(getUploadedFiles());

                if (refreshAutoSchedules) {
                    autoSchedule(modifiableEvent.isOwnerSetFromAsset() ? asset.getOwner() : modifiableEvent.getOwner());
                }
				
				createEventParameterBuilder.addSchedules(createEventScheduleBundles());
				Status eventStatus = (modifiableEvent.getOverrideResult() != null && !"auto".equals(modifiableEvent.getOverrideResult())) ? Status.valueOf(modifiableEvent.getOverrideResult()) : null;
                event.setStatus(eventStatus);

				event = eventPersistenceFactory.createEventCreator().create(createEventParameterBuilder.build());
				uniqueID = event.getId();
				
			} else {
				// only process criteria results if form editing is allowed
				if (event.isEditable()) {
					eventHelper.processFormCriteriaResults(event, criteriaResults, modifiedBy, getSessionUser());
				}
				// when updating, we need to remove any files that should no longer be attached
				updateAttachmentList(event, modifiedBy);
				event = eventManager.updateEvent(event, getSessionUser().getUniqueID(), fileData, getUploadedFiles());
			}
				
			completeSchedule();
			
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

    protected List<EventScheduleBundle> createEventScheduleBundles() {
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
	
	private void completeSchedule() {
		if (eventScheduleOnEvent == false ) {
			try {
				if (eventScheduleId.equals(EventScheduleSuggestion.NEW_SCHEDULE)) {
					eventSchedule = new EventSchedule(event);
				} else if (eventSchedule != null) {
					eventSchedule.completed(event);
				}
				if (eventSchedule != null) {
					eventScheduleManager.update(eventSchedule);
					addFlashMessageText("message.schedulecompleted");
				}
			} catch (Exception e) {
				logger.error("could not complete the schedule", e);
				addFlashErrorText("error.completingschedule");
			}
		}
	}

	protected void processProofTestFile() throws ProcessingProofTestException {
		if (newFile == true && proofTestDirectory != null && proofTestDirectory.length() != 0) {
			File tmpDirectory = PathHandler.getTempRoot();
			proofTest = new File(tmpDirectory.getAbsolutePath() + '/' + proofTestDirectory);
		}

		if (proofTest != null && proofTestType != ProofTestType.OTHER) {
			event.setProofTestInfo(new ProofTestInfo());
			event.getProofTestInfo().setProofTestType(proofTestType);
			fileData = createFileDataContainer();
		} else if (proofTestType == ProofTestType.OTHER) {
			event.setProofTestInfo(new ProofTestInfo());
			event.getProofTestInfo().setProofTestType(proofTestType);
			fileData = new FileDataContainer();
			fileData.setFileType(proofTestType);
			fileData.setPeakLoad(peakLoad);
			fileData.setTestDuration(testDuration);
			fileData.setPeakLoadDuration(peakLoadDuration);
		}
	}

	private FileDataContainer createFileDataContainer() throws ProcessingProofTestException {

		FileDataContainer fileData;
		try {
			fileData = event.getProofTestInfo().getProofTestType().getFileProcessorInstance().processFile(proofTest);
		} catch (Exception e) {
			throw new ProcessingProofTestException(e);
		} finally {
			// clean up the temp proof test file
			proofTest.delete();
		}

		return fileData;
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
            EventType eventType = persistenceManager.find(EventType.class, type, getTenantId(), "eventForm.sections", "supportedProofTests", "infoFieldNames");
            event.setType(eventType);
            if (event.getEventForm() == null) {
                event.setEventForm(eventType.getEventForm());
            }
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

	public Asset getAsset() {
		return asset;
	}

	public Long getAssetId() {
		return (asset != null) ? asset.getId() : null;
	}
	
	public List<EventSchedule> getEventSchedules() {
		return eventScheduleManager.getAvailableSchedulesFor(asset);
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
	
		if(isEditing && !assetStatuses.contains(event.getAssetStatus())) {
			assetStatuses.add(event.getAssetStatus());
		}
		
		return assetStatuses;
	}

	public List<Listable<Long>> getCommentTemplates() {
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
		return (event.getAssetStatus() != null) ? event.getAssetStatus().getId() : null;
	}

	public void setAssetStatus(Long assetStatus) {
		if (assetStatus == null) {
			event.setAssetStatus(null);
		} else if (event.getAssetStatus() == null || !assetStatus.equals(event.getAssetStatus().getId())) {
			event.setAssetStatus(legacyAssetManager.findAssetStatus(assetStatus, getTenantId()));
		} 
	}

	public Long getperformedBy() {
		return (event.getPerformedBy() != null) ? event.getPerformedBy().getId() : null;
	}

	public void setPerformedBy(Long performedBy) {
		if (performedBy == null) {
			event.setPerformedBy(null);
		} else if (event.getPerformedBy() == null || !performedBy.equals(event.getPerformedBy())) {
			event.setPerformedBy(persistenceManager.find(User.class, performedBy, getTenantId()));
		}
	}

	public Long getBook() {
		return (event.getBook() != null) ? event.getBook().getId() : null;
	}

	public void setBook(Long book) {
		if (book == null) {
			event.setBook(null);
		} else if (event.getBook() == null || !book.equals(event.getBook())) {
			event.setBook(persistenceManager.find(EventBook.class, book, getTenantId()));
		}
	}
	
	public String getNewEventBookTitle() {
		return newEventBookTitle;
	}
	
	public void setNewEventBookTitle(String newEventBookTitle) {
		this.newEventBookTitle = newEventBookTitle;
	}

	public List<CriteriaResultWebModel> getCriteriaResults() {
		if (criteriaResults == null) {
			// criteria results need to be placed back in the order that they appear on the form
			// so that the states and button images line up correctly.
            criteriaResults = eventHelper.orderCriteriaResults(event, getSessionUser());
		}

		return criteriaResults;
	}
	@Validations(customValidators = {
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
		if (proofTestType == null) {
			proofTestType = (event.getProofTestInfo() != null) ? event.getProofTestInfo().getProofTestType() : null;
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
		return (event.getStatus() != null) ? event.getStatus().name() : null;
	}

	public void setResult(String result) {
		event.setStatus((result != null && result.trim().length() > 0) ? Status.valueOf(result) : null);
	}

	public List<Status> getResults() {
		return Arrays.asList(Status.values());
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
		for (ProofTestType proofTestType : event.getType().getSupportedProofTests()) {
			uploadFlags.put(proofTestType.name(), proofTestType.isUploadable());
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
		if (subEvents == null) {
			if (!event.getSubEvents().isEmpty()) {
				Set<Long> ids = new HashSet<Long>();
				for (SubEvent subEvent : event.getSubEvents()) {
					ids.add(subEvent.getId());
				}
				subEvents = persistenceManager.findAll(SubEvent.class, ids, getTenant(), "asset", "eventForm.sections", "results", "attachments", "infoOptionMap");
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
	
	public Long getScheduleId() {
		return eventScheduleId;
	}

	public void setScheduleId(Long eventScheduleId) {
		if (eventScheduleId == null) {
			eventSchedule = null;
		} else if ((!eventScheduleId.equals(EventScheduleSuggestion.NEW_SCHEDULE) && !eventScheduleId.equals(EventScheduleSuggestion.NO_SCHEDULE)) &&
				(this.eventScheduleId == null || !eventScheduleId.equals(this.eventScheduleId))) {
			// XXX should this lock to just the correct asset and event type?
			eventSchedule = persistenceManager.find(EventSchedule.class, eventScheduleId, getTenantId());
		}
		this.eventScheduleId = eventScheduleId;
	}

	public List<EventSchedule> getAvailableSchedules() {
		if (availableSchedules == null) {
			availableSchedules = getLoaderFactory().createIncompleteEventSchedulesListLoader()
					.setAsset(asset)
					.setEventType(event.getType())
					.load();
			if (eventSchedule != null && !availableSchedules.contains(eventSchedule)) {
				availableSchedules.add(0, eventSchedule);
			}
		}
		return availableSchedules;
	}
	
	public List<ListingPair> getSchedules() {
		List<ListingPair> scheduleOptions = new ArrayList<ListingPair>();
		scheduleOptions.add(new ListingPair(EventScheduleSuggestion.NO_SCHEDULE, getText("label.notscheduled")));
		scheduleOptions.add(new ListingPair(EventScheduleSuggestion.NEW_SCHEDULE, getText("label.createanewscheduled")));
		for (EventSchedule schedule : getAvailableSchedules()) {
			scheduleOptions.add(new ListingPair(schedule.getId(), convertDate(schedule.getNextDate())));
		}
		
		return scheduleOptions;
	}
	
	public List<ListingPair> getJobs() {
		if (eventJobs == null) {
			List<Listable<Long>> eventJobListables = getLoaderFactory().createEventJobListableLoader().load();
			eventJobs = ListHelper.longListableToListingPair(eventJobListables);
		}
		return eventJobs;
	}
	
	public boolean isEventScheduleOnEvent() {
		return eventScheduleOnEvent;
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
}
