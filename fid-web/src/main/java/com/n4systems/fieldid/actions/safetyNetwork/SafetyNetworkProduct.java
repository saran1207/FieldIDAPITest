package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.legacy.AssetCodeMappingService;
import com.n4systems.model.Asset;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.safetynetwork.ProductAlreadyRegisteredLoader;
import com.n4systems.services.TenantCache;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.CatalogServiceImpl;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.fieldid.actions.product.AssetWebModel;
import com.n4systems.fieldid.actions.product.TraceabilityCrud;

import java.util.List;

public class SafetyNetworkProduct extends TraceabilityCrud{

	public SafetyNetworkProduct(LegacyProductType productTypeManager,
			LegacyProductSerial legacyProductSerialManager,
			PersistenceManager persistenceManager,
			AssetCodeMappingService assetCodeMappingServiceManager,
			ProductManager productManager, OrderManager orderManager,
			ProjectManager projectManager,
			InspectionScheduleManager inspectionScheduleManager) {
		super(productTypeManager, legacyProductSerialManager, persistenceManager,
                assetCodeMappingServiceManager, productManager, orderManager,
				projectManager, inspectionScheduleManager);
	}
	
	protected AssetWebModel assetWebModel = new AssetWebModel(this);
    protected List<InspectionSchedule> inspectionSchedules;
	
	@Override
	protected void postInit() {
		setNetworkProduct(true);
		super.postInit();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		asset = getLoaderFactory().createSafetyNetworkProductLoader().withAllFields().setProductId(uniqueId).load();
		assetWebModel.match(asset);
	}
	
	@Override
	public boolean isInVendorContext() {
		return true;
	}

    public PrimaryOrg getVendor() {
        return TenantCache.getInstance().findPrimaryOrg(asset.getTenant().getId());
    }

	@SkipValidation
	@Override
	public String doInspections() {
		testExistingAsset();

		return SUCCESS;
	}

    @SkipValidation
	public String doSchedules() {
		testExistingAsset();

		return SUCCESS;
	}

    @Override
	public List<ProductAttachment> getAssetAttachments() {
		if (assetAttachments == null) {
			assetAttachments = getLoaderFactory().createSafetyNetworkProductAttachmentListLoader()
                    .setProductId(asset.getId())
                    .setNetworkId(asset.getNetworkId())
                    .load();
		}
		return assetAttachments;
	}

    public List<InspectionSchedule> getInspectionSchedules() {
        if (inspectionSchedules == null) {
            inspectionSchedules = inspectionScheduleManager.getAvailableSchedulesFor(asset);
        }
        return inspectionSchedules;
    }

    public List<Inspection> getInspections() {
        return getLoaderFactory().createInspectionsByProductIdLoader().setProductId(asset.getId()).load();
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
        ProductAlreadyRegisteredLoader loader = getLoaderFactory().createProductAlreadyRegisteredLoader();
        return loader.setNetworkId(asset.getNetworkId()).load();
    }

}
