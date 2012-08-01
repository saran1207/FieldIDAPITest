package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.*;
import com.n4systems.model.eventschedule.NextEventScheduleLoader;
import com.n4systems.tools.Pager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventGroupCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(EventGroupCrud.class);

	private EventGroup eventGroup;
	private Asset asset;

	private List<Asset> assets;

	private List<EventGroup> eventGroups;

	private String search;

	private EventManager eventManager;
	private AssetManager assetManager;

	private List<EventType> eventTypes;

	private Pager<Asset> page;

	private boolean usePagination = true;
	private boolean useAjaxPagination = false;

	public EventGroupCrud(EventManager eventManager, AssetManager assetManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.eventManager = eventManager;
		this.assetManager = assetManager;
	}

	@Override
	protected void initMemberFields() {

	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		asset = assetManager.findAsset(uniqueId, getSecurityFilter());
	}

	public String doList() {
		// if no search param came just show the form.
		if (search != null && search.length() > 0) {
			try {
				if(isUsePagination()){
					page = getLoaderFactory().createSmartSearchPagedLoader().setSearchText(getSearch()).setPage(getCurrentPage()).load();
					
					// if there is only one forward directly to the group view screen.
					if (page.getTotalResults() == 1) {
						asset = page.getList().get(0);
						uniqueID = asset.getId();
												
						return "oneFound";
					}
				}else{
					assets = assetManager.findAssetByIdentifiers(getSecurityFilter(), search);
					
					if (assets.size() == 1) {
						asset = assets.get(0);
						uniqueID = asset.getId();
						return "oneFound";
					}
				}
			} catch (Exception e) {
				logger.error("Failed to look up Assets", e);
				addActionError(getText("error.failedtoload"));
				return ERROR;
			}

		}

		return SUCCESS;
	}

	public String doShow() {
		if (asset == null) {
			addActionError(getText("error.noasset"));
			return MISSING;
		}

		eventGroups = eventManager.findAllEventGroups(getSecurityFilter(), uniqueID, "events");
		return SUCCESS;
	}

	public String doDelete() {
		return SUCCESS;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		if (search != null) {
			search = search.trim();
		}
		this.search = search;
	}

	public EventGroup getEventGroup() {
		return eventGroup;
	}

	public List<Asset> getAssets() {
		return assets;
	}

	public List<EventGroup> getEventGroups() {
		return eventGroups;
	}

	public List<EventType> getEventTypes() {
		if (eventTypes == null) {
			eventTypes = new ArrayList<EventType>();
			List<AssociatedEventType> associatedEventTypes = getLoaderFactory().createAssociatedEventTypesLoader().setAssetType(getAsset().getType()).load();
			for (AssociatedEventType associatedEventType : associatedEventTypes) {
				eventTypes.add(associatedEventType.getEventType());
			}
		}
		return eventTypes;
		
	}

	public boolean isMasterEvent(Long id) {
		return eventManager.isMasterEvent(id);
	}
	
	public void setUsePagination(boolean usePagination) {
		this.usePagination = usePagination;
	}

	public boolean isUsePagination() {
		return usePagination;
	}

	public void setUseAjaxPagination(boolean useAjaxPagination) {
		this.useAjaxPagination = useAjaxPagination;
	}

	public boolean isUseAjaxPagination() {
		return useAjaxPagination;
	}

	public Pager<Asset> getPage() {
		return page;
	}

    public Date getNextScheduledEventDate(Long id) {
        Event openEvent = new NextEventScheduleLoader().setAssetId(id).load();
        return openEvent==null ? null : openEvent.getNextDate();
    }

}
