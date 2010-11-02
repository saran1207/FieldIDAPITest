package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.model.Asset;
import com.n4systems.model.EventGroup;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;

public class InspectionGroupCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(InspectionGroupCrud.class);

	private EventGroup eventGroup;
	private Asset asset;

	private List<Asset> assets;

	private List<EventGroup> eventGroups;

	private String search;

	private EventManager eventManager;
	private AssetManager assetManager;

	private List<InspectionType> inspectionTypes;

	public InspectionGroupCrud(EventManager eventManager, AssetManager assetManager, PersistenceManager persistenceManager) {
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

		eventGroups = eventManager.findAllInspectionGroups(getSecurityFilter(), uniqueID, "inspections");
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

	public EventGroup getInspectionGroup() {
		return eventGroup;
	}

	public List<Asset> getAssets() {
		return assets;
	}

	public List<EventGroup> getInspectionGroups() {
		return eventGroups;
	}

	public List<InspectionType> getInspectionTypes() {
		if (inspectionTypes == null) {
			inspectionTypes = new ArrayList<InspectionType>();
			List<AssociatedInspectionType> associatedInspectionTypes = getLoaderFactory().createAssociatedInspectionTypesLoader().setAssetType(getAsset().getType()).load();
			for (AssociatedInspectionType associatedInspectionType : associatedInspectionTypes) {
				inspectionTypes.add(associatedInspectionType.getInspectionType());
			}
		}
		return inspectionTypes;
		
	}

	public boolean isMasterInspection(Long id) {
		return eventManager.isMasterEvent(id);
	}

}
