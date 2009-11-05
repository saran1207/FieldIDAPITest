package com.n4systems.fieldid.actions.reports;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.AggregateReportManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.search.InspectionReportAction;
import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.util.AggregateReport;


public class AggregateReportAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AggregateReportAction.class);

	private final AggregateReportManager aggregateReportManager;
	
	private InspectionSearchContainer criteria;
	
	protected String searchId;
	private Long currentPage;
	
	private AggregateReport report;
	
	public AggregateReportAction(AggregateReportManager aggregateReportManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.aggregateReportManager = aggregateReportManager;
	}
	
	private boolean findCriteria() {
		if(getSession().containsKey( InspectionReportAction.REPORT_CRITERIA ) && getSession().get( InspectionReportAction.REPORT_CRITERIA ) != null) {
			criteria = (InspectionSearchContainer)getSession().get( InspectionReportAction.REPORT_CRITERIA );
		}
		
		if( criteria == null || searchId == null || !searchId.equals( criteria.getSearchId() ) ) {
			return false;
		}
		return true;
	}
	
	@SkipValidation
	public String doShow(){
		if( !findCriteria() ) {
			addFlashErrorText("error.reportexpired");
			return ERROR;
		}
		try {
			List<Long> inspectionIds = persistenceManager.idSearch(criteria, criteria.getSecurityFilter());
			report = aggregateReportManager.createAggregateReport(inspectionIds);
		} catch (Exception e) {
			logger.error("could not produce aggregate report", e);
			addFlashErrorText("error.couldnotgeneratesummaryreport");
			return ERROR;
		}
		
		return SUCCESS;
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
