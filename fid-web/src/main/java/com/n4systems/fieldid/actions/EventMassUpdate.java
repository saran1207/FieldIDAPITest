package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.fieldid.actions.asset.LocationWebModel;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.fieldid.actions.helpers.MassUpdateEventHelper;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.eventbook.EventBookListLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.security.Permissions;
import com.n4systems.util.ListingPair;
import com.n4systems.util.ServiceLocator;
import com.opensymphony.xwork2.Preparable;

@UserPermissionFilter(userRequiresOneOf = { Permissions.EditEvent})
public class EventMassUpdate extends MassUpdate implements Preparable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(EventMassUpdate.class);

	private LegacyAsset assetManager;
	private EventSearchContainer criteria;
	private Event event = new Event();
	EventManager eventManager = ServiceLocator.getEventManager();
	private OwnerPicker ownerPicker;
	
	private int masterEventsToDelete=0;
	private int standardEventsToDelete=0;
	private int eventSchedulesToDelete=0;
	
	private final LocationWebModel location = new LocationWebModel(this);
	
	public EventMassUpdate(MassUpdateManager massUpdateManager, PersistenceManager persistenceManager, LegacyAsset assetManager) {
		super(massUpdateManager, persistenceManager);
		this.assetManager = assetManager;
	}

	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), event);
		overrideHelper(new MassUpdateEventHelper(getLoaderFactory()));
	}

	private void applyCriteriaDefaults() {
		setOwnerId(criteria.getOwnerId());
		setEventBook(criteria.getEventBook());
		setAssetStatus(criteria.getAssetStatus());
	}

	private boolean findCriteria() {
		criteria = getSession().getReportCriteria();

		if (criteria == null || searchId == null || !searchId.equals(criteria.getSearchId())) {
			return false;
		}
		return true;
	}

	@SkipValidation
	public String doEdit() {
		if (!findCriteria()) {
			addFlashErrorText("error.reportexpired");
			return ERROR;
		}

		applyCriteriaDefaults();
		return SUCCESS;
	}

	public String doSave() {
		if (!findCriteria()) {
			addFlashErrorText("error.reportexpired");
			return ERROR;
		}

		try {
			event.setAdvancedLocation(location.createLocation());
			List<Long> eventIds = criteria.getMultiIdSelection().getSelectedIds();
			Long results = massUpdateManager.updateEvents(eventIds, event, select, getSessionUser().getUniqueID());
			List<String> messageArgs = new ArrayList<String>();
			messageArgs.add(results.toString());
			addFlashMessage(getText("message.eventmassupdatesuccessful", messageArgs));

			return SUCCESS;
		} catch (UpdateFailureException ufe) {
			logger.error("failed to run a mass update on events", ufe);
		} catch (Exception e) {
			logger.error("failed to run a mass update on events", e);
		}

		addActionError(getText("error.failedtomassupdate"));
		return INPUT;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf = { Permissions.EditEvent })
	public String doConfirmDelete() {
		if (!findCriteria()) {
			addFlashErrorText("error.searchexpired");
			return ERROR;
		}

		List<Long> ids = criteria.getMultiIdSelection().getSelectedIds();
		calculateEventRemovalSummary(ids);

		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf = { Permissions.EditEvent })
	public String doDelete() {
		if (!findCriteria()) {
			addFlashErrorText("error.searchexpired");
			return ERROR;
		}
		
		List<Long> ids = criteria.getMultiIdSelection().getSelectedIds();
		calculateEventRemovalSummary(ids);

		try {
			findAndDeleteEvents(ids);
		} catch (Exception e) {
			addFlashErrorText("error.eventdeleting");
			logger.error("event retire " + event.getAsset().getSerialNumber(), e);
			return ERROR;
		}
		
		List<String> messageArgs = new ArrayList<String>();
		messageArgs.add(getMasterEventsToDelete() + getStandardEventsToDelete() + "");
		addFlashMessage(getText("message.event_massdelete_successful", messageArgs));
		
		return SUCCESS;
	}
	
	public Long getEventBook() {
		return (event.getBook() == null) ? null : event.getBook().getId();
	}

	public void setEventBook(Long eventBookId) {
		if (eventBookId == null) {
			event.setBook(null);
		} else if (event.getBook() == null || !eventBookId.equals(event.getBook().getId())) {
			EventBook eventBook = persistenceManager.find(EventBook.class, eventBookId);
			event.setBook(eventBook);
		}
	}

	public Collection<ListingPair> getEventBooks() {
		EventBookListLoader loader = new EventBookListLoader(getSecurityFilter());
		loader.setOpenBooksOnly(true);
		return loader.loadListingPair();
	}

	public boolean isPrintable() {
		return event.isPrintable();
	}

	public void setPrintable(boolean printable) {
		event.setPrintable(printable);
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}

	public List<AssetStatus> getAssetStatuses() {
		return getLoaderFactory().createAssetStatusListLoader().load();
	}

	public Long getAssetStatus() {
		return (event.getAssetStatus() == null) ? null : event.getAssetStatus().getId();
	}

	public void setAssetStatus(Long assetStatus) {
		if (assetStatus == null) {
			event.setAssetStatus(null);
		} else if (event.getAssetStatus() == null || !assetStatus.equals(event.getAssetStatus().getId())) {
			event.setAssetStatus(assetManager.findAssetStatus(assetStatus, getTenantId()));
		}
	}

	public LocationWebModel getLocation() {
		return location;
	}

	private void calculateEventRemovalSummary(List<Long> ids) {
		for (Long id : ids) {
			Event event = eventManager.findAllFields(id, new OpenSecurityFilter());
			if (event != null) {
				if (event.getType().isMaster()) {
					masterEventsToDelete++;
					standardEventsToDelete += event.getSubEvents().size();
				} else {
					standardEventsToDelete++;
				}
				eventSchedulesToDelete += (event.getSchedule() == null) ? 0 : 1;
			}
		}
	}
	
	private void findAndDeleteEvents(List<Long> ids){
		for (Long id  : ids) {
			
			Event event = eventManager.findAllFields(id, new OpenSecurityFilter());
			
			eventManager.retireEvent(event, getSessionUser().getUniqueID());
			
			criteria.getMultiIdSelection().clear();
		}
	}

	public int getMasterEventsToDelete() {
		return masterEventsToDelete;
	}

	public int getStandardEventsToDelete() {
		return standardEventsToDelete;
	}

	public int getEventSchedulesToDelete() {
		return eventSchedulesToDelete;
	}
	
	public int getNumberSelected(){
		if (!findCriteria()) {
			addFlashErrorText("error.searchexpired");
			return 0;
		}
		
		return criteria.getMultiIdSelection().getSelectedIds().size();
	}
}
