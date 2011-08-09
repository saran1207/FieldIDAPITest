package com.n4systems.fieldid.actions.asset;

import java.util.HashMap;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.ejb.legacy.AssetCodeMappingService;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.model.Asset;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.safetynetwork.AssetsByNetworkIdLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAssetAttachmentListLoader;

public class TraceabilityCrud extends AssetCrud {

	private boolean networkAsset;

	public TraceabilityCrud(LegacyAssetType assetTypeManager, LegacyAsset legacyAssetManager, PersistenceManager persistenceManager,
			AssetCodeMappingService assetCodeMappingServiceManager, AssetManager assetManager, OrderManager orderManager, ProjectManager projectManager, EventScheduleManager eventScheduleManager, EventManager eventManager) {
		super(assetTypeManager, legacyAssetManager, persistenceManager, assetCodeMappingServiceManager, assetManager, orderManager, projectManager, eventScheduleManager, eventManager);
	}
	
		
	@SkipValidation
	public String doTraceability() {
		setPageType();
		
		testExistingAsset();
		
		AssetsByNetworkIdLoader loader = getLoaderFactory().createAssetsByNetworkIdLoader();
		loader.setNetworkId(asset.getNetworkId());
		
		if (!isInVendorContext()) {
			// in a vendor context, we show the asset since they don't get the "show" tab right now
			loader.setExcludeAssetId(asset.getId());
		}
		
		linkedAssets = loader.load();
		
		// let's populate our asset attachment map
		SafetyNetworkAssetAttachmentListLoader attachmentLoader = getLoaderFactory().createSafetyNetworkAssetAttachmentListLoader();
		attachmentLoader.setNetworkId(asset.getNetworkId());
		
		linkedAssetAttachments = new HashMap<Long, List<AssetAttachment>>();
		for (Asset linkedProd: linkedAssets) {
			attachmentLoader.setAssetId(linkedProd.getId());
			linkedAssetAttachments.put(linkedProd.getId(), attachmentLoader.load());
		}
		
		return SUCCESS;
	}


	private void setPageType() {
		if(!networkAsset) {
			setPageType("asset", "traceability");
		}else {
			setPageType("network_asset", "traceability");
		}
	}
	
	
	public boolean isHasRegisteredAsset() {
		for (Asset linkedAsset : getLinkedAssets()) {
			if (linkedAsset.getTenant().equals(getTenant())) {
				return true;
			}
		}
		return false;
	}

	
	public Long getContextAssetId() {
		return isInVendorContext() ? asset.getId() : null;
	}


	public boolean isNetworkAsset() {
		return networkAsset;
	}


	public void setNetworkAsset(boolean networkAsset) {
		this.networkAsset = networkAsset;
	}
}
