package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.security.Permissions;

public class SetupAction extends SimpleAction {
	
	private LegacyAssetType assetTypeManager;
	
	private final static Comparator<AssetType> ASSET_TYPE_NAME_COMPARATOR = new Comparator<AssetType>() {
		@Override
		public int compare(AssetType o1, AssetType o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};
	
	private final static Comparator<EventType> EVENT_TYPE_NAME_COMPARATOR = new Comparator<EventType>() {
		@Override
		public int compare(EventType o1, EventType o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};

	public SetupAction(PersistenceManager persistenceManager, LegacyAssetType assetTypeManager) {
		super(persistenceManager);
		this.assetTypeManager = assetTypeManager;
	}

	@Override
	@UserPermissionFilter(userRequiresOneOf={Permissions.ManageEndUsers, Permissions.ManageSystemConfig, Permissions.ManageSystemUsers})
	public String execute() {
		return super.execute();
	}
	
	public List<AssetType> getAssetTypes() {
		List<AssetType> assetTypeList = assetTypeManager.getAssetTypesForTenant(getTenantId());
		Collections.sort(assetTypeList, ASSET_TYPE_NAME_COMPARATOR);
		return assetTypeList;
	}
	
	public List<EventType> getEventTypes() {
		List<EventType> eventTypeList = getLoaderFactory().createEventTypeListLoader().load();
		Collections.sort(eventTypeList, EVENT_TYPE_NAME_COMPARATOR);
		return eventTypeList;
	}
	
	public List<AssetType> getAttributeTypes() {
		List<AssetType> assetTypeList = new ArrayList<AssetType>();
		for (AssetType type: getAssetTypes()) {
			if(type.getAutoAttributeCriteria() != null)
				assetTypeList.add(type);
		}
		return assetTypeList;
	}

}
