package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.fieldid.actions.search.InspectionReportAction;
import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.JobSite;
import com.n4systems.util.ListingPair;

public class InspectionMassUpdate extends MassUpdate {
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger( InspectionMassUpdate.class );
	
	private InspectionManager inspectionManager;
	
	private InspectionSearchContainer criteria;
	
	private Inspection inspection = new Inspection();
	
	private List<ListingPair> jobSites;

	public InspectionMassUpdate( InspectionManager inspectionManager,  CustomerManager customerManager, MassUpdateManager massUpdateManager, PersistenceManager persistenceManager ) {
		super( customerManager, massUpdateManager, persistenceManager );
		this.inspectionManager = inspectionManager;
	}
	
	private void applyCriteriaDefaults() { 
		setCustomer( criteria.getCustomer() );
		setDivision( criteria.getDivision() );
		setInspectionBook( criteria.getInspectionBook() );
		setJobSite( criteria.getJobSite() );
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
	public String doEdit(){
		if( !findCriteria() ) {
			addFlashError( getText( "error.reportexpired" ) );
			return ERROR;
		}
		
		applyCriteriaDefaults();
		return SUCCESS;
	}
	
	public String doSave(){
		if( !findCriteria() ) {
			addFlashError( getText( "error.reportexpired" ) );
			return ERROR;
		}
		
		try {
			List<Long> inspectionIds = persistenceManager.idSearch(criteria);
			Long results = massUpdateManager.updateInspections(inspectionIds, inspection, select, getSessionUser().getUniqueID());
			List<String> messageArgs = new ArrayList<String>();
			messageArgs.add( results.toString() );
			addFlashMessage( getText( "message.inspectionmassupdatesuccessful", messageArgs ) ) ;
			
			return SUCCESS;
		} catch ( UpdateFailureException ufe) {
			logger.error( "failed to run a mass update on inspections", ufe );
		} 
		catch (Exception e){
			logger.error( "failed to run a mass update on inspections", e );
		}
		
		addActionError( getText( "error.failedtomassupdate" ) );
		return INPUT;
	}
	

	public Long getCustomer() {
		return ( inspection.getCustomer() == null ) ? null : inspection.getCustomer().getId();
	}
	
	public void setCustomer(Long customer) {
		if( customer == null ) {
			inspection.setCustomer( null );
		} else if( inspection.getCustomer() == null || !customer.equals( inspection.getCustomer().getId() ) ) {
			Customer customerBean = customerManager.findCustomer( customer, getSecurityFilter() );
			inspection.setCustomer( customerBean );
		}
	}

	public Long getDivision() {
		return ( inspection.getDivision() == null ) ? null : inspection.getDivision().getId();
	}
	
	public void setDivision(Long division) {
		if( division == null ) {
			inspection.setDivision( null );
		} else if( inspection.getDivision() == null || !division.equals( inspection.getDivision().getId() ) ) {
			Division divisionBean = customerManager.findDivision( division, getSecurityFilter() );
			inspection.setDivision( divisionBean );
		}
	}
	
	public String getLocation() {
		return inspection.getLocation();
	}
	
	public void setLocation( String location ) {
		inspection.setLocation( location );
	}
	
	public Long getInspectionBook() {
		return ( inspection.getBook() == null ) ? null : inspection.getBook().getId();
	}
	
	public void setInspectionBook( Long inspectionBookId ) {
		if( inspectionBookId == null ) {
			inspection.setBook( null );
		} else if( inspection.getBook() == null || !inspectionBookId.equals( inspection.getBook().getId() ) ) {
			InspectionBook inspectionBook = persistenceManager.find( InspectionBook.class, inspectionBookId );
			inspection.setBook( inspectionBook );
		}
	}
	
	public Collection<ListingPair> getCustomers() {
		return customerManager.findCustomersLP(getTenantId(), getSecurityFilter());
	}
	
	public Collection<ListingPair> getDivisions() {
		Collection<ListingPair> divisions = new ArrayList<ListingPair>();
		if( getCustomer() != null ) {
			divisions = customerManager.findDivisionsLP(getCustomer(), getSecurityFilter());
		} 
		
		return divisions;
	}
	
	public Collection<ListingPair> getInspectionBooks() {	
		return inspectionManager.findAvailableInspectionBooksLP( getSecurityFilter(), false );
	}

	public boolean isPrintable() {
		return inspection.isPrintable();
	}

	public void setPrintable( boolean printable ) {
		inspection.setPrintable( printable );
	}
	
	public Long getJobSite() {
		return ( inspection.getJobSite() != null ) ? inspection.getJobSite().getId() : null;
	}

	
	public void setJobSite( Long jobSite ) {
		if (jobSite == null) {
			inspection.setJobSite( null );
		} else if (inspection.getJobSite() == null || !jobSite.equals(inspection.getJobSite().getId())) {
			inspection.setJobSite( persistenceManager.find( JobSite.class, jobSite, getSecurityFilter().setDefaultTargets() ) );
		}
	}

	public List<ListingPair> getJobSites() {
		if( jobSites == null ) {
			jobSites = persistenceManager.findAllLP( JobSite.class, getSecurityFilter().setDefaultTargets(), "name" );
		}
		return jobSites;
	}
}
