package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.event.viewmodel.CriteriaResultWebModel;
import com.n4systems.fieldid.actions.event.viewmodel.EventWebModel;
import com.n4systems.fieldid.actions.helpers.AssignedToUserGrouper;
import com.n4systems.fieldid.actions.helpers.MultiEventActionHelper;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.collection.helpers.CommonAssetValues;
import com.n4systems.fieldid.collection.helpers.CommonAssetValuesFinder;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.util.EventFormHelper;
import com.n4systems.handlers.CommonEventTypeHandler;
import com.n4systems.handlers.LoaderBackedCommonEventTypeHandler;
import com.n4systems.model.*;
import com.n4systems.model.api.Listable;
import com.n4systems.model.commenttemplate.CommentTemplate;
import com.n4systems.model.eventtype.CommonAssetTypeDatabaseLoader;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationByIdLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.SearchCriteriaContainer;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.security.Permissions;
import com.n4systems.uitags.views.HierarchicalNode;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleListable;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
public class MultiEventAction extends AbstractCrud implements ActionWithCriteriaResults {

	private static final long UNASSIGNED_OPTION_VALUE = 0L;
	private static final long KEEP_THE_SAME_OPTION = -1L;
	private List<Long> assetIds = new ArrayList<Long>();
	private Set<ThingEventType> eventTypes;

	private ThingEventType eventType;
	private ThingEvent event;
    private OwnerPicker ownerPicker;
	
	private final EventFormHelper eventFormHelper;
	private List<Asset> assets;

	private UserManager userManager;
	private List<ListingPair> examiners;
    private List<ListingPair> jobs;
	private List<CommentTemplate> commentTemplates;
	private List<AssetStatus> assetStatuses;
	private CommonEventTypeHandler commonEventTypeHandler;
	private CommonAssetValues commonAssetValues;
	private EventWebModel modifiableEvent;
	private MultiEventGroupSorter multiEventGroupSorter;
	private List<Listable<Long>> employees;
    protected List<CriteriaResultWebModel> criteriaResults;
	
    private String searchContainerKey;
    private String searchId;
    
    private EventScheduleManager eventScheduleManager;
    private AssignedToUserGrouper userGrouper;

    @Autowired
    protected UserService userService;
    @Autowired
    protected UserGroupService userGroupService;

    public MultiEventAction(PersistenceManager persistenceManager, UserManager userManager, EventScheduleManager eventScheduleManager) {
		super(persistenceManager);
		this.userManager = userManager;
		this.eventFormHelper = new EventFormHelper();
		this.eventScheduleManager=eventScheduleManager;
	}
	
	@Override
	protected void initMemberFields() {
		event = new ThingEvent();
		event.setType(eventType);
        if (eventType != null) {
            event.setEventForm(eventType.getEventForm());
        }
		event.setDate(new Date());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		initMemberFields();
	}
	
	@Override
	protected void postInit() {
		super.postInit();
		commonEventTypeHandler = createCommonEventTypeHandler();
		modifiableEvent = new EventWebModel(new OwnerPicker(getLoaderFactory().createEntityByIdLoader(BaseOrg.class), event), getSessionUser().createUserDateConverter(), this);
        ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), event);
        overrideHelper(new MultiEventActionHelper(getLoaderFactory()));
	}
	
	public void testDependencies() {
		if (getAssets() == null || getAssets().isEmpty()) {
			addActionErrorText("error.no_assets_given");
			throw new MissingEntityException("no assets given");
		}
	}
	
	@SkipValidation
	public String doEventTypes() {
		testDependencies();
		return SUCCESS;
	}

	private CommonEventTypeHandler createCommonEventTypeHandler() {
		return new LoaderBackedCommonEventTypeHandler(new CommonAssetTypeDatabaseLoader(getSecurityFilter(), ConfigContext.getCurrentContext()));
	}

	@SkipValidation
	public String doPerformEvent() {
		testDependencies();
		
		commonAssetValues = new CommonAssetValuesFinder(getAssets()).findCommonValues();
					
		event.setOwner(commonAssetValues.owner);	
		
		
		if (commonAssetValues.hasCommonLocation()) {
			event.setAdvancedLocation(commonAssetValues.location);
		}
		modifiableEvent.updateValuesToMatch(event);
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doPerformSchedule(){
		return SUCCESS;
	}
	
	public boolean isOneScheduleAvailable() {
		boolean oneAvailable=false;
		for (Asset asset : getAssets()){
			if (!getMultiEventScheduleListHelper().getEventSchedulesForAsset(asset).isEmpty()){
				oneAvailable=true;
				break;
			}
		}
		return oneAvailable;
	}

	public String doEventCheck() {
		return SUCCESS;
	}

	public List<Long> getAssetIds() {
		 assetIds.removeAll(Collections.singleton(null));
		return assetIds;
	}

	public Set<ThingEventType> getEventTypes() {
		if (eventTypes == null) {
			eventTypes = commonEventTypeHandler.findCommonEventTypesFor(assetIds);
		}
		return eventTypes;
	}

	public Long getType() {
		return eventType != null ? eventType.getId() : null;
	}

	public void setType(Long type) {
		if (type == null) {
			eventType = null;
		} else if (eventType == null || !type.equals(eventType.getId())) {
			eventType = persistenceManager.find(ThingEventType.class, type, getTenantId(), "eventForm.sections", "supportedProofTests", "infoFieldNames");
		}
	}

	public Long getPerformedBy() {
		return getSessionUser().getUniqueID();
	}

	public ThingEventType getEventType() {
		return eventType;
	}

	public List<Asset> getAssets() {
		if (assets == null) {
            if (searchContainerKey != null && getSession().get(searchContainerKey) != null) {
                SearchCriteriaContainer<AssetSearchCriteria> container = (SearchCriteriaContainer) getSession().get(searchContainerKey);
                if (container.getSearchId().equals(searchId)) {
                    assets = persistenceManager.findAll(new QueryBuilder<Asset>(Asset.class, getSecurityFilter()).addWhere(Comparator.IN, "assetIds", "id", container.getSearchCriteria().getSelection().getSelectedIds()).addPostFetchPaths("infoOptions"));
                    this.assetIds = new ArrayList<Long>();
                    this.assetIds.addAll(container.getSearchCriteria().getSelection().getSelectedIds());
                }
            } else if (!assetIds.isEmpty()) {
				assets = persistenceManager.findAll(new QueryBuilder<Asset>(Asset.class, getSecurityFilter()).addWhere(Comparator.IN, "assetIds", "id", assetIds).addPostFetchPaths("infoOptions"));
			} else {
				assets = new ArrayList<Asset>();
			}
		}
				
		return assets;
	}

	public boolean isPrintable() {
		return eventType.isPrintable();
	}

	public List<ListingPair> getExaminers() {
		if (examiners == null) {
			examiners = userManager.getExaminers(getSecurityFilter());
		}
		return examiners;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(ThingEvent event) {
		this.event = event;
	}

	public EventFormHelper getEventFormHelper() {
		return eventFormHelper;
	}
	
	public List<CommentTemplate> getCommentTemplates() {
		if (commentTemplates == null) {
			commentTemplates = getLoaderFactory().createCommentTemplateListableLoader().load();
		}
		return commentTemplates;
	}

	public List<AssetStatus> getAssetStatuses() {
		if (assetStatuses == null) {
			assetStatuses = getLoaderFactory().createAssetStatusListLoader().load();
		}
		return assetStatuses;
	}
	
	public Long getAssignedToId() {
		if (commonAssetValues.hasCommonAssignment()) {
			return commonAssetValues.assignment.assignTo != null ? commonAssetValues.assignment.assignTo.getId() : UNASSIGNED_OPTION_VALUE;
		}
		
		return KEEP_THE_SAME_OPTION;
	}
	
	public Long getAssetStatus() {
		return commonAssetValues.assetStatus != null ? commonAssetValues.assetStatus.getId() : null;
	}
	
	@VisitorFieldValidator(message="")
	public EventWebModel getModifiableEvent() {
		if (modifiableEvent == null) {
			throw new NullPointerException("action has not been initialized.");
		}
		return modifiableEvent;
	}
	
	public MultiEventGroupSorter getMultiEventGroupSorter() {
		if (multiEventGroupSorter == null) {
			multiEventGroupSorter = new MultiEventGroupSorter(getEventTypes());
		}
		return multiEventGroupSorter;
	}
	
	public List<Listable<Long>> getEmployees() {
		if (employees == null) {
			employees = new ArrayList<Listable<Long>>();
			employees.add(new SimpleListable<Long>(0L, getText("label.unassigned")));
			employees.addAll(getLoaderFactory().createCombinedUserListableLoader().load());
		}
		return employees;
	}
	
	public List<WebEventSchedule> getNextSchedules() {
		return new ArrayList<WebEventSchedule>();
	}

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getSearchContainerKey() {
        return searchContainerKey;
    }

    public void setSearchContainerKey(String searchContainerKey) {
        this.searchContainerKey = searchContainerKey;
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
    
    public MultiEventScheduleListHelper getMultiEventScheduleListHelper() {
    	return new MultiEventScheduleListHelper(eventScheduleManager, getEventType());
	}
    
	public List<EventResult> getResults() {
		return EventResult.getValidEventResults();
	}

    public boolean isRefreshAutoSchedules() {
        return true;
    }

    public String getTemporarySignatureFileId(Long criteriaId) {
        return null;
    }

	public AssignedToUserGrouper getUserGrouper() {
		if (userGrouper == null){
			userGrouper = new AssignedToUserGrouper(new TenantOnlySecurityFilter(getSecurityFilter()), getEmployees(), getSessionUser());
		}
		return userGrouper;
	}

    @Override
    public boolean isUseLegacyCss() {
        return false;
    }

    public List<CriteriaResultWebModel> getCriteriaResults() {
        return criteriaResults;
    }

    @Validations(customValidators = {
            @CustomValidator(type = "allScoresMustBeEntered", message = "", key = "error.scores.required"),
            @CustomValidator(type = "numberCriteriaValidator", message = "", key = "error.invalid_number_criteria")})
    public void setCriteriaResults(List<CriteriaResultWebModel> criteriaResults) {
        this.criteriaResults = criteriaResults;
    }

	public List<ListingPair> getJobs() {
		if (jobs == null) {
			List<Listable<Long>> eventJobListables = getLoaderFactory().createEventJobListableLoader().load();
			jobs = ListHelper.longListableToListingPair(eventJobListables);
		}
		return jobs;
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

    @SkipValidation
    public String doUpdateLocation() {
        return SUCCESS;
    }

    public List<HierarchicalNode> getPredefinedLocationTree() {
        return ((MultiEventActionHelper)getHelper()).getPredefinedLocationTree(event.getOwner());
    }

    public void setFreeFormLocation(String freeFormLocation) {
        event.getAdvancedLocation().setFreeformLocation(freeFormLocation);
    }

    public String getFreeFormLocation() {
        return event.getAdvancedLocation().getFreeformLocation();
    }

    public void setPredefinedLocationId(Long id) {
        event.getAdvancedLocation().setPredefinedLocation(new PredefinedLocationByIdLoader(getSecurityFilter()).setId(id).load());
    }

    public PredefinedLocation getPredefinedLocationId() {
        return event.getAdvancedLocation().getPredefinedLocation();
    }

    public void setOwnerId(Long ownerId) {
        ownerPicker.setOwnerId(ownerId);
    }

    public Long getOwnerId() {
        return ownerPicker.getOwnerId();
    }

    public BaseOrg getOwner() {
        return ownerPicker.getOwner();
    }

    public List<User> getAssignees() {
        return userService.getExaminers();
    }

    public List<UserGroup> getUserGroups() {
        return userGroupService.getVisibleActiveUserGroups();
    }

}
