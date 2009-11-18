package com.n4systems.reporting;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.Product;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.ReportMap;

public class ProductReportMapProducer extends ReportMapProducer {

	private final Product product;
	private ReportMap<Object> reportMap = new ReportMap<Object>();
	
	public ProductReportMapProducer(Product product, DateTimeDefinition dateTimeDefinition) {
		super(dateTimeDefinition);
		this.product = product;
	}

	@Override
	public ReportMap<Object> produceMap() {
		addProductParams();
		addProductTypeParams();
		return reportMap;
	}

	private void addProductParams() {
		reportMap.put("productDesc", product.getDescription());
		reportMap.put("rfidNumber", product.getRfidNumber());
		reportMap.put("serialNumber", product.getSerialNumber());
		reportMap.put("reelId", product.getSerialNumber());
		reportMap.put("poNumber", product.getPurchaseOrder());
		reportMap.put("customerRefNumber", product.getCustomerRefNumber());
		reportMap.put("dateOfIssue", formatDate(product.getCreated(), false));
		reportMap.put("productComment", product.getComments());
		reportMap.put("productLocation", product.getLocation());
		reportMap.put("productIdentified", formatDate(product.getIdentified(),false));
		reportMap.put("currentProductStatus", productStatusName());
		reportMap.put("infoOptionMap", produceInfoOptionMap());
		reportMap.put("infoOptionDataSource", new JRBeanCollectionDataSource(product.getOrderedInfoOptionList()));
	}

	private void addProductTypeParams() {
		reportMap.put("typeName", product.getType().getName());
		reportMap.put("warning", product.getType().getWarnings());
		reportMap.put("certificateText", product.getType().getManufactureCertificateText());
		
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
