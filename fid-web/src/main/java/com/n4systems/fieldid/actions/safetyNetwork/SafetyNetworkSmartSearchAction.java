package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.List;

import com.n4systems.model.Asset;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.safetynetwork.SafetyNetworkSmartSearchLoader;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSafetyNetwork})
public class SafetyNetworkSmartSearchAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SafetyNetworkSmartSearchAction.class);
	
	private Long vendorId;
	private String searchText;
	private Asset asset;
	private List<Asset> assets;
	
	public SafetyNetworkSmartSearchAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doFind() {
		if (searchText == null || searchText.trim().length() == 0) {
			return "notfound";
		}
		
		SafetyNetworkSmartSearchLoader smartSearchLoader = setupLoader();
		
		try {
			loadAssets(smartSearchLoader);
			if (!assets.isEmpty()) {
				if (assets.size() == 1) {
					asset = assets.get(0);
					return "foundone";
				} else {
					return "foundMany";
				}
			}
			return "notfound";
			
		} catch(Exception e) {
			addActionErrorText("error.could_not_access_the_safety_network");
			logger.error("Failed loading linked asset", e);
			return ERROR;
		}
	}

	private void loadAssets(SafetyNetworkSmartSearchLoader smartSearchLoader) {
		assets = smartSearchLoader.load();
	}

	private SafetyNetworkSmartSearchLoader setupLoader() {
		SafetyNetworkSmartSearchLoader smartSearchLoader = getLoaderFactory().createSafetyNetworkSmartSearchLoader();
		smartSearchLoader.setVendorOrgId(vendorId);
		smartSearchLoader.setSearchText(searchText);
		return smartSearchLoader;
	}
	
	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	public Asset getAsset() {
		return asset;
	}

	public List<Asset> getAssets() {
		return assets;
	}
}
