package com.n4systems.model.downloadlink;

import java.util.List;
import java.util.concurrent.Executor;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.reporting.InspectionReportType;
import com.n4systems.reporting.ReportDefiner;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.AutoAttributeExportTask;
import com.n4systems.taskscheduling.task.CustomerExportTask;
import com.n4systems.taskscheduling.task.DownloadTaskFactory;
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
	private final DownloadLinkFactory linkFactory;
	private final DownloadTaskFactory taskFactory;
	
	public DownloadCoordinator(UserBean user, Saver<DownloadLink> linkSaver, Executor executor, DownloadLinkFactory linkFactory, DownloadTaskFactory taskFactory) {
		this.user = user;
		this.linkSaver = linkSaver;
		this.executor = executor;
		this.linkFactory = linkFactory;
		this.taskFactory = taskFactory;
	}
	
	public DownloadCoordinator(UserBean user) {
		this(user, new DownloadLinkSaver(), TaskExecutor.getInstance(), new DownloadLinkFactory(), new DownloadTaskFactory());
	}
	
	private DownloadLink createDownloadLink(String name, ContentType type) {
		DownloadLink link = linkFactory.createDownloadLink(user, name, type);
		linkSaver.save(link);
		return link;
	}
	
	public void generateExcel(String name, String downloadUrl, SearchDefiner<TableView> searchDefiner, List<String> columnTitles, ExcelOutputHandler[] outputHandlers) {
		DownloadLink link = createDownloadLink(name, ContentType.EXCEL);
		ExcelReportExportTask task = taskFactory.createExcelTask(link, downloadUrl, searchDefiner, columnTitles, outputHandlers);
		
		executor.execute(task);
	}
	
	public void generateAllInspectionCertificates(String name, String downloadUrl, InspectionReportType type, List<Long> inspectionIds) {
		DownloadLink link = createDownloadLink(name, ContentType.ZIP);
		PrintAllInspectionCertificatesTask task = taskFactory.createPrintAllInspectionCertificatesTask(link, downloadUrl, type, inspectionIds);
		
		executor.execute(task);
	}
	
	public void generateAllProductCertificates(String name, String downloadUrl, List<Long> productIds) {
		DownloadLink link = createDownloadLink(name, ContentType.ZIP);
		PrintAllProductCertificatesTask task = taskFactory.createPrintAllProductCertificatesTask(link, downloadUrl, productIds);
		
		executor.execute(task);
	}
	
	public void generateInspectionSummaryReport(String name, String downloadUrl, ReportDefiner reportDefiner) {
		DownloadLink link = createDownloadLink(name, ContentType.PDF);
		PrintInspectionSummaryReportTask task = taskFactory.createPrintInspectionSummaryReportTask(link, downloadUrl, reportDefiner);
		
		executor.execute(task);
	}
	
	public void generateCustomerExport(String name, String downloadUrl, ContentType type, SecurityFilter filter) {
		DownloadLink link = createDownloadLink(name, type);
		CustomerExportTask task = taskFactory.createCustomerExportTask(link, downloadUrl, filter);
		
		executor.execute(task);
	}
	
	public void generateAutoAttributeExport(String name, String downloadUrl, ContentType type, ListLoader<AutoAttributeDefinition> attribLoader) {
		DownloadLink link = createDownloadLink(name, type);
		AutoAttributeExportTask task = taskFactory.createAutoAttributeExportTask(link, downloadUrl, attribLoader);
		
		executor.execute(task);
	}
}
