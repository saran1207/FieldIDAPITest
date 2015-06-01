package com.n4systems.taskscheduling.task;

import com.n4systems.ejb.SearchPerformer;
import com.n4systems.ejb.SearchPerformerWithReadOnlyTransactionManagement;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.util.excel.ExcelXSSFBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.views.ExcelOutputHandler;
import com.n4systems.util.views.TableView;
import com.n4systems.util.views.TableViewExcelHandler;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class ExcelReportExportTask extends DownloadTask implements SearchDefiner<TableView> {
	private static final long serialVersionUID = 1L;
	private static final int PAGE_SIZE = 256;
	
	private SearchDefiner<TableView> searchDefiner;
	private List<String> columnTitles = new ArrayList<String>();
	private ExcelOutputHandler[] cellHandlers;
	
	private int page = 0;
	
	public ExcelReportExportTask(DownloadLink downloadLink, String downloadUrl) {
		super(downloadLink, downloadUrl, "excelReportDownload");
	}
	
	@Override
	protected void generateFile(OutputStream fileContents, User user, String downloadName) throws Exception {
		SecurityFilter filter = user.getSecurityFilter();
		
		DateTimeDefiner dateTimeDefiner = new DateTimeDefiner(user);
		SearchPerformer performSearch = new SearchPerformerWithReadOnlyTransactionManagement();
		
		int totalPages = performSearch.countPages(this, filter, getPageSize());
		
		// we'll initalize the table view with one 1 row .. this will resize dynamically as we append tables
		TableView masterTable = new TableView(0, getColumnTitles().size());
		do {
			
			
			masterTable.append(performSearch.search(this, filter).getPageResults());
			page++;
			
		} while(page <= totalPages);
		
		// we now need to run all the cell handlers on the table so the values are properly converted
		TableViewExcelHandler tableHandler = new TableViewExcelHandler(cellHandlers);
		tableHandler.handle(masterTable);
		
		// create an excel builder and add our data
		ExcelXSSFBuilder excelBuilder = new ExcelXSSFBuilder(dateTimeDefiner);
		excelBuilder.createSheet("Report", getColumnTitles(), masterTable);
		
		//write the file
		excelBuilder.writeToStream(fileContents);
	}

	@Override
	public List<SortTerm> getSortTerms() {
		return searchDefiner.getSortTerms();
	}

	@Override
	public ResultTransformer<TableView> getTransformer() {
		return searchDefiner.getTransformer();
	}

	@Override
	public int getPage() {
		return page;
	}

	@Override
	public int getPageSize() {
		return PAGE_SIZE;
	}


	@Override
	public Class<?> getSearchClass() {
		return searchDefiner.getSearchClass();
	}

	@Override
	public List<SearchTermDefiner> getSearchTerms() {
		return searchDefiner.getSearchTerms();
	}
	
	@Override
	public List<JoinTerm> getJoinTerms() {
		return searchDefiner.getJoinTerms();
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

	@Override
	public List<QueryFilter> getSearchFilters() {
		return searchDefiner.getSearchFilters();
	}
	
	public void setCellHandlers(ExcelOutputHandler[] cellHandlers) {
		this.cellHandlers = cellHandlers;
	}

}
