 package com.n4systems.fieldid.actions.event;

 import com.n4systems.ejb.PersistenceManager;
 import com.n4systems.exceptions.MissingEntityException;
 import com.n4systems.fieldid.actions.api.AbstractCrud;
 import com.n4systems.fieldid.permissions.UserPermissionFilter;
 import com.n4systems.fieldid.service.event.EventTypeService;
 import com.n4systems.fieldid.service.remover.EventTypeRemovalService;
 import com.n4systems.fieldid.utils.StrutsListHelper;
 import com.n4systems.fieldid.viewhelpers.TrimmedString;
 import com.n4systems.fileprocessing.ProofTestType;
 import com.n4systems.handlers.remover.summary.EventTypeArchiveSummary;
 import com.n4systems.model.EventType;
 import com.n4systems.model.EventTypeGroup;
 import com.n4systems.model.eventtype.EventTypeCopier;
 import com.n4systems.security.Permissions;
 import com.n4systems.util.ListingPair;
 import com.n4systems.util.persistence.QueryBuilder;
 import com.opensymphony.xwork2.validator.annotations.CustomValidator;
 import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
 import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
 import com.opensymphony.xwork2.validator.annotations.Validations;
 import org.apache.log4j.Logger;
 import org.apache.struts2.interceptor.validation.SkipValidation;

 import java.util.*;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class EventTypeCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(EventTypeCrud.class);

	private List<ListingPair> eventTypeGroups;
	private List<EventType> eventTypes;
	private EventType eventType;
	private List<TrimmedString> infoFieldNames;
	private String saveAndAdd;
	private Map<String, Boolean> types;
	private EventTypeArchiveSummary archiveSummary;
	
	private boolean assignedToAvailable = false;
	
	private String nameFilter;
	private Long groupFilter;
    private EventTypeService eventTypeService;
    private EventTypeRemovalService eventTypeRemovalService;

    public EventTypeCrud(PersistenceManager persistenceManager, EventTypeService eventTypeService, EventTypeRemovalService eventTypeRemovalService) {
		super(persistenceManager);

        this.eventTypeService = eventTypeService;
        this.eventTypeRemovalService = eventTypeRemovalService;
    }

	@Override
	protected void initMemberFields() {
		eventType = new EventType();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		QueryBuilder<EventType> query = new QueryBuilder<EventType>(EventType.class, getSecurityFilter());
		query.addSimpleWhere("id", uniqueId);
		query.addPostFetchPaths("eventForm.sections", "supportedProofTests", "infoFieldNames");
		eventType = persistenceManager.find(query);
		
	}

	private void testRequiredEntities(boolean existing) {
		if (eventType == null || (existing && eventType.isNew())) {
			addActionErrorText("error.noeventtype");
			throw new MissingEntityException("no event type could be found.");
		}
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		assignedToAvailable = eventType.isAssignedToAvailable();
		infoFieldNames = TrimmedString.mapToTrimmedStrings(eventType.getInfoFieldNames());
		return SUCCESS;
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		assignedToAvailable = eventType.isAssignedToAvailable();
		infoFieldNames = TrimmedString.mapToTrimmedStrings(eventType.getInfoFieldNames());
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		return doSave();
	}

	public String doUpdate() {
		testRequiredEntities(true);
		return doSave();
	}

	private String doSave() {

		eventType.setInfoFieldNames(TrimmedString.mapFromTrimmedStrings(infoFieldNames));

		processSupportedTypes();
		
		processAssignedToAvailable();

		StrutsListHelper.clearNulls(eventType.getInfoFieldNames());

		eventType.setTenant(getTenant());
		
		eventType.setModifiedBy(getUser());
		eventType.setModified(new Date());
		
		try {
			if (eventType.getId() == null) {
				persistenceManager.save(eventType);
				uniqueID = eventType.getId();
			} else {
				eventType = persistenceManager.update(eventType);
			}

			addFlashMessageText("message.savedeventtype");
			if (saveAndAdd != null) {
				return "createEventForm";
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error("Failed saving EventType", e);
			addActionErrorText("error.failedtosave");
			return ERROR;
		}
	}

	private void processAssignedToAvailable() {
		if (assignedToAvailable && getSecurityGuard().isAssignedToEnabled()) {
			eventType.makeAssignedToAvailable();
		} else {
			eventType.removeAssignedTo();
		}
		
	}

	public String doDeleteConfirm() {
		testRequiredEntities(true);
		
		try {
            fillArchiveSummary(eventType);
		} catch (Exception e) {
            e.printStackTrace();
			addActionErrorText("error.confirming_event_type_delete");
			return ERROR;
		}

		return SUCCESS;
	}

	private EventTypeArchiveSummary fillArchiveSummary(EventType eventType) {
		archiveSummary = eventTypeRemovalService.summary(eventType);
		return archiveSummary;
	}

	public String doDelete() {
		testRequiredEntities(true);
		try {
            startBackgroundTask();

            addFlashMessageText("message.deleted_event_type");
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not archive event type", e);
			addFlashErrorText("error.delete_event_type");
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doCopy() {
		try {
			EventTypeCopier typeCopier = createEventTypeCopier();
			EventType newType = typeCopier.copy(getUniqueID());
			
			addFlashMessageText(getText("message.event_type_copied", new String[] {newType.getName()}));
		} catch(Exception e) {
			logger.error(getLogLinePrefix() + "failed coping event type", e);
			addFlashErrorText("error.unable_to_copy_event_type");
			return ERROR;
		}
		return SUCCESS;
	}
	
	protected EventTypeCopier createEventTypeCopier() {
		return new EventTypeCopier(getTenant());
	}

	private void startBackgroundTask() {
        eventTypeRemovalService.startRemovalTask(eventType);
	}

	public List<EventType> getEventTypes() {
		if (eventTypes == null) {
            eventTypes = eventTypeService.getEventTypes(groupFilter, nameFilter);
		}

		return eventTypes;
	}

	public EventType getEventType() {
		return eventType;
	}

	public List<ListingPair> getEventTypeGroups() {
		if (eventTypeGroups == null) {
			eventTypeGroups = persistenceManager.findAllLP(EventTypeGroup.class, getTenantId(), "name");
		}
		return eventTypeGroups;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public List<ProofTestType> getProofTestTypes() {
		return Arrays.asList(ProofTestType.values());
	}

	@RequiredStringValidator(message = "", key = "error.namerequired")
	public String getName() {
		return eventType.getName();
	}

	public void setName(String name) {
		eventType.setName(name);
	}

	public Long getGroup() {
		return (eventType.getGroup() != null) ? eventType.getGroup().getId() : null;
	}

	@RequiredFieldValidator(message = "", key = "error.grouprequired")
	public void setGroup(Long group) {
		if (group == null) {
			eventType.setGroup(null);
		} else if (!group.equals(eventType.getGroup())) {
			eventType.setGroup(persistenceManager.find(EventTypeGroup.class, group, getTenantId()));
		}
	}

	@SuppressWarnings("unchecked")
	public Map getSupportedProofTestTypes() {
		if (types == null) {
			types = new HashMap<String, Boolean>();

			// set up the map of types.
			for (ProofTestType type : getProofTestTypes()) {
				types.put(type.name(), false);
			}

			for (ProofTestType type : eventType.getSupportedProofTests()) {
				types.put(type.name(), true);
			}
		}

		return types;
	}

	private void processSupportedTypes() {
		Set<ProofTestType> supportedTypes = eventType.getSupportedProofTests();
		supportedTypes.clear();
		// convert the map of types back to a list of prooftestTypes
		if (types != null && !types.isEmpty()) {
			for (String typeKey : types.keySet()) {
				if (types.get(typeKey) != null && types.get(typeKey) == true) {
					supportedTypes.add(ProofTestType.valueOf(typeKey));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void setSupportedProofTestTypes(Map supportedTypes) {
		types = supportedTypes;
	}

	public boolean isPrintable() {
		return eventType.isPrintable();
	}

	public void setPrintable(boolean printable) {
		eventType.setPrintable(printable);
	}

	public boolean isMaster() {
		return eventType.isMaster();
	}

	public void setMaster(boolean master) {
		eventType.setMaster(master);
	}

	public void setSaveAndAdd(String saveAndAdd) {
		this.saveAndAdd = saveAndAdd;
	}

	public List<TrimmedString> getInfoFields() {
		if (infoFieldNames == null) {
			infoFieldNames = new ArrayList<TrimmedString>();
		}
		return infoFieldNames;
	}

	@Validations(customValidators = {
			@CustomValidator(type = "requiredStringSet", message = "", key = "error.eventattributeblank"),
			@CustomValidator(type = "uniqueInfoFieldValidator", message = "", key = "error.duplicateinfofieldname") })
	public void setInfoFields(List<TrimmedString> infoFields) {
		infoFieldNames = infoFields;
	}

	public EventTypeArchiveSummary getArchiveSummary() {
		return archiveSummary;
	}

	public boolean isAssignedToAvailable() {
		return assignedToAvailable;
	}

	public void setAssignedToAvailable(boolean assignedToAvailable) {
		this.assignedToAvailable = assignedToAvailable;
	}

	public Long getGroupFilter() {
		return groupFilter;
	}

	public void setGroupFilter(Long groupFilter) {
		this.groupFilter = groupFilter;
	}

	public void setEventTypes(List<EventType> eventTypes) {
		this.eventTypes = eventTypes;
	}

	public String getNameFilter() {
		return nameFilter;
	}

	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
	}

    public boolean isAction() {
        return eventType.getGroup().isAction();
    }
}
