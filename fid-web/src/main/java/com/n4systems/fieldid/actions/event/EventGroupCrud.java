package com.n4systems.fieldid.actions.event;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.model.Asset;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventType;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.AssociatedEventType;

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
				assets = assetManager.findAssetByIdentifiers(getSecurityFilter(), search);
				// if there is only one forward. directly to the group view
				// screen.
				if (assets.size() == 1) {
					asset = assets.get(0);
					uniqueID = asset.getId();
					return "oneFound";
				}
			} catch (Exception e) {
				logger.error("Failed to look up Products", e);
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

}
