package com.n4systems.reporting;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.event.LastEventDateService;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.util.DateTimeDefinition;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.FileUtils;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class AssetReportMapProducer extends ReportMapProducer {

	private static final String UNASSIGNED_USER_NAME = "Unassigned";
	private final Asset asset;
    private LastEventDateService lastEventDateService;

    public AssetReportMapProducer(Asset asset, LastEventDateService lastEventDateService, DateTimeDefinition dateTimeDefinition, S3Service s3Service) {
		super(dateTimeDefinition, s3Service);
		this.asset = asset;
        this.lastEventDateService = lastEventDateService;
    }

	@Override
	protected void addParameters() {
		add("productDesc", asset.getDescription());
		add("rfidNumber", asset.getRfidNumber());
		add("serialNumber", asset.getIdentifier());
		add("reelId", asset.getIdentifier());
		add("poNumber", asset.getPurchaseOrder());
		add("customerRefNumber", asset.getCustomerRefNumber());
		add("orderNumber", asset.getOrderNumber());
		add("dateOfIssue", formatDate(asset.getCreated(), false));
		add("productComment", asset.getComments());
		add("productLocation", asset.getAdvancedLocation().getFreeformLocation());
		add("predefinedLocationFullName", asset.getAdvancedLocation().getFullName());
		add("productIdentified", formatDate(asset.getIdentified(), false));
		add("currentProductStatus", assetStatusName());
		add("infoOptionMap", produceInfoOptionMap());
        java.util.Date lastEventDate = lastEventDateService.findLastEventDate(asset);
		add("lastInspectionDate", formatDate(lastEventDate, true));
		add("infoOptionBeanList", asset.getOrderedInfoOptionList());
		add("infoOptionDataSource", new JRBeanCollectionDataSource(asset.getOrderedInfoOptionList()));
		add("assignedUserName", assignedUserName());
		
		AssetType assetType = asset.getType();
		add("typeName", assetType.getName());
		add("warning", assetType.getWarnings());
		add("productWarning", assetType.getWarnings());
		add("certificateText", assetType.getManufactureCertificateText());
		add("productInstructions", assetType.getInstructions());
		
		add("productImage", assetTypeImagePath(assetType));
        add("assetProfileImage", assetProfileImagePath(asset));
		add("ownerLogo", getCustomerLogo(asset.getOwner()));
        add("latitude", asset.getGpsLocation() != null ? asset.getGpsLocation().getLatitude() : "");
        add("longitude", asset.getGpsLocation() != null ? asset.getGpsLocation().getLongitude() : "");
	}

	private String assignedUserName() {
		return asset.isAssigned() ? asset.getAssignedUser().getUserLabel() : UNASSIGNED_USER_NAME;
	}

	private File assetTypeImagePath(AssetType assetType) {
		return (assetType.hasImage()) ? new File(PathHandler.getAssetTypeImageFile(assetType), assetType.getImageName()) : null;
	}

    private File assetProfileImagePath(Asset asset) {
        if ((asset.getImageName() == null))
            return null;

        try {
            URL imageURL = s3Service.getAssetProfileImageMediumURL(asset.getId(), asset.getImageName());
            File image = PathHandler.getTempFile();
            FileUtils.copyURLToFile(imageURL, image);
            return image;
        } catch (Exception e) {
            return null;
        }
    }

	private Map<String, Object> produceInfoOptionMap() {
		Map<String, Object> infoOptions = new HashMap<String, Object>();
		for (InfoOptionBean option : asset.getOrderedInfoOptionList()) {
			if (option.getInfoField().getFieldType().equals(InfoFieldBean.DATEFIELD_FIELD_TYPE)) {
				infoOptions.put(normalizeString(option.getInfoField().getName()), formatDate(option));
			} else {
				infoOptions.put(normalizeString(option.getInfoField().getName()), option.getName());
			}
		}
		return infoOptions;
	}

	private String formatDate(InfoOptionBean option) {
		Date date = new Date(Long.parseLong(option.getName()));
		return formatDate(date, option.getInfoField().isIncludeTime());
	}

	private String assetStatusName() {
		return (asset.getAssetStatus() != null) ? asset.getAssetStatus().getName() : null;
	}

}
