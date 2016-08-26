package com.n4systems.taskscheduling.task;

import com.n4systems.exporting.ExporterFactory;
import com.n4systems.mail.MailManager;
import com.n4systems.model.Asset;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.reporting.ReportDefiner;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.views.ExcelOutputHandler;
import com.n4systems.util.views.TableView;

import java.util.List;

public class DownloadTaskFactory {
	private final Saver<DownloadLink> linkSaver;
	private final ExporterFactory exporterFactory;
	
	public DownloadTaskFactory(Saver<DownloadLink> linkSaver, ExporterFactory exporterFactory) {
		this.linkSaver = linkSaver;
		this.exporterFactory = exporterFactory;
	}
	
	public DownloadTaskFactory(Saver<DownloadLink> linkSaver) {
		this(linkSaver, new ExporterFactory());
	}
	
	protected MailManager createMailManager() {
		return ServiceLocator.getMailManager();
	}
	
	public ExcelReportExportTask createExcelTask(DownloadLink link, String downloadUrl, SearchDefiner<TableView> searchDefiner, List<String> columnTitles, ExcelOutputHandler[] outputHandlers) {
		ExcelReportExportTask task = new ExcelReportExportTask(link, downloadUrl);
		task.setSearchDefiner(searchDefiner);
		task.setColumnTitles(columnTitles);
		task.setCellHandlers(outputHandlers);
		
		return task;
	}
	
	public PrintEventSummaryReportTask createPrintEventSummaryReportTask(DownloadLink link, String downloadUrl, ReportDefiner reportDefiner, List<Long> eventIds) {
		PrintEventSummaryReportTask task = new PrintEventSummaryReportTask(link, downloadUrl);
		task.setReportDefiner(reportDefiner);
        task.setEventIds(eventIds);
		
		return task;
	}
	
	public CustomerExportTask createCustomerExportTask(DownloadLink link, String downloadUrl, ListLoader<CustomerOrg> customerLoader, SecurityFilter filter) {
		CustomerExportTask task = new CustomerExportTask(link, downloadUrl, linkSaver, createMailManager(), exporterFactory.createCustomerExporter(customerLoader, filter));
		return task;
	}
	
	public AutoAttributeExportTask createAutoAttributeExportTask(DownloadLink link, String downloadUrl,  ListLoader<AutoAttributeDefinition> attribLoader) {
		AutoAttributeExportTask task = new AutoAttributeExportTask(link, downloadUrl, linkSaver, createMailManager(), exporterFactory.createAutoAttributeExporter(attribLoader));
		return task;
	}
	
	public AssetExportTask createAssetExportTask(DownloadLink link, String downloadUrl, ListLoader<Asset> productLoader) {
		AssetExportTask task = new AssetExportTask(link, downloadUrl, linkSaver, createMailManager(), exporterFactory.createAssetExporter(productLoader));
		return task;
	}

	public UserExportTask createUserExportTask(DownloadLink link, String downloadUrl, ListLoader<User> userListLoader) {
		UserExportTask task = new UserExportTask(link, downloadUrl, linkSaver, createMailManager(), exporterFactory.createUserExporter(userListLoader));
		return task;
	}
}
