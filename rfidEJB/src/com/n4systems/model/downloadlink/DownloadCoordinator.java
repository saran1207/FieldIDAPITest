package com.n4systems.model.downloadlink;

import java.util.List;
import java.util.concurrent.Executor;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.reporting.InspectionReportType;
import com.n4systems.reporting.ReportDefiner;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.CustomerExportTask;
import com.n4systems.taskscheduling.task.ExcelReportExportTask;
import com.n4systems.taskscheduling.task.PrintAllInspectionCertificatesTask;
import com.n4systems.taskscheduling.task.PrintAllProductCertificatesTask;
import com.n4systems.taskscheduling.task.PrintInspectionSummaryReportTask;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.views.ExcelOutputHandler;
import com.n4systems.util.views.TableView;

public class DownloadCoordinator {
	private final Executor executor;
	private final Saver<DownloadLink> linkSaver;
	private final UserBean user;
	
	public DownloadCoordinator(UserBean user, Saver<DownloadLink> linkSaver, Executor executor) {
		this.linkSaver = linkSaver;
		this.user = user;
		this.executor = executor;
	}
	
	public DownloadCoordinator(UserBean user) {
		this(user, new DownloadLinkSaver(), TaskExecutor.getInstance());
	}
	
	private DownloadLink createDownloadLink(String name, ContentType type) {
		DownloadLink link = new DownloadLink();
		link.setState(DownloadState.REQUESTED);
		link.setContentType(type);
		link.setTenant(user.getTenant());
		link.setUser(user);
		link.setName(name);
		
		linkSaver.save(link);
		return link;
	}
	
	private ExcelReportExportTask createExcelTask(DownloadLink link, String downloadUrl, SearchDefiner<TableView> searchDefiner, List<String> columnTitles, ExcelOutputHandler[] outputHandlers) {
		ExcelReportExportTask task = new ExcelReportExportTask(link, downloadUrl);
		task.setSearchDefiner(searchDefiner);
		task.setColumnTitles(columnTitles);
		task.setCellHandlers(outputHandlers);
		
		return task;
	}
	
	public void generateExcel(String name, String downloadUrl, SearchDefiner<TableView> searchDefiner, List<String> columnTitles, ExcelOutputHandler[] outputHandlers) {
		DownloadLink link = createDownloadLink(name, ContentType.EXCEL);
		ExcelReportExportTask task = createExcelTask(link, downloadUrl, searchDefiner, columnTitles, outputHandlers);
		
		executor.execute(task);
	}
	
	private PrintAllInspectionCertificatesTask createPrintAllInspectionCertificatesTask(DownloadLink link, String downloadUrl, InspectionReportType type, List<Long> inspectionIds) {
		PrintAllInspectionCertificatesTask task = new PrintAllInspectionCertificatesTask(link, downloadUrl);
		task.setReportType(type);
		task.setInspectionIds(inspectionIds);
		
		return task;
	}
	
	public void generateAllInspectionCertificates(String name, String downloadUrl, InspectionReportType type, List<Long> inspectionIds) {
		DownloadLink link = createDownloadLink(name, ContentType.ZIP);
		PrintAllInspectionCertificatesTask task = createPrintAllInspectionCertificatesTask(link, downloadUrl, type, inspectionIds);
		
		executor.execute(task);
	}
	
	private PrintAllProductCertificatesTask createPrintAllProductCertificatesTask(DownloadLink link, String downloadUrl, List<Long> productIds) {
		PrintAllProductCertificatesTask task = new PrintAllProductCertificatesTask(link, downloadUrl);
		task.setProductIdList(productIds);
		
		return task;
	}
	
	public void generateAllProductCertificates(String name, String downloadUrl, List<Long> productIds) {
		DownloadLink link = createDownloadLink(name, ContentType.ZIP);
		PrintAllProductCertificatesTask task = createPrintAllProductCertificatesTask(link, downloadUrl, productIds);
		
		executor.execute(task);
	}
	
	private PrintInspectionSummaryReportTask createPrintInspectionSummaryReportTask(DownloadLink link, String downloadUrl, ReportDefiner reportDefiner) {
		PrintInspectionSummaryReportTask task = new PrintInspectionSummaryReportTask(link, downloadUrl);
		task.setReportDefiner(reportDefiner);
		
		return task;
	}
	
	public void generateInspectionSummaryReport(String name, String downloadUrl, ReportDefiner reportDefiner) {
		DownloadLink link = createDownloadLink(name, ContentType.PDF);
		PrintInspectionSummaryReportTask task = createPrintInspectionSummaryReportTask(link, downloadUrl, reportDefiner);
		
		executor.execute(task);
	}
	
	private CustomerExportTask createCustomerExportTask(DownloadLink link, String downloadUrl, SecurityFilter filter) {
		CustomerExportTask task = new CustomerExportTask(link, downloadUrl, filter);
		return task;
	}
	
	public void generateCustomerExport(String name, String downloadUrl, ContentType type, SecurityFilter filter) {
		DownloadLink link = createDownloadLink(name, type);
		CustomerExportTask task = createCustomerExportTask(link, downloadUrl, filter);
		
		executor.execute(task);
	}
}
