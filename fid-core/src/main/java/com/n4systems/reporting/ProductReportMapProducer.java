package com.n4systems.reporting;

import java.io.File;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.reporting.mapbuilders.ReportField;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.ReportMap;

public class ProductReportMapProducer extends ReportMapProducer {

	private static final String UNASSIGNED_USER_NAME = "Unassigned";
	private final Asset asset;
	public ProductReportMapProducer(Asset product, DateTimeDefinition dateTimeDefinition) {
		super(dateTimeDefinition);
		this.asset = product;
	}

	
	
	@Override
	protected void addParameters() {
		addProductParams();
		addProductTypeParams();
	}

	private void addProductParams() {
		add("productDesc", asset.getDescription());
		add("rfidNumber", asset.getRfidNumber());
		add("serialNumber", asset.getSerialNumber());
		add("reelId", asset.getSerialNumber());
		add("poNumber", asset.getPurchaseOrder());
		add("customerRefNumber", asset.getCustomerRefNumber());
		add("dateOfIssue", formatDate(asset.getCreated(), false));
		add("productComment", asset.getComments());
		add("productLocation", asset.getAdvancedLocation().getFreeformLocation());
		add("predefinedLocationFullName", asset.getAdvancedLocation().getFullName());
		add("productIdentified", formatDate(asset.getIdentified(),false));
		add("currentProductStatus", productStatusName());
		add("infoOptionMap", produceInfoOptionMap());
		add("lastInspectionDate", formatDate(asset.getLastInspectionDate(), true));
		add("infoOptionBeanList", asset.getOrderedInfoOptionList());
		add("infoOptionDataSource", new JRBeanCollectionDataSource(asset.getOrderedInfoOptionList()));
		add(ReportField.ASSIGNED_USER.getParamKey(), assignedUserName());
		
	}

	
	private String assignedUserName() {
		return asset.isAssigned() ? asset.getAssignedUser().getUserLabel() : UNASSIGNED_USER_NAME;
	}

	
	private void addProductTypeParams() {
		AssetType assetType = asset.getType();
		
		add("typeName", assetType.getName());
		add("warning", assetType.getWarnings());
		add("productWarning", assetType.getWarnings());
		add("certificateText", assetType.getManufactureCertificateText());
		add("productInstructions", assetType.getInstructions());
		
		add("productImage", imagePath(assetType));
	}

	private File imagePath(AssetType assetType) {
		return (assetType.hasImage()) ? new File(PathHandler.getProductTypeImageFile(assetType), assetType.getImageName()) : null;
	}
	
	private ReportMap<String> produceInfoOptionMap() {
		ReportMap<String> infoOptions = new ReportMap<String>(asset.getOrderedInfoOptionList().size());
		for (InfoOptionBean option : asset.getOrderedInfoOptionList()) {
			infoOptions.put(normalizeString(option.getInfoField().getName()), option.getName());
		}
		return infoOptions;
	}

	private String productStatusName() {
		return (asset.getAssetStatus() != null) ? asset.getAssetStatus().getName() : null;
	}



	



	
	
}
