package com.n4systems.model.downloadlink;

import java.util.List;
import java.util.concurrent.Executor;

import com.n4systems.model.Asset;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.reporting.ReportDefiner;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.AssetExportTask;
import com.n4systems.taskscheduling.task.AutoAttributeExportTask;
import com.n4systems.taskscheduling.task.CustomerExportTask;
import com.n4systems.taskscheduling.task.DownloadTaskFactory;
import com.n4systems.taskscheduling.task.ExcelReportExportTask;
import com.n4systems.taskscheduling.task.PrintEventSummaryReportTask;
import com.n4systems.taskscheduling.task.UserExportTask;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.views.ExcelOutputHandler;
import com.n4systems.util.views.TableView;

public class DownloadCoordinator {
	private final Executor executor;
	private final Saver<DownloadLink> linkSaver;
	private final User user;
	private final DownloadLinkFactory linkFactory;
	private final DownloadTaskFactory taskFactory;
	
	public DownloadCoordinator(User user, Saver<DownloadLink> linkSaver, Executor executor, DownloadLinkFactory linkFactory, DownloadTaskFactory taskFactory) {
		this.user = user;
		this.linkSaver = linkSaver;
		this.executor = executor;
		this.linkFactory = linkFactory;
		this.taskFactory = taskFactory;
	}
	
	public DownloadCoordinator(User user, Saver<DownloadLink> linkSaver) {
		this(user, linkSaver, TaskExecutor.getInstance(), new DownloadLinkFactory(), new DownloadTaskFactory(linkSaver));
	}
	
	private DownloadLink createDownloadLink(String name, ContentType type) {
		DownloadLink link = linkFactory.createDownloadLink(user, name, type);
		linkSaver.save(link);
		return link;
	}
	
	public DownloadLink generateExcel(String name, String downloadUrl, SearchDefiner<TableView> searchDefiner, List<String> columnTitles, ExcelOutputHandler[] outputHandlers) {
		DownloadLink link = createDownloadLink(name, ContentType.EXCEL);
		ExcelReportExportTask task = taskFactory.createExcelTask(link, downloadUrl, searchDefiner, columnTitles, outputHandlers);
		
		executor.execute(task);
		return link;
	}
	
	public DownloadLink generateEventSummaryReport(String name, String downloadUrl, ReportDefiner reportDefiner, List<Long> eventIds) {
		DownloadLink link = createDownloadLink(name, ContentType.PDF);
		PrintEventSummaryReportTask task = taskFactory.createPrintEventSummaryReportTask(link, downloadUrl, reportDefiner, eventIds);
		
		executor.execute(task);
		return link;
	}
	
	public DownloadLink generateCustomerExport(String name, String downloadUrl, ListLoader<CustomerOrg> customerLoader, SecurityFilter filter) {
		DownloadLink link = createDownloadLink(name, ContentType.EXCEL);
		CustomerExportTask task = taskFactory.createCustomerExportTask(link, downloadUrl, customerLoader, filter);
		
		executor.execute(task);
		return link;
	}
	
	public DownloadLink generateAutoAttributeExport(String name, String downloadUrl, ListLoader<AutoAttributeDefinition> attribLoader) {
		DownloadLink link = createDownloadLink(name, ContentType.EXCEL);
		AutoAttributeExportTask task = taskFactory.createAutoAttributeExportTask(link, downloadUrl, attribLoader);
		
		executor.execute(task);
		return link;
	}
	
	public void generateAssetExport(String name, String downloadUrl, ListLoader<Asset> assetLoader) {
		DownloadLink link = createDownloadLink(name, ContentType.EXCEL);
		AssetExportTask task = taskFactory.createAssetExportTask(link, downloadUrl, assetLoader);
		
		executor.execute(task);
	}

	public DownloadLink generateUserExport(String name, String downloadUrl, ListLoader<User> userListLoader, SecurityFilter securityFilter) {
		DownloadLink link = createDownloadLink(name, ContentType.EXCEL); 
		UserExportTask task = taskFactory.createUserExportTask(link, downloadUrl, userListLoader);
		executor.execute(task);
		return link;
	}
}
