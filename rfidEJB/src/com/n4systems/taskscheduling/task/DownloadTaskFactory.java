package com.n4systems.taskscheduling.task;

import java.util.List;

import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Product;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.reporting.InspectionReportType;
import com.n4systems.reporting.ReportDefiner;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.views.ExcelOutputHandler;
import com.n4systems.util.views.TableView;

public class DownloadTaskFactory {

	public DownloadTaskFactory() {}

	public ExcelReportExportTask createExcelTask(DownloadLink link, String downloadUrl, SearchDefiner<TableView> searchDefiner, List<String> columnTitles, ExcelOutputHandler[] outputHandlers) {
		ExcelReportExportTask task = new ExcelReportExportTask(link, downloadUrl);
		task.setSearchDefiner(searchDefiner);
		task.setColumnTitles(columnTitles);
		task.setCellHandlers(outputHandlers);
		
		return task;
	}
	
	public PrintAllInspectionCertificatesTask createPrintAllInspectionCertificatesTask(DownloadLink link, String downloadUrl, InspectionReportType type, List<Long> inspectionIds) {
		PrintAllInspectionCertificatesTask task = new PrintAllInspectionCertificatesTask(link, downloadUrl);
		task.setReportType(type);
		task.setInspectionIds(inspectionIds);
		
		return task;
	}
	
	public PrintAllProductCertificatesTask createPrintAllProductCertificatesTask(DownloadLink link, String downloadUrl, List<Long> productIds) {
		PrintAllProductCertificatesTask task = new PrintAllProductCertificatesTask(link, downloadUrl);
		task.setProductIdList(productIds);
		
		return task;
	}
	
	public PrintInspectionSummaryReportTask createPrintInspectionSummaryReportTask(DownloadLink link, String downloadUrl, ReportDefiner reportDefiner) {
		PrintInspectionSummaryReportTask task = new PrintInspectionSummaryReportTask(link, downloadUrl);
		task.setReportDefiner(reportDefiner);
		
		return task;
	}
	
	public CustomerExportTask createCustomerExportTask(DownloadLink link, String downloadUrl, SecurityFilter filter) {
		CustomerExportTask task = new CustomerExportTask(link, downloadUrl, filter);
		return task;
	}
	
	public AutoAttributeExportTask createAutoAttributeExportTask(DownloadLink link, String downloadUrl, ListLoader<AutoAttributeDefinition> attribLoader) {
		AutoAttributeExportTask task = new AutoAttributeExportTask(link, downloadUrl, attribLoader);
		return task;
	}
	
	public ProductExportTask createProductExportTask(DownloadLink link, String downloadUrl, ListLoader<Product> productLoader) {
		ProductExportTask task = new ProductExportTask(link, downloadUrl, productLoader);
		return task;
	}
}
