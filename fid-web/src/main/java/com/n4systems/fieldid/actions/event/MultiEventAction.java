package com.n4systems.fieldid.actions.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.n4systems.fieldid.actions.event.viewmodel.EventWebModel;
import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.handlers.CommonEventTypeHandler;
import com.n4systems.handlers.LoaderBackedCommonEventTypeHandler;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.Status;
import com.n4systems.model.eventtype.CommonAssetTypeDatabaseLoader;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MultiEventActionHelper;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.collection.helpers.CommonAssetValues;
import com.n4systems.fieldid.collection.helpers.CommonAssetValuesFinder;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Asset;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.security.Permissions;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleListable;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
public class MultiEventAction extends AbstractCrud {

	private static final long UNASSIGNED_OPTION_VALUE = 0L;
	private static final long KEEP_THE_SAME_OPTION = -1L;
	private List<Long> assetIds = new ArrayList<Long>();
	private Set<EventType> eventTypes;

	private EventType eventType;
	private Event event;
	
	private final EventFormHelper eventFormHelper;
	private List<Asset> assets;

	private UserManager userManager;
	private List<ListingPair> examiners;
	private List<Listable<Long>> commentTemplates;
	private List<AssetStatus> assetStatuses;
	private CommonEventTypeHandler commonEventTypeHandler;
	private CommonAssetValues commonAssetValues;
	private EventWebModel modifiableEvent;
	private MultiEventGroupSorter multiEventGroupSorter;
	private List<Listable<Long>> employees;

    private String searchContainerKey;
    private String searchId;

	public MultiEventAction(PersistenceManager persistenceManager, UserManager userManager) {
		super(persistenceManager);
		this.userManager = userManager;
		this.eventFormHelper = new EventFormHelper();
	}
	
	@Override
	protected void initMemberFields() {
		event = new Event();
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
	
	public String doEventCheck() {
		return SUCCESS;
	}

	public List<Long> getAssetIds() {
		 assetIds.removeAll(Collections.singleton(null));
		return assetIds;
	}

	public Set<EventType> getEventTypes() {
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
			eventType = persistenceManager.find(EventType.class, type, getTenantId(), "eventForm.sections", "supportedProofTests", "infoFieldNames");
		}
	}

	public Long getPerformedBy() {
		return getSessionUser().getUniqueID();
	}

	public EventType getEventType() {
		return eventType;
	}

	public List<Asset> getAssets() {
		if (assets == null) {
            if (searchContainerKey != null && getSession().get(searchContainerKey) != null) {
                SearchContainer container = (SearchContainer) getSession().get(searchContainerKey);
                if (container.getSearchId().equals(searchId)) {
                    assets = persistenceManager.findAll(new QueryBuilder<Asset>(Asset.class, getSecurityFilter()).addWhere(Comparator.IN, "assetIds", "id", container.getMultiIdSelection().getSelectedIds()));
                    this.assetIds = new ArrayList<Long>();
                    this.assetIds.addAll(container.getMultiIdSelection().getSelectedIds());
                }
            } else if (!assetIds.isEmpty()) {
				assets = persistenceManager.findAll(new QueryBuilder<Asset>(Asset.class, getSecurityFilter()).addWhere(Comparator.IN, "assetIds", "id", assetIds));
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

	public void setEvent(Event event) {
		this.event = event;
	}

	public EventFormHelper getEventFormHelper() {
		return eventFormHelper;
	}
	
	public List<Listable<Long>> getCommentTemplates() {
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
			employees.addAll(getLoaderFactory().createCurrentEmployeesListableLoader().load());
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
    
    public boolean hasAtLeastOneResultSettingCriteria() {
        if (event.getEventForm() == null)
            return false;

        for (CriteriaSection section : event.getEventForm().getSections()) {
            for (Criteria criteria : section.getCriteria()) {
                if (criteria instanceof OneClickCriteria && ((OneClickCriteria)criteria).isPrincipal()) {
                    return true;
                }
            }
        }

        return false;
    }
    
	public List<Status> getResults() {
		return Arrays.asList(Status.values());
	}
}
