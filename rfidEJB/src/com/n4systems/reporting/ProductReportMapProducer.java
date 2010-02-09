package com.n4systems.reporting;

import java.io.File;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.ReportMap;

public class ProductReportMapProducer extends ReportMapProducer {

	private final Product product;
	public ProductReportMapProducer(Product product, DateTimeDefinition dateTimeDefinition) {
		super(dateTimeDefinition);
		this.product = product;
	}

	
	
	@Override
	protected void addParameters() {
		addProductParams();
		addProductTypeParams();
	}

	private void addProductParams() {
		add("productDesc", product.getDescription());
		add("rfidNumber", product.getRfidNumber());
		add("serialNumber", product.getSerialNumber());
		add("reelId", product.getSerialNumber());
		add("poNumber", product.getPurchaseOrder());
		add("customerRefNumber", product.getCustomerRefNumber());
		add("dateOfIssue", formatDate(product.getCreated(), false));
		add("productComment", product.getComments());
		add("productLocation", product.getLocation());
		add("productIdentified", formatDate(product.getIdentified(),false));
		add("currentProductStatus", productStatusName());
		add("infoOptionMap", produceInfoOptionMap());
		add("lastInspectionDate", formatDate(product.getLastInspectionDate(), true));
		add("infoOptionBeanList", product.getOrderedInfoOptionList());
		add("infoOptionDataSource", new JRBeanCollectionDataSource(product.getOrderedInfoOptionList()));
	}

	private void addProductTypeParams() {
		ProductType productType = product.getType();
		
		add("typeName", productType.getName());
		add("warning", productType.getWarnings());
		add("productWarning", productType.getWarnings());
		add("certificateText", productType.getManufactureCertificateText());
		add("productInstructions", productType.getInstructions());
		
		add("productImage", imagePath(productType));
	}

	private File imagePath(ProductType productType) {
		return (productType.hasImage()) ? new File(PathHandler.getProductTypeImageFile(productType), productType.getImageName()) : null;
	}
	
	private ReportMap<String> produceInfoOptionMap() {
		ReportMap<String> infoOptions = new ReportMap<String>(product.getOrderedInfoOptionList().size());
		for (InfoOptionBean option : product.getOrderedInfoOptionList()) {
			infoOptions.put(normalizeString(option.getInfoField().getName()), option.getName());
		}
		return infoOptions;
	}

	private String productStatusName() {
		return (product.getProductStatus() != null) ? product.getProductStatus().getName() : null;
	}



	



	
	
}
