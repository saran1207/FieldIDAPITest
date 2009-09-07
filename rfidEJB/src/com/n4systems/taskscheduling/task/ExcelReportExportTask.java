package com.n4systems.taskscheduling.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ExcelBuilder;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.views.TableView;


public class ExcelReportExportTask implements Runnable, SearchDefiner<TableView> {
	private static final long serialVersionUID = 1L;
	private static final int PAGE_SIZE = 100;
	private static Logger logger = Logger.getLogger(ExcelReportExportTask.class);
	
	private SearchDefiner<TableView> searchDefiner;
	private Long userId;
	private String packageName;
	private List<String> columnTitles = new ArrayList<String>();
	
	private int page = 0; 
	@SuppressWarnings("unused")
    private int totalResults = 0;
	
	public void run() {
		try {
			UserBean user = ServiceLocator.getUser().getUser(getUserId());
			
			DateTimeDefiner dateTimeDefiner = new DateTimeDefiner(user);
			
			logger.info("Generating report ... ");
			
			String subject = "Excel Search Report for " + getPackageName();

			int totalPages = ServiceLocator.getPersistenceManager().countAllPages(getSearchClass(), getPageSize(), getSecurityFilter());
			
			// we'll initalize the table view with one 1 row .. this will resize dynamically as we append tables
			TableView masterTable = new TableView(0, getColumnTitles().size());
			do {
				
				masterTable.append(ServiceLocator.getPersistenceManager().search(this));
				page++;
				
			} while(page <= totalPages);
					
			// create an excel builder and add our data
			ExcelBuilder excelBuilder = new ExcelBuilder(dateTimeDefiner);
			excelBuilder.createSheet("Report", getColumnTitles(), masterTable);

			// setup the output file
			File tempDir = PathHandler.getTempDir();
			File reportFile = new File(tempDir, getPackageName() + ".xls");
			
			//write the file
			excelBuilder.writeToFile(reportFile);
			
			logger.info("Export to Excel Complete [" + reportFile.getPath() + "]");
			
			// TODO this should use a resource file to put out the correct language and move html to the template system for emails.
			String body = "<h4>Your Excel report is ready</h4>";
			body += "<br />Please see the attached Excel file.<br />";
			
			logger.info("Sending notification email [" + user.getEmailAddress() + "]");
			MailMessage message = new MailMessage(subject, body, user.getEmailAddress());
			message.addAttachment(reportFile);
			ServiceLocator.getMailManager().sendMessage(message);
			
			FileUtils.deleteDirectory(tempDir);
			
		} catch (Exception e) {
			logger.error("Excel Export Task failed", e);
		}
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

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	public Class<?> getSearchClass() {
		return searchDefiner.getSearchClass();
	}

	public List<SearchTermDefiner> getSearchTerms() {
		return searchDefiner.getSearchTerms();
	}

	public String[] getJoinColumns() {
		return searchDefiner.getJoinColumns();
	}
	
	public SecurityFilter getSecurityFilter() {
		return searchDefiner.getSecurityFilter();
	}

	public SearchDefiner<TableView> getSearchDefiner() {
		return searchDefiner;
	}

	public Long getUserId() { 
		return userId;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public List<String> getColumnTitles() {
		return columnTitles;
	}

	public void setSearchDefiner(SearchDefiner<TableView> searchDefiner) {
		this.searchDefiner = searchDefiner;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setColumnTitles(List<String> columnTitles) {
		this.columnTitles = columnTitles;
	}
	
}
