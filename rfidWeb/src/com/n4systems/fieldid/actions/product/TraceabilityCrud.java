package com.n4systems.fieldid.actions.product;

import java.util.HashMap;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.ejb.legacy.ProductCodeMapping;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.model.Product;
import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.safetynetwork.ProductsByNetworkId;
import com.n4systems.model.safetynetwork.SafetyNetworkProductAttachmentListLoader;

public class TraceabilityCrud extends ProductCrud {

	public TraceabilityCrud(LegacyProductType productTypeManager, LegacyProductSerial legacyProductSerialManager, PersistenceManager persistenceManager, UserManager userManager,
			ProductCodeMapping productCodeMappingManager, ProductManager productManager, OrderManager orderManager, ProjectManager projectManager, InspectionScheduleManager inspectionScheduleManager) {
		super(productTypeManager, legacyProductSerialManager, persistenceManager, userManager, productCodeMappingManager, productManager, orderManager, projectManager, inspectionScheduleManager);
	}
	
		
	@SkipValidation
	public String doTraceability() {
		testExistingProduct();
		
		ProductsByNetworkId loader = getLoaderFactory().createProductsByNetworkId();
		loader.setNetworkId(product.getNetworkId());
		
		if (!isInVendorContext()) {
			// in a vendor context, we show the product since they don't get the "show" tab right now
			loader.setExcludeProductId(product.getId());
		}
		
		linkedProducts = loader.load();
		
		// let's populate our product attachment map
		SafetyNetworkProductAttachmentListLoader attachmentLoader = getLoaderFactory().createSafetyNetworkProductAttachmentListLoader();
		attachmentLoader.setNetworkId(product.getNetworkId());
		
		linkedProductAttachments = new HashMap<Long, List<ProductAttachment>>();
		for (Product linkedProd: linkedProducts) {
			attachmentLoader.setProductId(linkedProd.getId());
			linkedProductAttachments.put(linkedProd.getId(), attachmentLoader.load());
		}
		
		return SUCCESS;
	}
	
	
	public boolean isHasRegisteredProduct() {
		for (Product linkedProduct : getLinkedProducts()) {
			if (linkedProduct.getTenant().equals(getTenant())) {
				return true;
			}
		}
		return false;
	}

	
	public Long getContextProductId() {
		return isInVendorContext() ? product.getId() : null;
	}
}
