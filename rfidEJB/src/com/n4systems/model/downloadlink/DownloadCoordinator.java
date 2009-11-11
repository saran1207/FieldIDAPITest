package com.n4systems.model.downloadlink;

import java.util.List;

import rfid.ejb.entity.UserBean;

import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.ExcelReportExportTask;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.views.ExcelOutputHandler;
import com.n4systems.util.views.TableView;

public class DownloadCoordinator {

	private final TaskExecutor taskExecutor;
	private final DownloadLinkSaver linkSaver;
	private final UserBean user;
	
	public DownloadCoordinator(UserBean user, DownloadLinkSaver linkSaver, TaskExecutor taskExecutor) {
		this.linkSaver = linkSaver;
		this.user = user;
		this.taskExecutor = taskExecutor;
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
	
	private ExcelReportExportTask createExcelTask(DownloadLink link, SearchDefiner<TableView> searchDefiner, List<String> columnTitles, ExcelOutputHandler[] outputHandlers, String downloadUrl) {
		ExcelReportExportTask exportTask = new ExcelReportExportTask(link, downloadUrl);
		exportTask.setSearchDefiner(searchDefiner);
		exportTask.setColumnTitles(columnTitles);
		exportTask.setCellHandlers(outputHandlers);
		
		return exportTask;
	}
	
	public void generateExcel(String name, SearchDefiner<TableView> searchDefiner, List<String> columnTitles, ExcelOutputHandler[] outputHandlers, String downloadUrl) {
		DownloadLink link = createDownloadLink(name, ContentType.EXCEL);
		ExcelReportExportTask exportTask = createExcelTask(link, searchDefiner, columnTitles, outputHandlers, downloadUrl);
		
		taskExecutor.execute(exportTask);
	}
	
}
