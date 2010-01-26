package com.n4systems.fieldid.actions.product;

import java.util.HashMap;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.LegacyProductType;
import rfid.ejb.session.ProductCodeMapping;
import rfid.ejb.session.User;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.model.Product;
import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.safetynetwork.ProductsByNetworkId;
import com.n4systems.model.safetynetwork.SafetyNetworkProductAttachmentListLoader;

public class TracabilityCrud extends ProductCrud {

	public TracabilityCrud(LegacyProductType productTypeManager, LegacyProductSerial legacyProductSerialManager, PersistenceManager persistenceManager, User userManager,
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

}
