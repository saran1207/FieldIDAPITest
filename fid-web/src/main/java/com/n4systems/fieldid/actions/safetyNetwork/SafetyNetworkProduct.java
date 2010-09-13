package com.n4systems.fieldid.actions.safetyNetwork;

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

	@SkipValidation
	@Override
	public String doInspections() {
		setPageType("network_product", "inspections");
		testExistingProduct();

		return SUCCESS;
	}

	
}
