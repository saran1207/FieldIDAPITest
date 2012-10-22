package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.*;
import com.n4systems.ejb.legacy.AssetCodeMappingService;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.fieldid.actions.asset.AssetWebModel;
import com.n4systems.fieldid.actions.asset.TraceabilityCrud;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.Tenant;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.AssetAlreadyRegisteredLoader;
import com.n4systems.services.TenantFinder;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.CatalogServiceImpl;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.List;

public class SafetyNetworkAsset extends TraceabilityCrud{

	public SafetyNetworkAsset(LegacyAssetType assetTypeManager,
			LegacyAsset legacyAssetManager,
			PersistenceManager persistenceManager,
			AssetCodeMappingService assetCodeMappingServiceManager,
			AssetManager assetManager, OrderManager orderManager,
			ProjectManager projectManager,
			EventScheduleManager eventScheduleManager, EventManager eventManager) {
		super(assetTypeManager, legacyAssetManager, persistenceManager,
                assetCodeMappingServiceManager, assetManager, orderManager,
				projectManager, eventScheduleManager, eventManager);
	}
	
	protected AssetWebModel assetWebModel = new AssetWebModel(this);
    protected List<Event> eventSchedules;
	
	@Override
	protected void postInit() {
		setNetworkAsset(true);
		super.postInit();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		asset = getLoaderFactory().createSafetyNetworkAssetLoader().withAllFields().setAssetId(uniqueId).load();
		assetWebModel.match(asset);
	}
	
	@Override
	public boolean isInVendorContext() {
		return true;
	}

    public PrimaryOrg getVendor() {
        return TenantFinder.getInstance().findPrimaryOrg(asset.getTenant().getId());
    }

	@SkipValidation
	@Override
	public String doEvents() {
		testExistingAsset();

		return SUCCESS;
	}

    @SkipValidation
	public String doSchedules() {
		testExistingAsset();

		return SUCCESS;
	}

    @Override
	public List<AssetAttachment> getAssetAttachments() {
		if (assetAttachments == null) {
			assetAttachments = getLoaderFactory().createSafetyNetworkAssetAttachmentListLoader()
                    .setAssetId(asset.getId())
                    .setNetworkId(asset.getNetworkId())
                    .load();
		}
		return assetAttachments;
	}

    public List<Event> getEventSchedules() {
        if (eventSchedules == null) {
            eventSchedules = eventScheduleManager.getAvailableSchedulesFor(asset);
        }
        return eventSchedules;
    }

    public List<Event> getEvents() {
        return getLoaderFactory().createEventsByAssetIdLoader().setAssetId(asset.getId()).load();
    }

	public boolean isPublishedCatalog(Tenant tenant) {
		try {
			CatalogService catalogService = new CatalogServiceImpl(persistenceManager, tenant);
			return catalogService.hasCatalog();
		} catch (Exception e) {
			return false;
		}
	}

    public boolean isAssetAlreadyRegistered(Asset asset) {
        AssetAlreadyRegisteredLoader loader = getLoaderFactory().createAssetAlreadyRegisteredLoader();
        return loader.setNetworkId(asset.getNetworkId()).load();
    }

}
