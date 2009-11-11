package com.n4systems.taskscheduling.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.util.ExcelBuilder;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.views.ExcelOutputHandler;
import com.n4systems.util.views.TableView;
import com.n4systems.util.views.TableViewExcelHandler;


public class ExcelReportExportTask extends DownloadTask implements SearchDefiner<TableView> {
	private static final long serialVersionUID = 1L;
	private static final int PAGE_SIZE = 256;
	
	private final PersistenceManager persistenceManager;
	private SearchDefiner<TableView> searchDefiner;
	private List<String> columnTitles = new ArrayList<String>();
	private ExcelOutputHandler[] cellHandlers;
	
	private int page = 0;
	
	public ExcelReportExportTask(DownloadLink downloadLink, String downloadUrl, PersistenceManager persistenceManager) {
		super(downloadLink, downloadUrl, "excelReportDownload");
		this.persistenceManager = persistenceManager;
	}
	
	public ExcelReportExportTask(DownloadLink downloadLink, String downloadUrl) {
		this(downloadLink, downloadUrl, ServiceLocator.getPersistenceManager());
	}
	
	@Override
	protected void generateFile(File downloadFile, UserBean user) throws Exception {
		SecurityFilter filter = user.getSecurityFilter();
		
		DateTimeDefiner dateTimeDefiner = new DateTimeDefiner(user);

		int totalPages = persistenceManager.countPages(this, filter, getPageSize());
		
		// we'll initalize the table view with one 1 row .. this will resize dynamically as we append tables
		TableView masterTable = new TableView(0, getColumnTitles().size());
		do {
			masterTable.append(persistenceManager.search(this, filter));
			page++;
			
		} while(page <= totalPages);
		
		// we now need to run all the cell handlers on the table so the values are properly converted
		TableViewExcelHandler tableHandler = new TableViewExcelHandler(cellHandlers);
		tableHandler.handle(masterTable);
		
		// create an excel builder and add our data
		ExcelBuilder excelBuilder = new ExcelBuilder(dateTimeDefiner);
		excelBuilder.createSheet("Report", getColumnTitles(), masterTable);
		
		//write the file
		excelBuilder.writeToFile(downloadFile);		
	}

	public List<SortTerm> getSortTerms() {
		return searchDefiner.getSortTerms();
	}

	public ResultTransformer<TableView> getTransformer() {
		return searchDefiner.getTransformer();
	}

	public int getPage() {
		return page;
	}

	public int getPageSize() {
		return PAGE_SIZE;
	}

	public void setTotalResults(int totalResults) {}

	public Class<?> getSearchClass() {
		return searchDefiner.getSearchClass();
	}

	public List<SearchTermDefiner> getSearchTerms() {
		return searchDefiner.getSearchTerms();
	}

	public String[] getJoinColumns() {
		return searchDefiner.getJoinColumns();
	}

	public SearchDefiner<TableView> getSearchDefiner() {
		return searchDefiner;
	}
	
	public List<String> getColumnTitles() {
		return columnTitles;
	}

	public void setSearchDefiner(SearchDefiner<TableView> searchDefiner) {
		this.searchDefiner = searchDefiner;
	}

	public void setColumnTitles(List<String> columnTitles) {
		this.columnTitles = columnTitles;
	}

	public List<QueryFilter> getSearchFilters() {
		return searchDefiner.getSearchFilters();
	}
	
	public void setCellHandlers(ExcelOutputHandler[] cellHandlers) {
		this.cellHandlers = cellHandlers;
	}

}
