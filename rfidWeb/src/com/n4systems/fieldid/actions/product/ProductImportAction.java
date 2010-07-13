package com.n4systems.fieldid.actions.product;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.ProductExporter;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.actions.importexport.AbstractImportAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.location.Location;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.notifiers.notifications.ProductImportFailureNotification;
import com.n4systems.notifiers.notifications.ProductImportSuccessNotification;
import com.n4systems.security.Permissions;
import com.n4systems.util.ArrayUtils;

@SuppressWarnings("serial")
@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class ProductImportAction extends AbstractImportAction {
	private Logger logger = Logger.getLogger(ProductImportAction.class);
	
	private ProductType type;
	private InputStream exampleExportFileStream;
	private String exampleExportFileSize;
	
	public ProductImportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected Importer createImporter(MapReader reader) {
		return getImporterFactory().createProductImporter(reader, getUser(), type);
	}
	
	@Override
	protected ImportSuccessNotification createSuccessNotification() {
		return new ProductImportSuccessNotification(getUser());
	}
	
	@Override
	protected ImportFailureNotification createFailureNotification() {
		return new ProductImportFailureNotification(getUser());
	}
	
	public String doDownloadExample() {
		ProductExporter exporter = new ProductExporter(getLoaderFactory().createPassthruListLoader(Arrays.asList(createExampleProduct())));
		
		MapWriter writer = null;
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try {
			writer = new ExcelMapWriter(byteOut, getPrimaryOrg().getDateFormat());
			exporter.export(writer);
			
		} catch (Exception e) {
			logger.error("Failed generating example asset export", e);
			return ERROR;
		} finally {
			StreamUtils.close(writer);
		}
		
		byte[] bytes = byteOut.toByteArray();
		exampleExportFileSize = String.valueOf(bytes.length);
		exampleExportFileStream = new ByteArrayInputStream(bytes);
		
		return SUCCESS;
	}
	
	private Product createExampleProduct() {
		Product example = new Product();
		
		example.setSerialNumber(getText("example.product.serialNumber"));
		example.setRfidNumber(getText("example.product.rfidNumber"));
		example.setCustomerRefNumber(getText("example.product.customerRefNumber"));
		example.setOwner(getUser().getOwner());
		example.setAdvancedLocation(Location.onlyFreeformLocation(getText("example.product.location")));
		example.setPurchaseOrder(getText("example.product.purchaseOrder"));
		example.setComments(getText("example.product.comments"));
		example.setIdentified(new Date());
		
		ProductStatusBean exampleStatus = getExampleProductStatus();
		if (exampleStatus != null) {
			example.setProductStatus(exampleStatus);
		}
		
		InfoOptionBean exampleOption;
		for (InfoFieldBean field: type.getInfoFields()) {
			exampleOption = new InfoOptionBean();
			exampleOption.setInfoField(field);
			exampleOption.setStaticData(false);
			exampleOption.setName("");
			
			example.getInfoOptions().add(exampleOption);
		}
		
		return example;
	}
	
	private ProductStatusBean getExampleProductStatus() {
		List<ProductStatusBean> statuses = getLoaderFactory().createProductStatusListLoader().load();
		
		return (statuses.isEmpty()) ? null : statuses.get(0);
	}
	
	public ProductType getProductType() {
		return type;
	}

	public Long getProductTypeId() {
		return (type != null) ? type.getId() : null;
	}

	public void setProductTypeId(Long id) {
		if (type == null || !type.getId().equals(id)) {
			type = getLoaderFactory().createProductTypeLoader().setStandardPostFetches().setId(id).load();
		}
	}
	
	/*
	 * Example export file download params
	 */
	private String getExportFileName() {
		String exportName = type.getName();
		return getText("label.export_file.asset", ArrayUtils.newArray(exportName));
	}
	
	public String getFileName() {
		return ContentType.EXCEL.prepareFileName(getExportFileName());
	}
	
	public String getFileSize() {
		return exampleExportFileSize;
	}

	public String getContentType() {
		return ContentType.EXCEL.getMimeType();
	}
	
	public InputStream getFileStream() {
		return exampleExportFileStream;
	}
}
