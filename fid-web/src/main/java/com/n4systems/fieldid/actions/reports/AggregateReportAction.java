package com.n4systems.fieldid.actions.reports;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.AggregateReportManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.SearchPerformerWithReadOnlyTransactionManagement;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.util.AggregateReport;
import com.n4systems.util.persistence.search.ImmutableBaseSearchDefiner;


public class AggregateReportAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AggregateReportAction.class);

	private final AggregateReportManager aggregateReportManager;
	
	private EventSearchContainer criteria;
	
	protected String searchId;
	private Long currentPage;
	
	private AggregateReport report;
	
	public AggregateReportAction(AggregateReportManager aggregateReportManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.aggregateReportManager = aggregateReportManager;
	}
	
	private boolean findCriteria() {
		criteria =  getSession().getReportCriteria();
		
		return !( criteria == null || searchId == null || !searchId.equals( criteria.getSearchId() ) );
	}
	
	@SkipValidation
	public String doShow(){
		if( !findCriteria() ) {
			addFlashErrorText("error.reportexpired");
			return ERROR;
		}
		try {
			List<Long> eventIds = getSearchIds();
			report = aggregateReportManager.createAggregateReport(eventIds);
		} catch (Exception e) {
			logger.error("could not produce aggregate report", e);
			addFlashErrorText("error.couldnotgeneratesummaryreport");
			return ERROR;
		}
		
		return SUCCESS;
	}

	private List<Long> getSearchIds() {
		return criteria.getMultiIdSelection().getSelectedIds();
	}
	
	public String getSearchId() {
		return searchId;
	}
	
	public void setSearchId( String searchId ) {
		this.searchId = searchId;
	}
	
	public Long getCurrentPage() {
		return currentPage;
	}

	public void getCurrentPage( Long pageNumber ) {
		this.currentPage = pageNumber;
	}

	public AggregateReport getReport() {
		return report;
	}
}
