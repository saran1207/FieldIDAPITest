package com.n4systems.reporting;

import java.io.File;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.reporting.mapbuilders.ReportField;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.ReportMap;

public class AssetReportMapProducer extends ReportMapProducer {

	private static final String UNASSIGNED_USER_NAME = "Unassigned";
	private final Asset asset;
	public AssetReportMapProducer(Asset asset, DateTimeDefinition dateTimeDefinition) {
		super(dateTimeDefinition);
		this.asset = asset;
	}

	
	
	@Override
	protected void addParameters() {
		addAssetParams();
		addAssetTypeParams();
	}

	private void addAssetParams() {
		add("productDesc", asset.getDescription());
		add("rfidNumber", asset.getRfidNumber());
		add("serialNumber", asset.getSerialNumber());
		add("reelId", asset.getSerialNumber());
		add("poNumber", asset.getPurchaseOrder());
		add("customerRefNumber", asset.getCustomerRefNumber());
		add("orderNumber", asset.getShopOrder().getOrder().getOrderNumber());
		add("dateOfIssue", formatDate(asset.getCreated(), false));
		add("productComment", asset.getComments());
		add("productLocation", asset.getAdvancedLocation().getFreeformLocation());
		add("predefinedLocationFullName", asset.getAdvancedLocation().getFullName());
		add("productIdentified", formatDate(asset.getIdentified(),false));
		add("currentProductStatus", assetStatusName());
		add("infoOptionMap", produceInfoOptionMap());
		add("lastInspectionDate", formatDate(asset.getLastEventDate(), true));
		add("infoOptionBeanList", asset.getOrderedInfoOptionList());
		add("infoOptionDataSource", new JRBeanCollectionDataSource(asset.getOrderedInfoOptionList()));
		add(ReportField.ASSIGNED_USER.getParamKey(), assignedUserName());
	}

	
	private String assignedUserName() {
		return asset.isAssigned() ? asset.getAssignedUser().getUserLabel() : UNASSIGNED_USER_NAME;
	}

	
	private void addAssetTypeParams() {
		AssetType assetType = asset.getType();
		
		add("typeName", assetType.getName());
		add("warning", assetType.getWarnings());
		add("productWarning", assetType.getWarnings());
		add("certificateText", assetType.getManufactureCertificateText());
		add("productInstructions", assetType.getInstructions());
		
		add("productImage", imagePath(assetType));
	}

	private File imagePath(AssetType assetType) {
		return (assetType.hasImage()) ? new File(PathHandler.getAssetTypeImageFile(assetType), assetType.getImageName()) : null;
	}
	
	private ReportMap<String> produceInfoOptionMap() {
		ReportMap<String> infoOptions = new ReportMap<String>(asset.getOrderedInfoOptionList().size());
		for (InfoOptionBean option : asset.getOrderedInfoOptionList()) {
			infoOptions.put(normalizeString(option.getInfoField().getName()), option.getName());
		}
		return infoOptions;
	}

	private String assetStatusName() {
		return (asset.getAssetStatus() != null) ? asset.getAssetStatus().getName() : null;
	}

}
