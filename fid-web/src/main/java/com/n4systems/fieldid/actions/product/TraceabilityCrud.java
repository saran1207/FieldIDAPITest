package com.n4systems.fieldid.actions.product;

import java.util.HashMap;
import java.util.List;

import com.n4systems.ejb.legacy.AssetCodeMappingService;
import com.n4systems.model.Asset;
import com.n4systems.model.safetynetwork.ProductsByNetworkIdLoader;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.safetynetwork.SafetyNetworkProductAttachmentListLoader;

public class TraceabilityCrud extends ProductCrud {

	private boolean networkProduct;

	public TraceabilityCrud(LegacyProductType productTypeManager, LegacyProductSerial legacyProductSerialManager, PersistenceManager persistenceManager, 
			AssetCodeMappingService assetCodeMappingServiceManager, ProductManager productManager, OrderManager orderManager, ProjectManager projectManager, InspectionScheduleManager inspectionScheduleManager) {
		super(productTypeManager, legacyProductSerialManager, persistenceManager, assetCodeMappingServiceManager, productManager, orderManager, projectManager, inspectionScheduleManager);
	}
	
		
	@SkipValidation
	public String doTraceability() {
		setPageType();
		
		testExistingAsset();
		
		ProductsByNetworkIdLoader loader = getLoaderFactory().createProductsByNetworkIdLoader();
		loader.setNetworkId(asset.getNetworkId());
		
		if (!isInVendorContext()) {
			// in a vendor context, we show the asset since they don't get the "show" tab right now
			loader.setExcludeProductId(asset.getId());
		}
		
		linkedAssets = loader.load();
		
		// let's populate our asset attachment map
		SafetyNetworkProductAttachmentListLoader attachmentLoader = getLoaderFactory().createSafetyNetworkProductAttachmentListLoader();
		attachmentLoader.setNetworkId(asset.getNetworkId());
		
		linkedProductAttachments = new HashMap<Long, List<ProductAttachment>>();
		for (Asset linkedProd: linkedAssets) {
			attachmentLoader.setProductId(linkedProd.getId());
			linkedProductAttachments.put(linkedProd.getId(), attachmentLoader.load());
		}
		
		return SUCCESS;
	}


	private void setPageType() {
		if(!networkProduct) {
			setPageType("product", "traceability");
		}else {
			setPageType("network_product", "traceability");
		}
	}
	
	
	public boolean isHasRegisteredProduct() {
		for (Asset linkedAsset : getLinkedAssets()) {
			if (linkedAsset.getTenant().equals(getTenant())) {
				return true;
			}
		}
		return false;
	}

	
	public Long getContextProductId() {
		return isInVendorContext() ? asset.getId() : null;
	}


	public boolean isNetworkProduct() {
		return networkProduct;
	}


	public void setNetworkProduct(boolean networkProduct) {
		this.networkProduct = networkProduct;
	}
}
