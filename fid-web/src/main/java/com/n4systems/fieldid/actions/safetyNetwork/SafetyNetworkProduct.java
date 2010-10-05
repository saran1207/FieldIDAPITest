package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
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
import com.n4systems.ejb.legacy.ProductCodeMapping;
import com.n4systems.fieldid.actions.product.AssetWebModel;
import com.n4systems.fieldid.actions.product.TraceabilityCrud;

import java.util.List;

public class SafetyNetworkProduct extends TraceabilityCrud{

	public SafetyNetworkProduct(LegacyProductType productTypeManager,
			LegacyProductSerial legacyProductSerialManager,
			PersistenceManager persistenceManager,
			ProductCodeMapping productCodeMappingManager,
			ProductManager productManager, OrderManager orderManager,
			ProjectManager projectManager,
			InspectionScheduleManager inspectionScheduleManager) {
		super(productTypeManager, legacyProductSerialManager, persistenceManager,
				productCodeMappingManager, productManager, orderManager,
				projectManager, inspectionScheduleManager);
	}
	
	protected AssetWebModel asset = new AssetWebModel(this);
    protected List<InspectionSchedule> inspectionSchedules;
	
	@Override
	protected void postInit() {
		setNetworkProduct(true);
		super.postInit();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		product = getLoaderFactory().createSafetyNetworkProductLoader().withAllFields().setProductId(uniqueId).load();
		asset.match(product);
	}
	
	@Override
	public boolean isInVendorContext() {
		return true;
	}

    public PrimaryOrg getVendor() {
        return TenantCache.getInstance().findPrimaryOrg(product.getTenant().getId());
    }

	@SkipValidation
	@Override
	public String doInspections() {
		testExistingProduct();

		return SUCCESS;
	}

    @SkipValidation
	public String doSchedules() {
		testExistingProduct();

		return SUCCESS;
	}

    @Override
	public List<ProductAttachment> getProductAttachments() {
		if (productAttachments == null) {
			productAttachments = getLoaderFactory().createSafetyNetworkProductAttachmentListLoader()
                    .setProductId(product.getId())
                    .setNetworkId(product.getNetworkId())
                    .load();
		}
		return productAttachments;
	}

    public List<InspectionSchedule> getInspectionSchedules() {
        if (inspectionSchedules == null) {
            inspectionSchedules = inspectionScheduleManager.getAvailableSchedulesFor(product);
        }
        return inspectionSchedules;
    }

    public List<Inspection> getInspections() {
        return getLoaderFactory().createInspectionsByProductIdLoader().setProductId(product.getId()).load();
    }

	public boolean isPublishedCatalog(Tenant tenant) {
		try {
			CatalogService catalogService = new CatalogServiceImpl(persistenceManager, tenant);
			return catalogService.hasCatalog();
		} catch (Exception e) {
			return false;
		}
	}

    public boolean isProductAlreadyRegistered(Product product) {
        ProductAlreadyRegisteredLoader loader = getLoaderFactory().createProductAlreadyRegisteredLoader();
        return loader.setNetworkId(product.getNetworkId()).load();
    }

}
